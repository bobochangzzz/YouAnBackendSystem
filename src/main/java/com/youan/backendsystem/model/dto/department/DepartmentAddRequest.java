package com.youan.backendsystem.model.dto.department;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 2023/4/21 - 10:24
 *
 * @author bobochang
 * @description
 */
@Data
public class DepartmentAddRequest implements Serializable {

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
    private Long departmentHeadId;

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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
