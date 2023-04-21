package com.youan.backendsystem.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @TableName role
 */
@TableName(value = "role")
@Data
public class Role implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     *
     */
    private String roleName;

    /**
     * 0开启 1关闭
     */
    private Integer status;

    /**
     *
     */
    private String remark;

    /**
     *
     */
    @TableLogic
    private Integer isDelete;

    /**
     *
     */
    private Date createTime;

    /**
     *
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}