package com.zerp.taskmanagement.customexception;

public class DuplicateInputException extends RuntimeException {

    private String errorMessage = "Duplicate email address dedected , please check your email address";


    public String getErrorMessage() {
        return errorMessage;
    }

}
