package com.youan.backendsystem.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.youan.backendsystem.model.dto.department.DepartmentQueryRequest;
import com.youan.backendsystem.model.entity.Department;
import com.baomidou.mybatisplus.extension.service.IService;
import com.youan.backendsystem.model.entity.Role;
import com.youan.backendsystem.model.vo.DepartmentVO;

import java.util.List;

/**
 * @author bobochang
 * @description 针对表【department(部门)】的数据库操作Service
 * @createDate 2023-04-21 10:18:07
 */
public interface DepartmentService extends IService<Department> {

    // 获取查询条件
    QueryWrapper<Department> getQueryWrapper(DepartmentQueryRequest departmentQueryRequest);

    // 删除部门信息
    boolean removeDepartment(Long id);

    // 整理返回前端的信息分页
    List<DepartmentVO> getDepartmentVO(List<Department> departmentPage);
}
