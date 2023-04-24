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
import com.youan.backendsystem.model.dto.role.*;
import com.youan.backendsystem.model.entity.Role;
import com.youan.backendsystem.model.entity.User;
import com.youan.backendsystem.model.enums.UserRoleEnum;
import com.youan.backendsystem.model.vo.CurrentRoleMenuVO;
import com.youan.backendsystem.model.vo.CurrentRoleVO;
import com.youan.backendsystem.model.vo.CurrentUserVO;
import com.youan.backendsystem.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 2023/4/20 - 15:03
 *
 * @author bobochang
 * @description 角色管理模块
 */
@RestController
@RequestMapping("/role")
@Slf4j
public class RoleController {

    @Resource
    private RoleService roleService;

    /**
     * 分页获取角色列表
     *
     * @param roleQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Role>> listRoleByPage(@RequestBody RoleQueryRequest roleQueryRequest,
                                                   HttpServletRequest request) {
        long current = roleQueryRequest.getCurrent();
        long size = roleQueryRequest.getPageSize();
        Page<Role> rolePage = roleService.page(new Page<>(current, size),
                roleService.getQueryWrapper(roleQueryRequest));
        return ResultUtils.success(rolePage);
    }

    /**
     * 添加角色
     *
     * @param roleAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addRole(@RequestBody RoleAddRequest roleAddRequest, HttpServletRequest request) {
        if (roleAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Role role = new Role();
        BeanUtils.copyProperties(roleAddRequest, role);
        boolean result = roleService.save(role);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(role.getId());
    }

    /**
     * 根据 角色id 获取对应信息
     *
     * @param roleId
     * @param request
     * @return
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<CurrentRoleVO> getCurrentRole(long roleId, HttpServletRequest request) {
        if (roleId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Role role = roleService.getById(roleId);
        CurrentRoleVO currentRoleVO = new CurrentRoleVO();
        BeanUtils.copyProperties(role, currentRoleVO);
        currentRoleVO.setRoleIdentification(UserRoleEnum.getValueByText(role.getRoleName()));
        ThrowUtils.throwIf(false, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(currentRoleVO);
    }

    /**
     * 根据角色id更新角色信息
     *
     * @param roleUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateRole(@RequestBody RoleUpdateRequest roleUpdateRequest,
                                            HttpServletRequest request) {
        if (roleUpdateRequest == null || roleUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Role role = new Role();
        BeanUtils.copyProperties(roleUpdateRequest, role);
        boolean result = roleService.updateById(role);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 删除角色信息
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteRole(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = roleService.removeById(deleteRequest.getId());
        return ResultUtils.success(b);
    }

    /**
     * 指定用户分配角色
     *
     * @param assignRoleRequest
     * @param request
     * @return
     */
    @PostMapping("/assign/role")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> assignRole(@RequestBody AssignRoleRequest assignRoleRequest,
                                            HttpServletRequest request) {
        if (assignRoleRequest == null || assignRoleRequest.getUserId() <= 0 || assignRoleRequest.getUserRoleEnumText() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long userId = assignRoleRequest.getUserId();
        String userRoleEnumText = assignRoleRequest.getUserRoleEnumText();
        boolean result = roleService.assignRole(userId, userRoleEnumText);
        return ResultUtils.success(result);
    }

    /**
     * 指定角色分配菜单权限
     *
     * @param assignMenuRequest
     * @param request
     * @return
     */
    @PostMapping("/assign/menu")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> assignMenu(@RequestBody AssignMenuRequest assignMenuRequest,
                                            HttpServletRequest request) {
        if (assignMenuRequest == null || assignMenuRequest.getRoleId() <= 0 || assignMenuRequest.getMenuIds() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long roleId = assignMenuRequest.getRoleId();
        List<Long> menuIds = assignMenuRequest.getMenuIds();
        boolean result = roleService.assignMenu(roleId, menuIds);
        return ResultUtils.success(result);
    }

    @GetMapping("/get/menuPermission")
    public BaseResponse<CurrentRoleMenuVO> getCurrentRoleMenu(long roleId, HttpServletRequest request) {
        if (roleId < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        CurrentRoleMenuVO currentRoleMenuVO = roleService.getMenuPermission(roleId);
        return ResultUtils.success(currentRoleMenuVO);
    }
}
