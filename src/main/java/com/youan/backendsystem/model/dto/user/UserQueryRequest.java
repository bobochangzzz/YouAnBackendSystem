package com.youan.backendsystem.model.dto.user;

import com.youan.backendsystem.common.PageRequest;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户查询请求
 *
 * @author bobochang
 * @from <a href="https://blog.bobochang.work">bobochang's BLOG</a>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryRequest extends PageRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 用户名称
     */
    private String userAccount;

    /**
     * 部门名称
     */
    private Integer departmentId;


    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 权限
     */
    private String userRoleName;

    private static final long serialVersionUID = 1L;
}