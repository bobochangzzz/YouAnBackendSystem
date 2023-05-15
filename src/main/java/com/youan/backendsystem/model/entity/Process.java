package com.youan.backendsystem.model.entity;

import lombok.Data;

/**
 * @Author: zz
 * @CreateTime: 2023-05-15  19:53
 * @Description: 已部署流程表实体类
 * @Version: 1.0
 */
@Data
public class Process {
    String id;

    String deploymentId;

    String name;

    String resourceName;

    String key;

    String diagramresourceName;

    Integer version;

    Boolean suspended;
}
