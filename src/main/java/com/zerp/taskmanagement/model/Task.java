package com.zerp.taskmanagement.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zerp.taskmanagement.enums.Priority;
import com.zerp.taskmanagement.enums.Status;

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

    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime createdAt;
    private LocalDateTime startDate;
    private LocalDateTime dueDate;

    @JsonIgnore
    private int depth;

    @OneToMany(mappedBy = "task")
    private List<File> files;

    @OneToMany(mappedBy = "parentTask")
    private List<Task> childList;

    @ManyToOne
    @JoinColumn(name = "parent_task_id", referencedColumnName = "id")
    @JsonIgnore()
    private Task parentTask;

    @ManyToOne
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
    private User creator;

    @ManyToMany
    @JoinTable(name = "task_assignments", joinColumns = {
            @JoinColumn(name = "task_id", referencedColumnName = "id") }, inverseJoinColumns = {
                    @JoinColumn(name = "assignee_id", referencedColumnName = "id") })
    private Set<User> assignees;

    @ManyToOne
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

    public Set<User> getAssignees() {
        return assignees;
    }

    public void setAssignees(Set<User> assignees) {
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
