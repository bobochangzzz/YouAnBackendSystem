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
import com.youan.backendsystem.model.dto.purchase.PurchaseAddRequest;
import com.youan.backendsystem.model.dto.purchase.PurchaseQueryRequest;
import com.youan.backendsystem.model.dto.purchase.PurchaseUpdateRequest;
import com.youan.backendsystem.model.entity.Purchase;
import com.youan.backendsystem.model.vo.CurrentPurchaseVO;
import com.youan.backendsystem.service.PurchaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author: zz
 * @CreateTime: 2023-05-20  10:46
 * @Description: 材料资金申请接口
 * @Version: 1.0
 */
@RestController
@RequestMapping("/purchase")
@Slf4j
public class PurchaseController {

    @Resource
    private PurchaseService purchaseService;

    /**
     * 分页获取材料资金申请列表
     *
     * @param purchaseQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Purchase>> listContractByPage(@RequestBody PurchaseQueryRequest purchaseQueryRequest,
                                                           HttpServletRequest request) {
        long current = purchaseQueryRequest.getCurrent();
        long size = purchaseQueryRequest.getPageSize();
        Page<Purchase> purchasePage = purchaseService.page(new Page<>(current, size),
                purchaseService.getQueryWrapper(purchaseQueryRequest));
        return ResultUtils.success(purchasePage);
    }

    /**
     * 添加材料资金申请
     *
     * @param purchaseAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addPurchase(@RequestBody PurchaseAddRequest purchaseAddRequest, HttpServletRequest request) {
        if (purchaseAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Purchase purchase = new Purchase();
        BeanUtils.copyProperties(purchaseAddRequest, purchase);
        boolean result = purchaseService.save(purchase);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(purchase.getId());
    }

    /**
     * 根据 id 获取材料资料申请对应信息
     *
     * @param purchaseId
     * @param request
     * @return
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<CurrentPurchaseVO> getCurrentPurchase(long purchaseId, HttpServletRequest request) {
        if (purchaseId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Purchase purchase = purchaseService.getById(purchaseId);
        CurrentPurchaseVO currentPurchaseVO = new CurrentPurchaseVO();
        BeanUtils.copyProperties(purchase, currentPurchaseVO);
        ThrowUtils.throwIf(false, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(currentPurchaseVO);
    }

    /**
     * 根据id更新材料资金申请
     *
     * @param purchaseUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updatePurchase(@RequestBody PurchaseUpdateRequest purchaseUpdateRequest,
                                                HttpServletRequest request) {
        if (purchaseUpdateRequest == null || purchaseUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Purchase purchase = new Purchase();
        BeanUtils.copyProperties(purchaseUpdateRequest, purchase);
        boolean result = purchaseService.updateById(purchase);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 删除材料资金申请
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteContract(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = purchaseService.removeById(deleteRequest.getId());
        return ResultUtils.success(b);
    }




    /**
     * 通过审批
     *
     * @param purchaseId
     * @param request
     * @return
     */
    @PostMapping("/getStatusPurchaseById")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> getStatusPurchaseById(long purchaseId, HttpServletRequest request) {
        if (purchaseId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = purchaseService.getStatusPurchaseById(purchaseId);
        return ResultUtils.success(b);
    }

    /**
     * 不通过审批
     *
     * @param purchaseId
     * @param request
     * @return
     */
    @PostMapping("/getNoStatusPurchaseById")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> getNoStatusPurchaseById(long purchaseId, HttpServletRequest request) {
        if (purchaseId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = purchaseService.getNoStatusPurchaseById(purchaseId);
        return ResultUtils.success(b);
    }



}
