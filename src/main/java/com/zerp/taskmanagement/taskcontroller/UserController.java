package com.zerp.taskmanagement.taskcontroller;

import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.zerp.taskmanagement.customexception.InvalidInputException;
import com.zerp.taskmanagement.dbentity.User;
import com.zerp.taskmanagement.dto.ChangePasswordDTO;
import com.zerp.taskmanagement.dto.LoginDTO;
import com.zerp.taskmanagement.dto.UpdateUserDTO;
import com.zerp.taskmanagement.dto.UserDTO;
import com.zerp.taskmanagement.taskservice.JwtService;
import com.zerp.taskmanagement.taskservice.UserService;


@RestController
public class UserController {
    
    @Autowired
    UserService userService;

     @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public User register(@RequestBody UserDTO userDTO ) throws IllegalAccessException, UnknownHostException{
        return userService.addUser(userDTO);
    }

     @PostMapping("/login")
    public String login(@RequestBody LoginDTO authRequest) throws UnknownHostException{
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        if(authenticate.isAuthenticated()){
            System.out.println(authRequest.getEmail());
            return jwtService.generateToken(authRequest.getEmail());
        }else {
            throw new InvalidInputException("Invalid login");
        }
    }

    @PutMapping("users/{email}/change-password")
    public String changePassword(@PathVariable String email , @RequestBody ChangePasswordDTO changePasswordD){
        return userService.changePassword(email,changePasswordD);
    }

    @PutMapping("users/{email}")
    public String changeUserRole(@PathVariable String email , @RequestBody UpdateUserDTO user){
        return userService.changeUserRole(email , user.getRole());
    }

}
