package com.youan.backendsystem.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.youan.backendsystem.common.ErrorCode;
import com.youan.backendsystem.exception.BusinessException;
import com.youan.backendsystem.listener.EmployeeListener;
import com.youan.backendsystem.mapper.EmployeeMapper;
import com.youan.backendsystem.model.dto.employee.EmployeeQueryRequest;
import com.youan.backendsystem.model.entity.Employee;
import com.youan.backendsystem.model.vo.EmployeeExcelVO;
import com.youan.backendsystem.service.DepartmentService;
import com.youan.backendsystem.service.EmployeeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: zz
 * @CreateTime: 2023-04-23  15:28
 * @Description: TODO
 * @Version: 1.0
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee>
        implements EmployeeService {

    @Resource
    private DepartmentService departmentService;

    @Override
    public boolean exportEmployeeData(HttpServletResponse response) {
        // 设置下载信息
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = "employee";
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        // 查询数据库
        List<EmployeeExcelVO> employeeExcelVOList = list().stream()
                // todo 性能优化：减少数据库查询次数 批量先查询出所有部门id 再获取部门名称 将信息以key-value存入Set中
                .map(employee -> {
                    // 将employee数据复制到employExcelVO中
                    EmployeeExcelVO employeeExcelVO = new EmployeeExcelVO();
                    BeanUtils.copyProperties(employee, employeeExcelVO);
                    // 根据部门id获取部门名称
                    employeeExcelVO.setDepartmentName(departmentService.getById(employee.getDepartmentId()).getDepartmentName());
                    return employeeExcelVO;
                })
                .collect(Collectors.toList());
        // 调用EasyExcel方法进行写操作
        try {
            EasyExcel.write(response.getOutputStream(), EmployeeExcelVO.class).sheet("员工数据").doWrite(employeeExcelVOList);
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return true;
    }


    @Override
    public boolean importEmployeeData(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(), EmployeeExcelVO.class, new EmployeeListener(baseMapper, departmentService)).sheet().doRead();
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return false;
    }

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
