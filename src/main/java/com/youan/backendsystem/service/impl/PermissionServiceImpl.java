package com.youan.backendsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.youan.backendsystem.common.ErrorCode;
import com.youan.backendsystem.exception.BusinessException;
import com.youan.backendsystem.mapper.PermissionMapper;
import com.youan.backendsystem.model.dto.perimission.PermissionQueryRequest;
import com.youan.backendsystem.model.entity.Permission;
import com.youan.backendsystem.service.PermissionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission>
        implements PermissionService {

        @Override
        public QueryWrapper<Permission> getQueryWrapper(PermissionQueryRequest permissionQueryRequest){
            if (permissionQueryRequest == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
            }
            Long id = permissionQueryRequest.getId();
            Long pid = permissionQueryRequest.getPid();
            Integer type = permissionQueryRequest.getType();
            String name = permissionQueryRequest.getName();
            Integer status = permissionQueryRequest.getStatus();
            String icon = permissionQueryRequest.getIcon();
            String compoent = permissionQueryRequest.getCompoent();
            String permissionvalue = permissionQueryRequest.getPermissionValue();
            String path = permissionQueryRequest.getPath();
            QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(StringUtils.isNotBlank(name), "name", name);
            queryWrapper.eq(status != null, "status", status);
//            queryWrapper.eq(id != null, "id", id);
//            queryWrapper.eq(pid != null, "pid", pid);
//            queryWrapper.eq(type != null, "type", type);
            queryWrapper.eq(StringUtils.isNotBlank(icon), "icon", icon);
            queryWrapper.eq(StringUtils.isNotBlank(compoent), "compoent", compoent);
            queryWrapper.eq(StringUtils.isNotBlank(permissionvalue), "permissionValue", permissionvalue);
            queryWrapper.eq(StringUtils.isNotBlank(path), "path", path);
            return queryWrapper;
        }
    }

