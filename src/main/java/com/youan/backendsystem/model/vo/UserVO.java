package com.youan.backendsystem.model.vo;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 用户视图（脱敏）
 *
 * @author bobochang
 * @from <a href="https://blog.bobochang.work">bobochang's BLOG</a>
 */
@Data
public class UserVO implements Serializable {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户名称
     */
    private String userAccount;


    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 权限名称
     */
    private String userRoleName;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}