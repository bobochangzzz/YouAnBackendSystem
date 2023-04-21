package com.youan.backendsystem.model.dto.role;

import com.youan.backendsystem.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 2023/4/20 - 15:27
 *
 * @author bobochang
 * @description 角色查询请求类
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleQueryRequest extends PageRequest implements Serializable {

    /**
     * 角色名称
     */
    private String roleName;


    /**
     * 状态
     */
    private Long status;

    /**
     * 备注
     */
    private String remark;
}
