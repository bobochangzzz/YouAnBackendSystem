package com.youan.backendsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.youan.backendsystem.common.ErrorCode;
import com.youan.backendsystem.exception.BusinessException;
import com.youan.backendsystem.mapper.MenuMapper;
import com.youan.backendsystem.model.dto.menu.MenuQueryRequest;
import com.youan.backendsystem.model.entity.Menu;
import com.youan.backendsystem.model.vo.MenuVo;
import com.youan.backendsystem.service.MenuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu>
        implements MenuService {

    @Override
    public List<MenuVo> listMenu() {
        //查询所有菜单
        List<MenuVo> menus = baseMapper.selectAll();
        //返回的菜单树
        List<MenuVo> rootMenus = new ArrayList<>();
        for (MenuVo menu : menus) {
            //pid（上级Id）为0的是根菜单
            if (menu.getPid() == 0L) {
                rootMenus.add(menu);
            }
        }
        //遍历，找到二级菜单（根菜单的id和所有菜单中的pid比较）
        for (MenuVo rootMenu : rootMenus) {
            List<MenuVo> child = getChild(rootMenu.getId(), menus);
            rootMenu.setSubMenuList(child);
        }
        return rootMenus;

    }

    /**
     * 递归获取下级菜单
     *
     * @param pid   上级Id
     * @param menus 所有菜单
     * @return
     */
    public List<MenuVo> getChild(Long pid, List<MenuVo> menus) {
        //子菜单列表
        List<MenuVo> childList = new ArrayList<>();
        for (MenuVo menu : menus) {
            if (pid == menu.getPid()){
                childList.add(menu);
            }
        }
        //遍历 获取子菜单的子菜单
        for (MenuVo menu : childList) {
            List<MenuVo> child = getChild(menu.getId(), menus);
            menu.setSubMenuList(child);
        }
        //递归出口  childList长度为0
        if (childList.size() == 0) {
            return new ArrayList<>();
        }
        return childList;
    }


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
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(pid != null, "pid", pid);
        queryWrapper.eq(type != null, "type", type);
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
        if (count(menuQueryWrapper) == 0L) {
            // 当前菜单为下级菜单 直接删除
            return removeById(id);
        }else{
            // 当前菜单为顶级菜单 需要先删除子菜单
            List<Long> ids = list(menuQueryWrapper).stream().map(Menu::getId).collect(Collectors.toList());
            return removeByIds(ids) && removeById(id);
        }

    }
}

