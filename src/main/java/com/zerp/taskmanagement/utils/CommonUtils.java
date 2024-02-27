package com.zerp.taskmanagement.utils;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zerp.taskmanagement.exceptions.UnAuthorizeException;
import com.zerp.taskmanagement.model.User;
import com.zerp.taskmanagement.repository.UserRepository;
import com.zerp.taskmanagement.service.JwtService;
import com.zerp.taskmanagement.singletonmanager.CollectionSingletonManager;
import com.zerp.taskmanagement.validation.Validator;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class CommonUtils extends Validator {

    @Autowired
    JwtService jwtService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CollectionSingletonManager collectionSingletonManager;

    public String getUserEmail(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        String userName = jwtService.extractUserName(token);
        return userName;
    }

    public String getToken(HttpServletRequest request) {
        return request.getHeader("Authorization").substring(7);
    }

    public void throwUnAuthorizedException(String error) {
        throw new UnAuthorizeException(error);
    }

    public User getCreator(String creatorEmail) {
        return userRepository.findByEmailIgnoreCase(creatorEmail);
    }

    public Set<User> getAssignees(Set<Long> assigneesId) {
        Set<User> assignees = collectionSingletonManager.getUserHashSetInstance();

        for (long id : assigneesId) {
            isValidUser(id);
            assignees.add(userRepository.findById(id).get());
        }
        return assignees;
    }

    

}
