package com.youan.backendsystem.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.youan.backendsystem.model.dto.purchase.PurchaseQueryRequest;
import com.youan.backendsystem.model.entity.Purchase;

public interface PurchaseService extends IService<Purchase> {

    // 获取查询条件
    QueryWrapper<Purchase> getQueryWrapper(PurchaseQueryRequest purchaseQueryRequest);

    //审核通过
    boolean getStatusPurchaseById(long purchaseId);

    //审核不通过
    boolean getNoStatusPurchaseById(long purchaseId);
}
