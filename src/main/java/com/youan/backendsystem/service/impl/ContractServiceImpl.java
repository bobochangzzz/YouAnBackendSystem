package com.youan.backendsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.youan.backendsystem.common.ErrorCode;
import com.youan.backendsystem.exception.BusinessException;
import com.youan.backendsystem.mapper.ContractMapper;
import com.youan.backendsystem.model.dto.contract.ContractQueryRequest;
import com.youan.backendsystem.model.entity.Contract;
import com.youan.backendsystem.model.entity.Role;
import com.youan.backendsystem.service.ContractService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @Author: zz
 * @CreateTime: 2023-05-20  09:37
 * @Description: TODO
 * @Version: 1.0
 */
@Service
public class ContractServiceImpl extends ServiceImpl<ContractMapper, Contract>
        implements ContractService {

    @Override
    public QueryWrapper<Contract> getQueryWrapper(ContractQueryRequest contractQueryRequest) {
        if (contractQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        String projectName = contractQueryRequest.getProjectName();
        String firstParty = contractQueryRequest.getFirstParty();
        String remark = contractQueryRequest.getRemark();
        QueryWrapper<Contract> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(projectName), "projectName", projectName);
        queryWrapper.eq(StringUtils.isNotBlank(firstParty), "firstParty", firstParty);
        queryWrapper.eq(StringUtils.isNotBlank(remark), "remark", remark);
        return queryWrapper;
    }
}
