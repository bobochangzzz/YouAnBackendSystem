package com.youan.backendsystem.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: zz
 * @CreateTime: 2023-04-23  15:01
 * @Description: 员工实体类
 * @TableName employee
 * @Version: 1.0
 */

@TableName(value = "employee")
@Data
public class Employee implements Serializable {
    /**
     * 员工编号
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 员工名称
     */
    private String name;

    /**
     * 员工性别
     */
    private Integer sex;


    /**
     * 籍贯
     */
    private String nativePlace;

    /**
     * 职务
     */
    private String position;

    /**
     * 学历
     */
    private String career;

    /**
     * 员工状态
     */
    private Integer status;

    /**
     * 毕业院校
     */
    private String school;

    /**
     * 专业
     */
    private String major;

    /**
     * 技能证书
     */
    private String certificate;

    /**
     * 入职时间
     */
    private Date entryTime;

    /**
     * 离职时间
     */
    private Date resignTime;

    /**
     * 身份证到期时间
     */
    private Date cardTime;


    /**
     * 员工所属部门编号
     */
    private Long departmentId;


    /**
     * 身份证号码
     */
    private String card;

    /**
     * 家庭联系地址
     */
    private String address;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 银行卡号
     */
    private String bankCard;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 备注
     */
    private String remark;

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
