package com.youan.backendsystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.youan.backendsystem.model.entity.UserDepartment;
import com.youan.backendsystem.service.UserDepartmentService;
import com.youan.backendsystem.mapper.UserDepartmentMapper;
import org.springframework.stereotype.Service;

/**
* @author bobochang
* @description 针对表【user_department(部门)】的数据库操作Service实现
* @createDate 2023-04-21 10:18:29
*/
@Service
public class UserDepartmentServiceImpl extends ServiceImpl<UserDepartmentMapper, UserDepartment>
    implements UserDepartmentService{

}




