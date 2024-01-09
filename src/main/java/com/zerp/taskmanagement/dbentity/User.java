package com.zerp.taskmanagement.dbentity;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;
    private String employeeName;

    private long mobileNumber;
    private String employeeRole;
    private String username;
    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "creator")
    public Set<Task> creatorTask = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "assignee")
    public Set<Task> assigneTask = new HashSet<>();


    

    public User(String employeeName, long mobileNumber, String employeeRole, String username, String password) {
        this.employeeName = employeeName;
        this.mobileNumber = mobileNumber;
        this.employeeRole = employeeRole;
        this.username = username;
        this.password = password;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public long getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(long mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmployeeRole() {
        return employeeRole;
    }

    public void setEmployeeRole(String employeeRole) {
        this.employeeRole = employeeRole;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Task> getCreatorTask() {
        return creatorTask;
    }

    public void setCreatorTask(Set<Task> creatorTask) {
        this.creatorTask = creatorTask;
    }

    public Set<Task> getAssigneTask() {
        return assigneTask;
    }

    public void setAssigneTask(Set<Task> assigneTask) {
        this.assigneTask = assigneTask;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
       return List.of(()->employeeRole);
    }

    @Override
    public String getUsername() {
        return username;
    }

        
    public void setUserName(String username) {
        this.username = username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
