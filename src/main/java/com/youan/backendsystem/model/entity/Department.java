package com.youan.backendsystem.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 部门
 *
 * @TableName department
 */
@TableName(value = "department")
@Data
public class Department implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 上级部门id
     */
    private Long parentId;

    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 部门负责人
     */
    private String departmentHead;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 部门状态 0-开启 1-关闭
     */
    private Long status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}