package com.zerp.taskmanagement.dto;

public class UpdateUserDTO {
    
    private String userName;
    private String employeeRole;
    private Long mobileNumber;
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getEmployeeRole() {
        return employeeRole;
    }
    public void setEmployeeRole(String employyeeRole) {
        this.employeeRole = employyeeRole;
    }
    public Long getMobileNumber() {
        return mobileNumber;
    }
    public void setMobileNumber(Long mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    

}
