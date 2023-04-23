package com.youan.backendsystem.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.youan.backendsystem.model.dto.menu.MenuQueryRequest;
import com.youan.backendsystem.model.entity.Menu;


public interface MenuService extends IService<Menu> {

    // 获取查询条件
    QueryWrapper<Menu> getQueryWrapper(MenuQueryRequest menuQueryRequest);


    // 删除菜单
    boolean removeMenu(Long id);
}
