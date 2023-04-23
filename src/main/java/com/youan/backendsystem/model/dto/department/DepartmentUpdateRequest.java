package com.youan.backendsystem.model.dto.department;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * 2023/4/21 - 10:31
 *
 * @author bobochang
 * @description
 */
@Data
public class DepartmentUpdateRequest implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 上级部门id
     */
    private Long parentId;

    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 部门负责人id
     */
    private String departmentHeadId;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 部门状态 0-开启 1-关闭
     */
    private Integer status;
}
