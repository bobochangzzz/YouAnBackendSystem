package com.youan.backendsystem.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.youan.backendsystem.model.dto.role.RoleQueryRequest;
import com.youan.backendsystem.model.entity.Role;

/**
* @author bobochang
* @description 针对表【role】的数据库操作Service
* @createDate 2023-04-20 15:40:45
*/
public interface RoleService extends IService<Role> {

    // 获取查询条件
    QueryWrapper<Role> getQueryWrapper(RoleQueryRequest roleQueryRequest);

    // 角色分配
    boolean assignRole(Long userId, String roleName);
}
