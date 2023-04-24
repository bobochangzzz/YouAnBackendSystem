package com.youan.backendsystem.model.dto.user;

import lombok.Data;

/**
 * 2023/4/24 - 09:17
 *
 * @author bobochang
 * @description
 */
@Data
public class UserUpdatePasswordRequest {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 重置后的密码
     */
    private String userPassword;
}
