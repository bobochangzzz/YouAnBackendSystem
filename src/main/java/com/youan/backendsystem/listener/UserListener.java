package com.youan.backendsystem.listener;

import cn.hutool.core.lang.Dict;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.youan.backendsystem.mapper.UserMapper;
import com.youan.backendsystem.model.entity.Department;
import com.youan.backendsystem.model.entity.User;
import com.youan.backendsystem.model.vo.UserExcelVO;
import com.youan.backendsystem.service.DepartmentService;
import org.springframework.beans.BeanUtils;

import javax.annotation.Resource;

/**
 * 2023/4/24 - 14:53
 *
 * @author bobochang
 * @description
 */
public class UserListener extends AnalysisEventListener<UserExcelVO> {


    private UserMapper userMapper;

    @Resource
    private DepartmentService departmentService;

    public UserListener(UserMapper userMapper, DepartmentService departmentService) {
        this.userMapper = userMapper;
        this.departmentService = departmentService;
    }

    //一行一行读取
    @Override
    public void invoke(UserExcelVO userExcelVO, AnalysisContext analysisContext) {
        // 调用方法添加数据库
        User user = new User();
        BeanUtils.copyProperties(userExcelVO, user);
        // 根据部门名称查询部门id
        QueryWrapper<Department> departmentQueryWrapper = new QueryWrapper<>();
        departmentQueryWrapper.eq(userExcelVO.getDepartmentName() != null, "departmentName", userExcelVO.getDepartmentName());
        user.setDepartmentId(departmentService.getOne(departmentQueryWrapper).getId());
        userMapper.insert(user);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
