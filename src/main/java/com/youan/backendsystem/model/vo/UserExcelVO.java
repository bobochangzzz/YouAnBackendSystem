package com.youan.backendsystem.model.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 2023/4/24 - 14:12
 *
 * @author bobochang
 * @description
 */
@Data
public class UserExcelVO implements Serializable {

    @ExcelProperty(value = "id", index = 0)
    private Long id;

    @ExcelProperty(value = "用户名称", index = 1)
    private String userAccount;

    @ExcelProperty(value = "用户昵称", index = 2)
    private String userName;

    @ExcelProperty(value = "部门名称", index = 3)
    private String departmentName;

    @ExcelProperty(value = "手机号码", index = 4)
    private String phone;

    @ExcelProperty(value = "创建时间", index = 5)
    private Date createTime;

    @ExcelProperty(value = "状态", index = 6)
    private Integer status;


}
