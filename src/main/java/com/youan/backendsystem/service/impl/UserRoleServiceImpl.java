package com.youan.backendsystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.youan.backendsystem.model.entity.UserRole;
import com.youan.backendsystem.service.UserRoleService;
import com.youan.backendsystem.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;

/**
* @author bobochang
* @description 针对表【user_role】的数据库操作Service实现
* @createDate 2023-04-21 09:36:39
*/
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>
    implements UserRoleService{

}




