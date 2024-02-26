package com.zerp.taskmanagement.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zerp.taskmanagement.exceptions.UnAuthorizeException;
import com.zerp.taskmanagement.service.JwtService;
import com.zerp.taskmanagement.validation.Validator;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class CommonUtils extends Validator {

    @Autowired
    JwtService jwtService;

    public String getUserEmail(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        String userName = jwtService.extractUserName(token);
        return userName;
    }

    public String getToken(HttpServletRequest request) {
        return request.getHeader("Authorization").substring(7);
    }

    public void throwUnAuthorizedException(String error){
        throw new UnAuthorizeException(error);
    }

}
