package com.youan.backendsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.youan.backendsystem.common.ErrorCode;
import com.youan.backendsystem.exception.BusinessException;
import com.youan.backendsystem.mapper.MenuMapper;
import com.youan.backendsystem.model.dto.menu.MenuQueryRequest;
import com.youan.backendsystem.model.entity.Menu;
import com.youan.backendsystem.service.MenuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu>
        implements MenuService {

        @Override
        public QueryWrapper<Menu> getQueryWrapper(MenuQueryRequest menuQueryRequest){
            if (menuQueryRequest == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
            }
            Long id = menuQueryRequest.getId();
            Long pid = menuQueryRequest.getPid();
            Integer type = menuQueryRequest.getType();
            String name = menuQueryRequest.getName();
            Integer status = menuQueryRequest.getStatus();
            String icon = menuQueryRequest.getIcon();
            String compoent = menuQueryRequest.getCompoent();
            String permissionvalue = menuQueryRequest.getPermissionValue();
            String path = menuQueryRequest.getPath();
            QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
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

