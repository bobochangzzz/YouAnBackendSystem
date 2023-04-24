package com.youan.backendsystem.model.dto.role;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 2023/4/23 - 15:15
 *
 * @author bobochang
 * @description 分配角色请求类
 */
@Data
public class AssignRoleRequest implements Serializable {
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 分配角色
     */
    private String userRoleEnumText;
}
