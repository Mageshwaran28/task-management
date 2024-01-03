package com.zerp.taskmanagement.taskservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zerp.taskmanagement.dbentity.User;
import com.zerp.taskmanagement.dbrepository.UserRepository;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public String addUser(User user) {
        userRepository.save(user);
        return "success";
    }
    
}
