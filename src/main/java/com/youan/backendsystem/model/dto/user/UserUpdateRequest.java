package com.youan.backendsystem.model.dto.user;

import java.io.Serializable;

import lombok.Data;

/**
 * 用户更新请求
 *
 * @author bobochang
 * @from <a href="https://blog.bobochang.work">bobochang's BLOG</a>
 */
@Data
public class UserUpdateRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 部门id
     */
    private Long departmentId;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 用户状态
     */
    private Integer status;

    /**
     * 用户权限
     */
    private String userRoleName;
    /**
     * 用户密码
     */
    private String userPassword;


    private static final long serialVersionUID = 1L;
}