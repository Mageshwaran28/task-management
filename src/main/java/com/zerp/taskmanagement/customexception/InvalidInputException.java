package com.zerp.taskmanagement.customexception;


public class InvalidInputException extends RuntimeException {
 
    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    public InvalidInputException(String errorMessage) {
        super();
        this.errorMessage = errorMessage;
    }

    public InvalidInputException(){
        
    }

}
