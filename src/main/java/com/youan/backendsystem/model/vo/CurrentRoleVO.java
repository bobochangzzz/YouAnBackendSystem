package com.youan.backendsystem.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 2023/4/24 - 10:29
 *
 * @author bobochang
 * @description
 */
@Data
public class CurrentRoleVO implements Serializable {

    private String roleName;

    private String roleIdentification;

    private Integer status;

    private String remark;
}
