package com.zerp.taskmanagement.controlleradvice;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.zerp.taskmanagement.customexception.DuplicateInputException;
import com.zerp.taskmanagement.customexception.EmptyInputException;
import com.zerp.taskmanagement.customexception.InvalidInputException;
import com.zerp.taskmanagement.customexception.UnAuthorizeException;

import jakarta.persistence.EntityNotFoundException;

@ControllerAdvice
public class MyControllerAdvice {
    
    @ExceptionHandler(EmptyInputException.class)
    public ResponseEntity<String> handleEmptyInput(EmptyInputException emptyInputException){
        return new ResponseEntity<>("Input field is empty , Please look into it" , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<String> handeInvalidInput(InvalidInputException invalidInput){
        return new ResponseEntity<>(invalidInput.getErrorMessage() , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateInputException.class)
    public ResponseEntity<String> handeDuplicateInput(DuplicateInputException duplicateInputException){
        return new ResponseEntity<>(duplicateInputException.getErrorMessage() , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchField(NoSuchElementException noSuchElementException){
        return new ResponseEntity<>("No value is present in database , Please change your request" , HttpStatus.NOT_FOUND );
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> handleMEntityNotSupportedException(HttpRequestMethodNotSupportedException resolverException){
        return new ResponseEntity<>("The requested method is not supported , please check the requested method" , HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException entityNotFoundException){
        return new ResponseEntity<>("The requested entity is not found , please check the requested entity" , HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnAuthorizeException.class)
    public ResponseEntity<String> handleUnAuthorizeException(UnAuthorizeException exception){
        return new ResponseEntity<String>(exception.getErrorMessage(),HttpStatus.UNAUTHORIZED);
    }

}
