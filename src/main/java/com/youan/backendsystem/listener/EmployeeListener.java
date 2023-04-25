package com.youan.backendsystem.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.youan.backendsystem.mapper.EmployeeMapper;
import com.youan.backendsystem.mapper.UserMapper;
import com.youan.backendsystem.model.entity.Department;
import com.youan.backendsystem.model.entity.Employee;
import com.youan.backendsystem.model.entity.User;
import com.youan.backendsystem.model.vo.EmployeeExcelVO;
import com.youan.backendsystem.service.DepartmentService;
import org.springframework.beans.BeanUtils;

import javax.annotation.Resource;


/**
 * @Author: zz
 * @CreateTime: 2023-04-25  10:06
 * @Description: TODO
 * @Version: 1.0
 */
public class EmployeeListener extends AnalysisEventListener<EmployeeExcelVO> {


    private EmployeeMapper employeeMapper;

    @Resource
    private DepartmentService departmentService;

    public EmployeeListener(EmployeeMapper employeeMapper, DepartmentService departmentService) {
        this.employeeMapper = employeeMapper;
        this.departmentService = departmentService;
    }

    @Override
    public void invoke(EmployeeExcelVO employeeExcelVO, AnalysisContext analysisContext) {
        // 调用方法添加数据库
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeExcelVO, employee);
        // 根据部门名称查询部门id
        QueryWrapper<Department> departmentQueryWrapper = new QueryWrapper<>();
        departmentQueryWrapper.eq(employeeExcelVO.getDepartmentName() != null, "departmentName", employeeExcelVO.getDepartmentName());
        employee.setDepartmentId(departmentService.getOne(departmentQueryWrapper).getId());

        employeeMapper.insert(employee);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
