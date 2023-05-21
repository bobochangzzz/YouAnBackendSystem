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
import com.youan.backendsystem.model.dto.contract.ContractAddRequest;
import com.youan.backendsystem.model.dto.contract.ContractQueryRequest;
import com.youan.backendsystem.model.dto.contract.ContractUpdateRequest;
import com.youan.backendsystem.model.entity.Contract;
import com.youan.backendsystem.model.vo.CurrentContractVO;
import com.youan.backendsystem.service.ContractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author: zz
 * @CreateTime: 2023-05-20  09:26
 * @Description: 合同管理接口
 * @Version: 1.0
 */
@RestController
@RequestMapping("/contract")
@Slf4j
public class ContractController {

    @Resource
    private ContractService contractService;

    /**
     * 分页获取合同列表
     *
     * @param contractQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Contract>> listContractByPage(@RequestBody ContractQueryRequest contractQueryRequest,
                                                       HttpServletRequest request) {
        long current = contractQueryRequest.getCurrent();
        long size = contractQueryRequest.getPageSize();
        Page<Contract> contractPage = contractService.page(new Page<>(current, size),
                contractService.getQueryWrapper(contractQueryRequest));
        return ResultUtils.success(contractPage);
    }

    /**
     * 添加合同
     *
     * @param contractAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addContract(@RequestBody ContractAddRequest contractAddRequest, HttpServletRequest request) {
        if (contractAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Contract contract = new Contract();
        BeanUtils.copyProperties(contractAddRequest, contract);
        boolean result = contractService.save(contract);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(contract.getId());
    }

    /**
     * 根据 合同id 获取对应信息
     *
     * @param contractId
     * @param request
     * @return
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<CurrentContractVO> getCurrentContract(long contractId, HttpServletRequest request) {
        if (contractId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Contract contract = contractService.getById(contractId);
        CurrentContractVO currentContractVO = new CurrentContractVO();
        BeanUtils.copyProperties(contract, currentContractVO);
        ThrowUtils.throwIf(false, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(currentContractVO);
    }

    /**
     * 根据合同id更新合同信息
     *
     * @param contractUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateContract(@RequestBody ContractUpdateRequest contractUpdateRequest,
                                            HttpServletRequest request) {
        if (contractUpdateRequest == null || contractUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Contract contract = new Contract();
        BeanUtils.copyProperties(contractUpdateRequest, contract);
        boolean result = contractService.updateById(contract);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 删除合同信息
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
        boolean b = contractService.removeById(deleteRequest.getId());
        return ResultUtils.success(b);
    }


}
