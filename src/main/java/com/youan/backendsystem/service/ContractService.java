package com.youan.backendsystem.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.youan.backendsystem.model.dto.contract.ContractQueryRequest;
import com.youan.backendsystem.model.entity.Contract;


public interface ContractService extends IService<Contract> {

    // 获取查询条件
    QueryWrapper<Contract> getQueryWrapper(ContractQueryRequest contractQueryRequest);

}
