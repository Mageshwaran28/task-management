package com.zerp.taskmanagement.dto;

import java.time.LocalDateTime;
import java.util.Set;

public class ProjectDTO {

    private String name;
    private String description;
    private int status;
    private LocalDateTime startDate;
    private LocalDateTime dueDate;
    private Set<Long> assignees;

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

    public Set<Long> getAssignees() {
        return assignees;
    }

    public void setAssignees(Set<Long> assignees) {
        this.assignees = assignees;
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

    
}
