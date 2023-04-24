package com.youan.backendsystem.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 2023/4/24 - 10:08
 *
 * @author bobochang
 * @description
 */
@Data
public class CurrentUserVO implements Serializable {

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;


}
