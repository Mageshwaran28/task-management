package com.zerp.taskmanagement.dbentity;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonView;
import com.zerp.taskmanagement.jsonview.View;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(value = {View.withOutChild.class})
    private long id;

    @JsonView(value = {View.withOutChild.class})
    private String name;

    @JsonView(value = {View.withOutChild.class})
    private String description;

    @OneToOne
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
    private User creator;

    @ManyToMany
    @JoinTable(name = "project_assignments", joinColumns = {
            @JoinColumn(name = "project_id", referencedColumnName = "id") }, inverseJoinColumns = {
                    @JoinColumn(name = "assignee_id", referencedColumnName = "id") })
    private Set<User> assignees;

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

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Set<User> getAssignees() {
        return assignees;
    }

    public void setAssignees(Set<User> assignees) {
        this.assignees = assignees;
    }

}
