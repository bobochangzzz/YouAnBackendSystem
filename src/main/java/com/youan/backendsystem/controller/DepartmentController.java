package com.youan.backendsystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import com.youan.backendsystem.model.entity.User;
import com.youan.backendsystem.model.vo.DepartmentVO;
import com.youan.backendsystem.service.DepartmentService;
import com.youan.backendsystem.service.UserService;
import com.youan.backendsystem.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
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

    @Resource
    @Lazy
    private UserService userService;


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
        department = setDepartmentInfo(departmentAddRequest);
        boolean result = departmentService.save(department);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(department.getId());
    }

    private Department setDepartmentInfo(DepartmentAddRequest departmentAddRequest) {
        Department department = new Department();
        BeanUtils.copyProperties(departmentAddRequest, department);
        // 根据上级部门名称查询上级部门id
        Department oldDepartment = departmentService.getOne(new QueryWrapper<Department>()
                .eq(StringUtils.isNotBlank(departmentAddRequest.getParDepartmentName()), "departmentName", departmentAddRequest.getParDepartmentName()));
        Long parentId = oldDepartment.getId();
        // 根据负责人名称查询负责人id
        User user = userService.getOne(new QueryWrapper<User>()
                .eq(StringUtils.isNotBlank(departmentAddRequest.getDepartmentHeadName()), "userName", departmentAddRequest.getDepartmentHeadName()));
        Long userId = user.getId();
        department.setDepartmentHeadId(userId);
        department.setParentId(parentId);
        return department;
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
        boolean b = departmentService.removeDepartment(deleteRequest.getId());
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

    @PostMapping("/list/vo/page")
    public BaseResponse<Page<DepartmentVO>> listDepartmentVOByPage(@RequestBody DepartmentQueryRequest departmentQueryRequest,
                                                                   HttpServletRequest request) {
        long current = departmentQueryRequest.getCurrent();
        long size = departmentQueryRequest.getPageSize();
        Page<Department> departmentPage = departmentService.page(new Page<>(current, size),
                departmentService.getQueryWrapper(departmentQueryRequest));
        Page<DepartmentVO> userPage = new Page<>(current, size, departmentPage.getTotal());
        List<DepartmentVO> departmentVO = departmentService.getDepartmentVO(departmentPage.getRecords());
        userPage.setRecords(departmentVO);
        return ResultUtils.success(userPage);
    }

    // todo 所有部门展示（子列表方式全部展示）

    // endregion

}
