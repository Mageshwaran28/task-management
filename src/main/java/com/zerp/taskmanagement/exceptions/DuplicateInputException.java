package com.zerp.taskmanagement.exceptions;

public class DuplicateInputException extends RuntimeException {

    private String errorMessage = "Email Already Exists";


    public String getErrorMessage() {
        return errorMessage;
    }

}
