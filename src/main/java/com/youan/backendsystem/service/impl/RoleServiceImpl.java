package com.youan.backendsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.youan.backendsystem.common.ErrorCode;
import com.youan.backendsystem.exception.BusinessException;
import com.youan.backendsystem.model.dto.role.RoleQueryRequest;
import com.youan.backendsystem.model.entity.*;
import com.youan.backendsystem.model.enums.UserRoleEnum;
import com.youan.backendsystem.model.vo.CurrentRoleMenuVO;
import com.youan.backendsystem.service.*;
import com.youan.backendsystem.mapper.RoleMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @author bobochang
 * @description 针对表【role】的数据库操作Service实现
 * @createDate 2023-04-20 15:40:45
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
        implements RoleService {

    @Resource
    private UserService userService;
    @Resource
    private UserRoleService userRoleService;
    @Resource
    private RoleMenuService roleMenuService;
    @Resource
    private MenuService menuService;

    @Override
    public QueryWrapper<Role> getQueryWrapper(RoleQueryRequest roleQueryRequest) {
        if (roleQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        String roleName = roleQueryRequest.getRoleName();
        Integer status = roleQueryRequest.getStatus();
        String remark = roleQueryRequest.getRemark();
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(roleName), "roleName", roleName);
        queryWrapper.eq(status != null, "status", status);
        queryWrapper.eq(StringUtils.isNotBlank(remark), "remark", remark);
        return queryWrapper;
    }

    @Override
    public boolean assignRole(Long userId, String userRoleEnumText) {
        // 将分配到的角色id存储到用户表中
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq(userId != 0L, "id", userId);
        User user = userService.getOne(userQueryWrapper);
        String enumValue = UserRoleEnum.getValueByText(userRoleEnumText);
        user.setUserRoleName(enumValue);
        // 先根据roleName查询角色id 再保存信息到用户角色表中
        QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<>();
        roleQueryWrapper.eq(StringUtils.isNotBlank(userRoleEnumText), "roleName", userRoleEnumText);
        Long roleId = getOne(roleQueryWrapper).getId();
        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(roleId);
        return userService.updateById(user) && userRoleService.save(userRole);
    }

    @Override
    public boolean assignMenu(Long roleId, List<Long> menuIds) {
        // 判断当前分配菜单是 添加 还是 修改
        QueryWrapper<RoleMenu> roleMenuQueryWrapper = new QueryWrapper<>();
        roleMenuQueryWrapper.eq(roleId != 0L, "roleId", roleId);
        RoleMenu roleMenu = new RoleMenu();
        if (roleMenuService.count(roleMenuQueryWrapper) > 0) {
            // 修改
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(StringUtils.join(menuIds, ","));
            return roleMenuService.updateById(roleMenu);
        }
        // 添加
        roleMenu.setRoleId(roleId);
        roleMenu.setMenuId(StringUtils.join(menuIds, ","));
        return roleMenuService.save(roleMenu);
    }

    @Override
    public CurrentRoleMenuVO getMenuPermission(long roleId) {
        CurrentRoleMenuVO currentRoleMenuVO = new CurrentRoleMenuVO();
        StringBuffer menuNameStr = new StringBuffer();
        // 根据 角色id查询关系表 当前角色拥有的菜单权限
        QueryWrapper<RoleMenu> roleMenuQueryWrapper = new QueryWrapper<>();
        roleMenuQueryWrapper.eq(roleId != 0L, "roleId", roleId);
        // 遍历获取到的String[]数组中的值并添加到list<long>列表中并对列表中的每个id值进行对应查询
        Arrays.stream(roleMenuService.getOne(roleMenuQueryWrapper).getMenuId().split(","))
                .map(Long::parseLong)
                .forEach(menuId -> {
                    // 根据menuId获取菜单名称
                    QueryWrapper<Menu> menuQueryWrapper = new QueryWrapper<>();
                    menuQueryWrapper.eq(menuId != 0L, "id", menuId);
                    String menuName = menuService.getOne(menuQueryWrapper).getName();
                    // 把每个menuName追加进menuNameStr中并每次都以逗号分隔
                    menuNameStr.append(menuName).append(",");
                });
        // 根据 角色id查询角色表 获取当前角色名称和标识
        Role role = getOne(new QueryWrapper<Role>().eq("id", roleId));
        currentRoleMenuVO.setRoleName(role.getRoleName());
        currentRoleMenuVO.setRoleIdentification(UserRoleEnum.getValueByText(role.getRoleName()));
        currentRoleMenuVO.setMenuNameStr(menuNameStr);
        return currentRoleMenuVO;
    }
}
