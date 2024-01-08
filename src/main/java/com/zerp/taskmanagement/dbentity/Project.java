package com.zerp.taskmanagement.dbentity;



import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Project {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long projectId;
    private String projectName;
    private String projectDescription;
    private Date startDate;
    private Date endDate;

    @JsonIgnore
    @OneToMany(mappedBy = "project" , cascade =  CascadeType.ALL)
    public Set<Task> tasks = new HashSet<>();

    public long getProjectId() {
        return projectId;
    }
    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }
    public String getProjectName() {
        return projectName;
    }
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    public String getProjectDescription() {
        return projectDescription;
    }
    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public Set<Task> getTasks() {
        return tasks;
    }
    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }
    
}
