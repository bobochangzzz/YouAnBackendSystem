package com.youan.backendsystem.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: zz
 * @CreateTime: 2023-05-20  09:29
 * @Description: TODO
 * @Version: 1.0
 */
@TableName(value = "contract")
@Data
public class Contract implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     *id
     */
    @TableId(type = IdType.AUTO)
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
