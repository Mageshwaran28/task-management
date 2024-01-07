package com.zerp.taskmanagement.taskcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zerp.taskmanagement.dbentity.User;
import com.zerp.taskmanagement.taskservice.UserService;


@RestController
@RequestMapping("taskmanagement/")
public class UserController {
    
    @Autowired
    UserService userService;

    @PostMapping("/user/add")
    public String addTask(@RequestBody User user ){
        return userService.addUser(user);
    }

    @GetMapping("/users")
    public List<User> geAlltUsers(){
        return userService.findAll();
    }

    @GetMapping("/user/{userId}")
    public User getUserById(@PathVariable long userId) {
        return userService.findByUserId(userId);
    }

    @PutMapping("/user/{userId}")
    public String updateUser(@PathVariable long userId, @RequestBody User user){
        return userService.updateUser(user , userId);
    }

    @PutMapping("/user/{userId}/{currentPassword}/{newPassword}")
    public String changeUserPassword(@PathVariable long userId , @PathVariable String currentPassword , @PathVariable String newPassword){
        return userService.changeUserPassword(userId, currentPassword , newPassword);
    }


}
