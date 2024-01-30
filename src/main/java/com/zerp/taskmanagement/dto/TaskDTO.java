package com.zerp.taskmanagement.dto;

import java.time.LocalDateTime;
import java.util.Set;

public class TaskDTO {
    private String name;
    private String description;
    private int priority;
    private int status;
    private LocalDateTime startDate;
    private LocalDateTime dueDate;
    private String creator;
    private Set<String> assignees;
    private long projectId;
    private long parentTaskId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Set<String> getAssignees() {
        return assignees;
    }

    public void setAssignees(Set<String> assignees) {
        this.assignees = assignees;
    }

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public long getParentTaskId() {
        return parentTaskId;
    }

    public void setParentTaskId(long parentTaskId) {
        this.parentTaskId = parentTaskId;
    }

}