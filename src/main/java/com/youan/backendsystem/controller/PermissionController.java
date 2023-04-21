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
import com.youan.backendsystem.model.dto.perimission.PermissionAddRequest;
import com.youan.backendsystem.model.dto.perimission.PermissionQueryRequest;
import com.youan.backendsystem.model.dto.perimission.PermissionUpdateRequest;
import com.youan.backendsystem.model.entity.Permission;
import com.youan.backendsystem.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 2023/4/21 - 10:47
 *
 * @author bobochang
 * @description 菜单管理模块
 */
@RestController
@RequestMapping("/permission")
@Slf4j
public class PermissionController {

    @Resource
    private PermissionService permissionService;

    /**
     * 分页获取菜单列表
     *
     * @param PermissionQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Permission>> listPermissionByPage(@RequestBody PermissionQueryRequest permissionQueryRequest,
                                                               HttpServletRequest request) {
        long current = permissionQueryRequest.getCurrent();
        long size = permissionQueryRequest.getPageSize();
        Page<Permission> permissionPage = permissionService.page(new Page<>(current, size),
                permissionService.getQueryWrapper(permissionQueryRequest));
        return ResultUtils.success(permissionPage);
    }

    /**
     * 添加菜单
     *
     * @param permissionAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addPermission(@RequestBody PermissionAddRequest permissionAddRequest, HttpServletRequest request) {
        if (permissionAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Permission permission = new Permission();
        BeanUtils.copyProperties(permissionAddRequest, permission);
        boolean result = permissionService.save(permission);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(permission.getId());
    }

    /**
     * 根据菜单id更新菜单信息
     *
     * @param permissionUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updatePermission(@RequestBody PermissionUpdateRequest permissionUpdateRequest,
                                            HttpServletRequest request) {
        if (permissionUpdateRequest == null || permissionUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Permission permission = new Permission();
        BeanUtils.copyProperties(permissionUpdateRequest, permission);
        boolean result = permissionService.updateById(permission);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 删除菜单信息
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deletePermission(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = permissionService.removeById(deleteRequest.getId());
        return ResultUtils.success(b);
    }
}
