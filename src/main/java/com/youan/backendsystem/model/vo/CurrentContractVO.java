package com.youan.backendsystem.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: zz
 * @CreateTime: 2023-05-20  10:17
 * @Description: TODO
 * @Version: 1.0
 */
@Data
public class CurrentContractVO implements Serializable {
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
