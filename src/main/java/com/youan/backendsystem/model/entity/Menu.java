package com.youan.backendsystem.model.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName menu菜单
 */
@TableName(value = "menu")
@Data
public class Menu implements Serializable {
    /**
     *id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     *所属上级id
     */
    private Long pid;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 权限值
     */
    private String permissionValue;

    /**
     * 访问路径
     */
    private String path;

    /**
     * 组件路径
     */
    private String component;

    /**
     * 图标
     */
    private String icon;

    /**
     * 0开启 1关闭
     */
    private Integer status;

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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
