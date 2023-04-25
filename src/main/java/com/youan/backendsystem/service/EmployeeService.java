package com.youan.backendsystem.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.youan.backendsystem.model.dto.employee.EmployeeQueryRequest;
import com.youan.backendsystem.model.entity.Employee;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;


public interface EmployeeService extends IService<Employee> {
    // 获取查询条件
    QueryWrapper<Employee> getQueryWrapper(EmployeeQueryRequest employeeQueryRequest);

    /**
     * @description:导入员工数据
     * @author: zz
     * @date: 2023-04-25 9:55
     * @param: [file]
     * @return: boolean
     **/
    boolean importEmployeeData(MultipartFile file);

    /**
     * @description:导出员工数据
     * @author: zz
     * @date: 2023-04-25 14:27
     * @param: [response]
     * @return: boolean
     **/
    boolean exportEmployeeData(HttpServletResponse response);
}
