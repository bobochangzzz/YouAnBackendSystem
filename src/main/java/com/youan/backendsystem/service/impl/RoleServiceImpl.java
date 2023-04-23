package com.youan.backendsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.youan.backendsystem.common.ErrorCode;
import com.youan.backendsystem.exception.BusinessException;
import com.youan.backendsystem.model.dto.role.RoleQueryRequest;
import com.youan.backendsystem.model.entity.Role;
import com.youan.backendsystem.model.entity.User;
import com.youan.backendsystem.model.entity.UserRole;
import com.youan.backendsystem.model.enums.UserRoleEnum;
import com.youan.backendsystem.service.RoleService;
import com.youan.backendsystem.mapper.RoleMapper;
import com.youan.backendsystem.service.UserRoleService;
import com.youan.backendsystem.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
}
