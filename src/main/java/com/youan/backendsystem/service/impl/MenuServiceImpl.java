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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu>
        implements MenuService {

    @Override
    public QueryWrapper<Menu> getQueryWrapper(MenuQueryRequest menuQueryRequest) {
        if (menuQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = menuQueryRequest.getId();
        Long pid = menuQueryRequest.getPid();
        Long type = menuQueryRequest.getType();
        String name = menuQueryRequest.getName();
        Integer status = menuQueryRequest.getStatus();
        String icon = menuQueryRequest.getIcon();
        String component = menuQueryRequest.getComponent();
        String permissionValue = menuQueryRequest.getPermissionValue();
        String path = menuQueryRequest.getPath();
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.eq(status != null, "status", status);
        queryWrapper.eq(id != 0L, "id", id);
        queryWrapper.eq(pid != 0L, "pid", pid);
        queryWrapper.eq(type != 0L, "type", type);
        queryWrapper.eq(StringUtils.isNotBlank(icon), "icon", icon);
        queryWrapper.eq(StringUtils.isNotBlank(component), "component", component);
        queryWrapper.eq(StringUtils.isNotBlank(permissionValue), "permissionValue", permissionValue);
        queryWrapper.eq(StringUtils.isNotBlank(path), "path", path);
        return queryWrapper;
    }

    @Override
    @Transactional
    public boolean removeMenu(Long id) {
        // 判断当前菜单是否为顶级菜单
        QueryWrapper<Menu> menuQueryWrapper = new QueryWrapper<>();
        menuQueryWrapper.eq("pid", id);
        if (count(menuQueryWrapper) != 0L) {
            // 当前菜单为下级菜单 直接删除
            return removeById(id);
        }
        // 当前菜单为顶级菜单 需要先删除子菜单
        List<Long> ids = list(menuQueryWrapper).stream().map(Menu::getId).collect(Collectors.toList());
        return removeByIds(ids) && removeById(id);
    }
}

