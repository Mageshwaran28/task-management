package com.zerp.taskmanagement.dbentity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.zerp.taskmanagement.jsonview.View;
import com.zerp.taskmanagement.myenum.Priority;
import com.zerp.taskmanagement.myenum.Status;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonView(value = { View.withOutChild.class })
    private String name;

    @JsonView(value = { View.withOutChild.class })
    private String description;

    @JsonView(value = { View.withOutChild.class })
    @Enumerated(EnumType.STRING)
    private Priority priority;

    @JsonView(value = { View.withOutChild.class })
    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime createdAt;
    private LocalDateTime startDate;
    private LocalDateTime dueDate;
    private int depth;

    @OneToMany(mappedBy = "task")
    private List<File> files;

    @OneToMany(mappedBy = "parentTask")
    @JsonView(value = { View.withChild.class })
    private List<Task> childList;

    @ManyToOne
    @JoinColumn(name = "parent_task_id", referencedColumnName = "id")
    @JsonIgnore()
    private Task parentTask;

    @ManyToOne
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
    @JsonView(value = { View.withOutChild.class })
    private User creator;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "task_assignments", joinColumns = {
            @JoinColumn(name = "task_id", referencedColumnName = "id") }, inverseJoinColumns = {
                    @JoinColumn(name = "assignee_id", referencedColumnName = "id") })
    @JsonView(value = { View.withOutChild.class })
    private List<User> assignees;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinTable(name = "project_task_assignments", joinColumns = {
            @JoinColumn(name = "task_id", referencedColumnName = "id") }, inverseJoinColumns = {
                    @JoinColumn(name = "project_id", referencedColumnName = "id") })
    @JsonView(value = { View.withOutChild.class })
    Project project;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
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

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public List<User> getAssignees() {
        return assignees;
    }

    public void setAssignees(List<User> assignees) {
        this.assignees = assignees;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public List<Task> getChildList() {
        return childList;
    }

    public void setChildList(List<Task> childList) {
        this.childList = childList;
    }

    public Task getParentTask() {
        return parentTask;
    }

    public void setParentTask(Task parentTask) {
        this.parentTask = parentTask;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

}
