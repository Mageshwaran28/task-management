package com.zerp.taskmanagement.controlleradvice;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.zerp.taskmanagement.customexception.EmptyInputException;
import com.zerp.taskmanagement.customexception.InvalidInputException;

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

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchField(NoSuchElementException noSuchElementException){
        return new ResponseEntity<>("No value is present in database , Please change your request" , HttpStatus.NOT_FOUND );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleDefaultException(HttpMessageNotReadableException resolverException){
        return new ResponseEntity<>("Invalid status or priority , please check your input" , HttpStatus.NOT_FOUND );
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> handleMEntityNotSupportedException(HttpRequestMethodNotSupportedException resolverException){
        return new ResponseEntity<>("The requested method is not supported , please check the requested method" , HttpStatus.NOT_FOUND);
    }

}
