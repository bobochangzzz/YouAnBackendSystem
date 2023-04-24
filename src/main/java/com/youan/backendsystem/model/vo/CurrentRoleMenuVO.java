package com.youan.backendsystem.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 2023/4/24 - 11:28
 *
 * @author bobochang
 * @description
 */
@Data
public class CurrentRoleMenuVO implements Serializable {

    /**
     * 角色昵称
     */
    private String roleName;

    /**
     * 角色标识
     */
    private String roleIdentification;

    private StringBuffer menuNameStr;
}
