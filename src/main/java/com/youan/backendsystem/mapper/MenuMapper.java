package com.youan.backendsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.youan.backendsystem.model.entity.Menu;
import com.youan.backendsystem.model.vo.MenuVo;

import java.util.List;


public interface MenuMapper extends BaseMapper<Menu> {
    List<MenuVo> selectAll();
}
