package com.zerp.taskmanagement.dto;

public class UserDTO {

    private String name;
    private String email;
    private String role;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.trim();
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role.trim().toUpperCase();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    
}
