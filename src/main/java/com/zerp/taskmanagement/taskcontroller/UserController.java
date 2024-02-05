package com.zerp.taskmanagement.taskcontroller;

import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.zerp.taskmanagement.customexception.InvalidInputException;
import com.zerp.taskmanagement.customexception.UnAuthorizeException;
import com.zerp.taskmanagement.dbentity.User;
import com.zerp.taskmanagement.dto.ChangePasswordDTO;
import com.zerp.taskmanagement.dto.LoginDTO;
import com.zerp.taskmanagement.dto.UpdateUserDTO;
import com.zerp.taskmanagement.dto.UserDTO;
import com.zerp.taskmanagement.taskservice.JwtService;
import com.zerp.taskmanagement.taskservice.UserService;
import com.zerp.taskmanagement.validation.Validator;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    Validator validator;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public User register(@RequestBody UserDTO userDTO) throws IllegalAccessException, UnknownHostException {
        return userService.addUser(userDTO);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginDTO authRequest) throws UnknownHostException {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        if (authenticate.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getEmail());
        } else {
            throw new InvalidInputException("Invalid login");
        }
    }

    @PutMapping("users/change-password")
    public String changePassword(@RequestBody ChangePasswordDTO changePasswordD, HttpServletRequest request) {
        
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(validator.getUserEmail(request), changePasswordD.getCurrentPassword()));
        if (authenticate.isAuthenticated()) {
            return userService.changePassword(changePasswordD, request);
        }
        throw new UnAuthorizeException("Don't have permission to change your password , Invalid credentials");
       
    }

    @PutMapping("users")
    public String changeUserRole( @RequestBody UpdateUserDTO user, HttpServletRequest request) {
        return userService.changeUserRole(user.getRole(), request);
    }

}
