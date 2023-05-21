package com.youan.backendsystem.model.dto.purchase;

import com.youan.backendsystem.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Author: zz
 * @CreateTime: 2023-05-20  11:07
 * @Description: TODO
 * @Version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PurchaseQueryRequest extends PageRequest implements Serializable {


    /**
     * 所属合同id
     */
    private Long contractId;


    /**
     * 原材料
     */
    private String materials;


    /**
     * 供应商
     */
    private String provider;

    /**
     * 申请人
     */
    private String applicant;

    /**
     * 备注
     */
    private String remark;




}
