package com.zerp.taskmanagement.controller;

import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.zerp.taskmanagement.dto.ChangePasswordDTO;
import com.zerp.taskmanagement.dto.LoginDTO;
import com.zerp.taskmanagement.dto.UpdateUserDTO;
import com.zerp.taskmanagement.dto.UserDTO;
import com.zerp.taskmanagement.exceptions.InvalidInputException;
import com.zerp.taskmanagement.model.User;
import com.zerp.taskmanagement.service.JwtService;
import com.zerp.taskmanagement.service.UserService;
import com.zerp.taskmanagement.utils.CommonUtils;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class UserController extends CommonUtils {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/signup")
    public User signup(@RequestBody UserDTO userDTO, HttpServletRequest request)
            throws IllegalAccessException, UnknownHostException, MessagingException {
        return userService.signup(userDTO, request.getRemoteAddr());
    }

    @PostMapping("/signin")
    public String signin(@RequestBody LoginDTO authRequest, HttpServletRequest request) throws UnknownHostException {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        if (authenticate.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getEmail(), request);
        } else {
            throw new InvalidInputException("Invalid login credentials");
        }
    }

    @PostMapping("/signout")
    public String signout(HttpServletRequest request) {
        return userService.signout(getToken(request));
    }

    @PutMapping("users/change-password")
    public String changePassword(@RequestBody ChangePasswordDTO changePasswordD, HttpServletRequest request) {
        return userService.changePassword(changePasswordD, getUserEmail(request), getToken(request));
    }

    @PutMapping("users")
    public String changeUserRole(@RequestBody UpdateUserDTO user, HttpServletRequest request) {
        return userService.changeUserRole(user.getRole(), getUserEmail(request));
    }

    @GetMapping("/users")
    public String getUserName(HttpServletRequest request) {
        return getUserEmail(request);
    }

}
