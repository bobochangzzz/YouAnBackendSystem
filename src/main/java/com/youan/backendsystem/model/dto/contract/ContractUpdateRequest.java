package com.youan.backendsystem.model.dto.contract;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: zz
 * @CreateTime: 2023-05-20  09:49
 * @Description: TODO
 * @Version: 1.0
 */
@Data
public class ContractUpdateRequest implements Serializable {
    /**
     * 合同id
     */
    private Long id;

    /**
     *甲方
     */
    private String firstParty;

    /**
     * 乙方
     */
    private String secondParty;

    /**
     * 工程名称
     */
    private String projectName;

    /**
     * 沥青品类
     */
    private String category;

    /**
     * 数量
     */
    private String quantity;

    /**
     * 付款方式
     */
    private String payMethod;

    /**
     * 颜色
     */
    private String colour;

    /**
     * 价格
     */
    private String price;

    /**
     * 备注
     */
    private String remark;
}
