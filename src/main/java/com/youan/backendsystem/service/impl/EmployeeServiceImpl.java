package com.youan.backendsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.youan.backendsystem.common.ErrorCode;
import com.youan.backendsystem.exception.BusinessException;
import com.youan.backendsystem.mapper.EmployeeMapper;
import com.youan.backendsystem.model.dto.employee.EmployeeQueryRequest;
import com.youan.backendsystem.model.entity.Employee;
import com.youan.backendsystem.service.EmployeeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @Author: zz
 * @CreateTime: 2023-04-23  15:28
 * @Description: TODO
 * @Version: 1.0
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee>
        implements EmployeeService {

    @Override
    public QueryWrapper<Employee> getQueryWrapper(EmployeeQueryRequest employeeQueryRequest) {
        if (employeeQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = employeeQueryRequest.getId();
        String employeeName = employeeQueryRequest.getName();
        Integer status = employeeQueryRequest.getStatus();
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != 0L, "id", id);
        queryWrapper.eq(StringUtils.isNotBlank(employeeName), "name", employeeName);
        queryWrapper.eq(status != null, "status", status);
        return queryWrapper;
    }
}
