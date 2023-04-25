package com.youan.backendsystem.model.vo;

import com.alibaba.excel.annotation.ExcelProperty;

import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: zz
 * @CreateTime: 2023-04-25  09:58
 * @Description: TODO
 * @Version: 1.0
 */
@Data
public class EmployeeExcelVO implements Serializable {

    /**
     * 员工编号
     */
    @ExcelProperty(value = "员工编号", index = 0)
    private Long id;

    /**
     * 员工名称
     */
    @ExcelProperty(value = "员工名称", index = 1)
    private String name;

    /**
     * 员工性别
     */
    @ExcelProperty(value = "员工性别", index = 2)
    private String sex;


    /**
     * 籍贯
     */
    @ExcelProperty(value = "籍贯", index = 3)
    private String nativePlace;

    /**
     * 职务
     */
    @ExcelProperty(value = "职务", index = 4)
    private String position;

    /**
     * 学历
     */
    @ExcelProperty(value = "学历", index = 5)
    private String career;

    /**
     * 员工状态
     */
    @ExcelProperty(value = "员工状态", index = 6)
    private Integer status;

    /**
     * 毕业院校
     */
    @ExcelProperty(value = "毕业院校", index = 7)
    private String school;

    /**
     * 专业
     */
    @ExcelProperty(value = "专业", index = 8)
    private String major;

    /**
     * 技能证书
     */
    @ExcelProperty(value = "技能证书", index = 9)
    private String certificate;

    /**
     * 入职时间
     */
    @ExcelProperty(value = "入职时间", index = 10)
    @DateTimeFormat("yyyy-MM-dd")
    private Date entryTime;

    /**
     * 离职时间
     */
    @ExcelProperty(value = "离职时间", index = 11)
    @DateTimeFormat("yyyy-MM-dd")
    private Date resignTime;

    /**
     * 身份证到期时间
     */
    @ExcelProperty(value = "身份证到期时间", index = 12)
    @DateTimeFormat("yyyy-MM-dd")
    private Date cardTime;


    /**
     * 员工所属部门编号
     */
    @ExcelProperty(value = "员工所属部门编号", index = 13)
    private String departmentName;


    /**
     * 身份证号码
     */
    @ExcelProperty(value = "身份证号码", index = 14)
    private String card;

    /**
     * 家庭联系地址
     */
    @ExcelProperty(value = "家庭联系地址", index = 15)
    private String address;

    /**
     * 手机号码
     */
    @ExcelProperty(value = "手机号码", index = 16)
    private String phone;

    /**
     * 银行卡号
     */
    @ExcelProperty(value = "银行卡号", index = 17)
    private String bankCard;

    /**
     * 邮箱
     */
    @ExcelProperty(value = "邮箱", index = 18)
    private String email;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注", index = 19)
    private String remark;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间", index = 20)
    private Date createTime;



}
