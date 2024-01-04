package com.zerp.taskmanagement.dto;

import org.springframework.stereotype.Component;

@Component
public class TaskDTO {
    private long taskId;
    private long projectId;
    private long creatorId;
    private long assigneeId;

    public long getTaskId() {
        return taskId;
    }
    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public long getCreatorId() {
        return creatorId;
    }
    public void setCreatorId(long creatorId) {
        this.creatorId = creatorId;
    }
    public long getAssigneeId() {
        return assigneeId;
    }
    public void setAssigneeId(long assigneeId) {
        this.assigneeId = assigneeId;
    }
    public long getProjectId() {
        return projectId;
    }
    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    
}
