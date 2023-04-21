package com.youan.backendsystem.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.youan.backendsystem.model.dto.perimission.PermissionQueryRequest;
import com.youan.backendsystem.model.entity.Permission;


public interface PermissionService extends IService<Permission> {

    // 获取查询条件
    QueryWrapper<Permission> getQueryWrapper(PermissionQueryRequest permissionQueryRequest);


}
