package com.zerp.taskmanagement.taskcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.zerp.taskmanagement.dbentity.User;
import com.zerp.taskmanagement.dto.UserDTO;
import com.zerp.taskmanagement.taskservice.UserService;


@RestController
public class UserController {
    
    @Autowired
    UserService userService;

    @PostMapping("/register")
    public User register(@RequestBody UserDTO userDTO ) throws IllegalAccessException{
        return userService.addUser(userDTO);
    }

}
