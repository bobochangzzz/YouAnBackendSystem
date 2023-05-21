package com.youan.backendsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.youan.backendsystem.common.ErrorCode;
import com.youan.backendsystem.exception.BusinessException;
import com.youan.backendsystem.mapper.PurchaseMapper;
import com.youan.backendsystem.model.dto.purchase.PurchaseQueryRequest;
import com.youan.backendsystem.model.entity.Contract;
import com.youan.backendsystem.model.entity.Purchase;
import com.youan.backendsystem.service.ContractService;
import com.youan.backendsystem.service.PurchaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: zz
 * @CreateTime: 2023-05-20  10:58
 * @Description: TODO
 * @Version: 1.0
 */
@Service
public class PurchaseServiceImpl extends ServiceImpl<PurchaseMapper, Purchase>
        implements PurchaseService {


    //把status改为2，审核不通过
    @Override
    public boolean getNoStatusPurchaseById(long purchaseId) {
        Purchase purchase = baseMapper.selectById(purchaseId);
        purchase.setStatus(2);
        baseMapper.updateById(purchase);
        return true;
    }

    //把status改为1，审核通过
    @Override
    public boolean getStatusPurchaseById(long purchaseId) {
        Purchase purchase = baseMapper.selectById(purchaseId);
        purchase.setStatus(1);
        baseMapper.updateById(purchase);
        return true;
    }

    @Override
    public QueryWrapper<Purchase> getQueryWrapper(PurchaseQueryRequest purchaseQueryRequest) {
        if (purchaseQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long contractId = purchaseQueryRequest.getContractId();
        String materials = purchaseQueryRequest.getMaterials();
        String provider = purchaseQueryRequest.getProvider();
        String applicant = purchaseQueryRequest.getApplicant();
        String remark = purchaseQueryRequest.getRemark();
        QueryWrapper<Purchase> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(contractId != 0L, "contractId", contractId);
        queryWrapper.eq(StringUtils.isNotBlank(materials), "materials", materials);
        queryWrapper.eq(StringUtils.isNotBlank(provider), "provider", provider);
        queryWrapper.eq(StringUtils.isNotBlank(applicant), "applicant", applicant);
        queryWrapper.eq(StringUtils.isNotBlank(remark), "remark", remark);
        return queryWrapper;
    }

}
