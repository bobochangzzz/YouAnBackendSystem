package com.youan.backendsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.youan.backendsystem.common.ErrorCode;
import com.youan.backendsystem.exception.BusinessException;
import com.youan.backendsystem.model.dto.department.DepartmentQueryRequest;
import com.youan.backendsystem.model.entity.Department;
import com.youan.backendsystem.model.entity.Role;
import com.youan.backendsystem.model.entity.UserDepartment;
import com.youan.backendsystem.service.DepartmentService;
import com.youan.backendsystem.mapper.DepartmentMapper;
import com.youan.backendsystem.service.UserDepartmentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author bobochang
 * @description 针对表【department(部门)】的数据库操作Service实现
 * @createDate 2023-04-21 10:18:07
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department>
        implements DepartmentService {

    @Resource
    private UserDepartmentService userDepartmentService;

    @Override
    public QueryWrapper<Department> getQueryWrapper(DepartmentQueryRequest departmentQueryRequest) {

        if (departmentQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        String departmentName = departmentQueryRequest.getDepartmentName();
        Long departmentHeadId = departmentQueryRequest.getDepartmentHeadId();
        Integer status = departmentQueryRequest.getStatus();
        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(departmentName), "departmentName", departmentName);
        queryWrapper.eq(departmentHeadId != 0L, "departmentHeadId", departmentHeadId);
        queryWrapper.eq(status != null, "status", status);
        return queryWrapper;
    }

    @Override
    @Transactional
    public boolean removeDepartment(Long id) {
        // 判断当前需要删除的部门是上级部门 or 下级部门
        QueryWrapper<Department> departmentQueryWrapper = new QueryWrapper<>();
        departmentQueryWrapper.eq("parentId", id);
        if (count(departmentQueryWrapper) == 0L) {
            // 需要删除的部门无下级部门
            return removeById(id);
        }
        // 需要删除的部门是上级部门 1.先删除下级部门 2.再删除上级部门
        // 查询出所有下级部门并获取这些list中的id
        List<Long> ids = list(departmentQueryWrapper).stream().map(Department::getId).collect(Collectors.toList());
        // 根据list[ids] 删除所有符合条件的下级部门
        return removeByIds(ids) && removeById(id);
    }
}




