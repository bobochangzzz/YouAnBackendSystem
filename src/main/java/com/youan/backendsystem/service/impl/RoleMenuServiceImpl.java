package com.youan.backendsystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.youan.backendsystem.mapper.RoleMenuMapper;
import com.youan.backendsystem.model.entity.RoleMenu;
import com.youan.backendsystem.service.RoleMenuService;
import org.springframework.stereotype.Service;

@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu>
        implements RoleMenuService {
}
