package com.youan.backendsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.youan.backendsystem.common.ErrorCode;
import com.youan.backendsystem.exception.BusinessException;
import com.youan.backendsystem.model.dto.department.DepartmentQueryRequest;
import com.youan.backendsystem.model.entity.Department;
import com.youan.backendsystem.model.entity.Role;
import com.youan.backendsystem.service.DepartmentService;
import com.youan.backendsystem.mapper.DepartmentMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author bobochang
 * @description 针对表【department(部门)】的数据库操作Service实现
 * @createDate 2023-04-21 10:18:07
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department>
        implements DepartmentService {

    @Override
    public QueryWrapper<Department> getQueryWrapper(DepartmentQueryRequest departmentQueryRequest) {

        if (departmentQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        String departmentName = departmentQueryRequest.getDepartmentName();
        String departmentHead = departmentQueryRequest.getDepartmentHead();
        Long status = departmentQueryRequest.getStatus();
        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(departmentName), "departmentName", departmentName);
        queryWrapper.eq(StringUtils.isNotBlank(departmentHead), "departmentHead", departmentHead);
        queryWrapper.eq(status != null, "status", status);
        return queryWrapper;
    }
}




