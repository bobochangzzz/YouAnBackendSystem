package com.youan.backendsystem.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 2023/5/12 - 11:14
 *
 * @author bobochang
 * @description
 */
@Data
public class DepartmentVO implements Serializable {

    /*
     * 部门编号
     */
    private Long id;

    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 上级部门名称
     */
    private String parDepartmentName;

    /**
     * 部门负责人名称
     */
    private String departmentHeadName;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    private static final long serialVersionUID = 1L;
}
