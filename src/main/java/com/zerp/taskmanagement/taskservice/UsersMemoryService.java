package com.zerp.taskmanagement.taskservice;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.zerp.taskmanagement.dbentity.User;

@Service
public class UsersMemoryService {
    
    public List<UserDetails> users(){
        UserDetails u1 = new User("Mageshwaran", 7339448499l, "Intern", "magesh", "magesh@123");

        UserDetails u2 = new User("Balaji", 7339448499l, "Employee", "balaji", "balaji@123");

        return List.of(u1, u2);
    }

}
