package com.youan.backendsystem.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.youan.backendsystem.model.dto.role.RoleQueryRequest;
import com.youan.backendsystem.model.entity.Role;
import com.youan.backendsystem.model.vo.CurrentRoleMenuVO;

import java.util.List;

/**
 * @author bobochang
 * @description 针对表【role】的数据库操作Service
 * @createDate 2023-04-20 15:40:45
 */
public interface RoleService extends IService<Role> {

    // 获取查询条件
    QueryWrapper<Role> getQueryWrapper(RoleQueryRequest roleQueryRequest);

    // 用户分配角色
    boolean assignRole(Long userId, String roleName);

    // 菜单分配角色
    boolean assignMenu(Long roleId, List<Long> menuIds);

    // 获取当前角色获得的菜单权限
    CurrentRoleMenuVO getMenuPermission(long roleId);
}
