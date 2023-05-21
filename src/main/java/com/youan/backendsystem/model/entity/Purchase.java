package com.youan.backendsystem.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: zz
 * @CreateTime: 2023-05-20  10:53
 * @Description: TODO
 * @Version: 1.0
 */
@TableName(value = "purchase")
@Data
public class Purchase implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

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

    /**
     *是否删除
     */
    @TableLogic
    private Integer isDelete;

    /**
     *创建时间
     */
    private Date createTime;

    /**
     *更新时间
     */
    private Date updateTime;
}
