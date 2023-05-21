package com.youan.backendsystem.model.dto.contract;

import com.youan.backendsystem.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Author: zz
 * @CreateTime: 2023-05-20  09:45
 * @Description: TODO
 * @Version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ContractQueryRequest extends PageRequest implements Serializable {
    /**
     * 工程名称
     */
    private String projectName;


    /**
     * 甲方
     */
    private String firstParty;

    /**
     * 备注
     */
    private String remark;
}
