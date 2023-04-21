package com.youan.backendsystem.model.dto.role;

import lombok.Data;

import java.io.Serializable;

/**
 * 2023/4/20 - 16:30
 *
 * @author bobochang
 * @description
 */
@Data
public class RoleUpdateRequest implements Serializable {

    /**
     * 角色id
     */
    private Long id;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色状态
     * 0开启 1关闭
     */
    private Integer status;

    /**
     * 角色备注
     */
    private String remark;
}
