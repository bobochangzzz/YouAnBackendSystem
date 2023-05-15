package com.youan.backendsystem.model.entity;

import lombok.Data;

/**
 * @Author: zz
 * @CreateTime: 2023-05-15  20:59
 * @Description: 流程管理任务信息实体
 * @Version: 1.0
 */
@Data
public class TaskInfo {
    String taskId;

    String processInstanceId;

    String executionId;

    String businessKey;

    String processName;

    String taskName;

    String starter;

    String assignee;

    String startTime;

    String endTime;

    String createTime;

    String formKey;

    String comment;

    Integer pageSize;

    Integer pageNum;

}
