package com.youan.backendsystem.model.dto.purchase;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: zz
 * @CreateTime: 2023-05-20  11:11
 * @Description: TODO
 * @Version: 1.0
 */
@Data
public class PurchaseAddRequest implements Serializable {

    /**
     * 所属合同id
     */
    private Long contractId;

    /**
     * 原材料
     */
    private String materials;

    /**
     * 数量
     */
    private String quantity;

    /**
     * 价格
     */
    private String price;

    /**
     * 供应商
     */
    private String provider;

    /**
     * 申请人
     */
    private String applicant;

    /**
     * 申请时间
     */
    private Date applicantTime;

    /**
     * 审核状态码
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

}
