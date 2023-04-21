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
import com.youan.backendsystem.model.dto.department.DepartmentAddRequest;
import com.youan.backendsystem.model.dto.department.DepartmentQueryRequest;
import com.youan.backendsystem.model.dto.department.DepartmentUpdateRequest;
import com.youan.backendsystem.model.entity.Department;
import com.youan.backendsystem.service.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户接口
 *
 * @author bobochang
 * @from <a href="https://blog.bobochang.work">bobochang's BLOG</a>
 */
@RestController
@RequestMapping("/department")
@Slf4j
public class DepartmentController {

    @Resource
    private DepartmentService departmentService;


    // region 增删改查

    /**
     * 创建部门
     *
     * @param departmentAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addDepartment(@RequestBody DepartmentAddRequest departmentAddRequest, HttpServletRequest request) {
        if (departmentAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Department department = new Department();
        BeanUtils.copyProperties(departmentAddRequest, department);
        boolean result = departmentService.save(department);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(department.getId());
    }

    /**
     * 删除部门
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteDepartment(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = departmentService.removeById(deleteRequest.getId());
        return ResultUtils.success(b);
    }

    /**
     * 更新部门
     *
     * @param departmentUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateDepartment(@RequestBody DepartmentUpdateRequest departmentUpdateRequest,
                                            HttpServletRequest request) {
        if (departmentUpdateRequest == null || departmentUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Department department = new Department();
        BeanUtils.copyProperties(departmentUpdateRequest, department);
        boolean result = departmentService.updateById(department);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }


    /**
     * 分页获取部门列表（仅管理员）
     *
     * @param departmentQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Department>> listDepartmentByPage(@RequestBody DepartmentQueryRequest departmentQueryRequest,
                                                         HttpServletRequest request) {
        long current = departmentQueryRequest.getCurrent();
        long size = departmentQueryRequest.getPageSize();
        Page<Department> userPage = departmentService.page(new Page<>(current, size),
                departmentService.getQueryWrapper(departmentQueryRequest));
        return ResultUtils.success(userPage);
    }

    // todo 所有部门展示（子列表方式全部展示）

    // endregion

}
