package com.youan.backendsystem.model.dto.user;

import java.io.Serializable;
import lombok.Data;

/**
 * 用户创建请求
 *
 * @author bobochang
 * @from <a href="https://blog.bobochang.work">bobochang's BLOG</a>
 */
@Data
public class UserAddRequest implements Serializable {

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 部门Id
     */
    private Long departmentId;


    private static final long serialVersionUID = 1L;
}