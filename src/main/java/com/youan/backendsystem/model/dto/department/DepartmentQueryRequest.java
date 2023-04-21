package com.youan.backendsystem.model.dto.department;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.youan.backendsystem.common.PageRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * 2023/4/21 - 10:34
 *
 * @author bobochang
 * @description
 */
@Data
public class DepartmentQueryRequest extends PageRequest implements Serializable {

    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 部门负责人
     */
    private String departmentHead;

    /**
     * 部门状态 0-开启 1-关闭
     */
    private Long status;
}
