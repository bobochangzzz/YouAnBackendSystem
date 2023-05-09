package com.youan.backendsystem.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.youan.backendsystem.model.entity.Menu;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author: zz
 * @CreateTime: 2023-05-04  14:52
 * @Description: TODO
 * @Version: 1.0
 */
@Data
public class MenuVo implements Serializable {
    /**
     *id
     */
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

    @Transient
    List<MenuVo> subMenuList;
}
