package com.zerp.taskmanagement.dto;

import java.util.Set;

public class ProjectDTO {

    private String name;
    private String description;
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

    

}
