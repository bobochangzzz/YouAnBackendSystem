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
import com.youan.backendsystem.model.dto.menu.MenuAddRequest;
import com.youan.backendsystem.model.dto.menu.MenuQueryRequest;
import com.youan.backendsystem.model.dto.menu.MenuUpdateRequest;
import com.youan.backendsystem.model.entity.Menu;
import com.youan.backendsystem.service.MenuService;
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
@RequestMapping("/menu")
@Slf4j
public class MenuController {

    @Resource
    private MenuService menuService;

    /**
     * 分页获取菜单列表
     *
     * @param MenuQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Menu>> listMenuByPage(@RequestBody MenuQueryRequest menuQueryRequest,
                                                   HttpServletRequest request) {
        long current = menuQueryRequest.getCurrent();
        long size = menuQueryRequest.getPageSize();
        Page<Menu> menuPage = menuService.page(new Page<>(current, size),
                menuService.getQueryWrapper(menuQueryRequest));
        return ResultUtils.success(menuPage);
    }

    /**
     * 添加菜单
     *
     * @param menuAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addMenu(@RequestBody MenuAddRequest menuAddRequest, HttpServletRequest request) {
        if (menuAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Menu menu = new Menu();
        BeanUtils.copyProperties(menuAddRequest, menu);
        boolean result = menuService.save(menu);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(menu.getId());
    }

    /**
     * 根据菜单id更新菜单信息
     *
     * @param menuUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateMenu(@RequestBody MenuUpdateRequest menuUpdateRequest,
                                            HttpServletRequest request) {
        if (menuUpdateRequest == null || menuUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Menu menu = new Menu();
        BeanUtils.copyProperties(menuUpdateRequest, menu);
        boolean result = menuService.updateById(menu);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 删除菜单信息
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteMenu(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = menuService.removeMenu(deleteRequest.getId());
        return ResultUtils.success(b);
    }
}
