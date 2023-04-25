package com.youan.backendsystem.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.youan.backendsystem.annotation.AuthCheck;
import com.youan.backendsystem.common.BaseResponse;
import com.youan.backendsystem.common.DeleteRequest;
import com.youan.backendsystem.common.ErrorCode;
import com.youan.backendsystem.common.ResultUtils;
import com.youan.backendsystem.constant.UserConstant;
import com.youan.backendsystem.exception.BusinessException;
import com.youan.backendsystem.exception.ThrowUtils;
import com.youan.backendsystem.model.dto.employee.EmployeeAddRequest;
import com.youan.backendsystem.model.dto.employee.EmployeeQueryRequest;
import com.youan.backendsystem.model.dto.employee.EmployeeUpdateRequest;
import com.youan.backendsystem.model.entity.Employee;
import com.youan.backendsystem.model.vo.CurrentEmployeeVo;
import com.youan.backendsystem.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: zz
 * @CreateTime: 2023-04-23  15:24
 * @Description: 员工管理模块
 * @Version: 1.0
 */
@RestController
@RequestMapping("/employee")
@Slf4j
public class EmployeeController {

    @Resource
    private EmployeeService employeeService;

    /**
     * 分页获取员工列表
     *
     * @param employeeQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Employee>> listEmployeeByPage(@RequestBody EmployeeQueryRequest employeeQueryRequest,
                                                       HttpServletRequest request) {
        long current = employeeQueryRequest.getCurrent();
        long size = employeeQueryRequest.getPageSize();
        Page<Employee> employeePage = employeeService.page(new Page<>(current, size),
                employeeService.getQueryWrapper(employeeQueryRequest));
        return ResultUtils.success(employeePage);
    }


    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<CurrentEmployeeVo> getCurrentEmployee(long employeeId, HttpServletRequest request) {
        if (employeeId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Employee employee = employeeService.getById(employeeId);
        CurrentEmployeeVo currentEmployeeVo = new CurrentEmployeeVo();
        BeanUtils.copyProperties(employee, currentEmployeeVo);
        ThrowUtils.throwIf(false, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(currentEmployeeVo);
    }


    /**
     * 添加员工
     *
     * @param employeeAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addEmployee(@RequestBody EmployeeAddRequest employeeAddRequest, HttpServletRequest request) {
        if (employeeAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeAddRequest, employee);
        boolean result = employeeService.save(employee);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(employee.getId());
    }

    /**
     * 根据员工id更新员工信息
     *
     * @param employeeUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateRole(@RequestBody EmployeeUpdateRequest employeeUpdateRequest,
                                            HttpServletRequest request) {
        if (employeeUpdateRequest == null || employeeUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeUpdateRequest, employee);
        boolean result = employeeService.updateById(employee);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 删除员工信息
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteEmployee(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = employeeService.removeById(deleteRequest.getId());
        return ResultUtils.success(b);
    }

    /**
     * @description:导入员工数据
     * @author: zz
     * @date: 2023-04-25 9:53
     * @param: [file]
     * @return: com.youan.backendsystem.common.BaseResponse<java.lang.Boolean>
     **/
    @PostMapping("/import")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> importEmployeeData(MultipartFile file) {
        return ResultUtils.success(employeeService.importEmployeeData(file));
    }


    /**
     * @description:导出员工数据
     * @author: zz
     * @date: 2023-04-25 14:18
     * @param: [response]
     * @return: com.youan.backendsystem.common.BaseResponse<java.lang.Boolean>
     **/
    @GetMapping("/export")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> exportEmployeeData(HttpServletResponse response) {
        return ResultUtils.success(employeeService.exportEmployeeData(response));
    }


}
