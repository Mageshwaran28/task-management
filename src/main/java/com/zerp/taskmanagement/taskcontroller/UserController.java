package com.zerp.taskmanagement.taskcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zerp.taskmanagement.dbentity.User;
import com.zerp.taskmanagement.taskservice.UserService;


@RestController
@RequestMapping("taskmanagement/user")
public class UserController {
    
    @Autowired
    UserService userService;

    @PostMapping("/add")
    public String addTask(@RequestBody User user ){
        return userService.addUser(user);
    }

}
