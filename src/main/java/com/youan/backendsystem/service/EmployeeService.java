package com.youan.backendsystem.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.youan.backendsystem.model.dto.employee.EmployeeQueryRequest;
import com.youan.backendsystem.model.entity.Employee;


public interface EmployeeService extends IService<Employee> {
    // 获取查询条件
    QueryWrapper<Employee> getQueryWrapper(EmployeeQueryRequest employeeQueryRequest);
}
