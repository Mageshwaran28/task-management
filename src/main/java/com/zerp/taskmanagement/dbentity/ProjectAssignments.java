package com.zerp.taskmanagement.dbentity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "project_assignments")
public class ProjectAssignments {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    
    @Column(name = "project_id")
    private long projectId;

    @Column(name = "assignee_id")
    private long assigneeId;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public long getProjectId() {
        return projectId;
    }
    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }
    public long getAssigneeId() {
        return assigneeId;
    }
    public void setAssigneeId(long assigneeId) {
        this.assigneeId = assigneeId;
    }

    

}
