package com.youan.backendsystem.model.dto.role;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 2023/4/24 - 10:48
 *
 * @author bobochang
 * @description 角色分配菜单请求参数
 */
@Data
public class AssignMenuRequest implements Serializable {

    /**
     * 分配角色id
     */
    private Long roleId;

    /**
     * 分配的菜单ids列表
     */
    private List<Long> menuIds;
}
