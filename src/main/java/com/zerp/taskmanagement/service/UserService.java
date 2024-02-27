package com.zerp.taskmanagement.service;

import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.zerp.taskmanagement.dto.ChangePasswordDTO;
import com.zerp.taskmanagement.dto.UserDTO;
import com.zerp.taskmanagement.exceptions.DuplicateInputException;
import com.zerp.taskmanagement.exceptions.InvalidInputException;
import com.zerp.taskmanagement.filter.TokenBlockList;
import com.zerp.taskmanagement.model.Role;
import com.zerp.taskmanagement.model.User;
import com.zerp.taskmanagement.repository.RoleRepository;
import com.zerp.taskmanagement.repository.UserRepository;
import com.zerp.taskmanagement.singletonmanager.ModelSingletonManager;
import com.zerp.taskmanagement.utils.CommonUtils;

import jakarta.mail.MessagingException;

@Service
public class UserService extends CommonUtils {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenBlockList tokenBlockList;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelSingletonManager modelSingletonManager;

    @Autowired
    private MailService emailService;

    public User signup(UserDTO userDTO, String ipAddress)
            throws IllegalAccessException, UnknownHostException, MessagingException {

        isFieldsAreEmpty(userDTO);
        isValidEmail(userDTO.getEmail());
        isValidPassword(userDTO.getPassword());
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new DuplicateInputException();
        } else if (!userDTO.getEmail().equals("admin@zirius.in") && userDTO.getRole().equals("ADMIN")) {
            throw new InvalidInputException("Invalid role for user " + userDTO.getRole());
        }
        User user = modelSingletonManager.getUserInstance();
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        Role role = roleRepository.findByRole(userDTO.getRole());

        if (role == null) {
            role = modelSingletonManager.getRoleInstance();
            role.setRole(userDTO.getRole());
            roleRepository.save(role);
        }
        user.setRole(role);
        user.setIpAddress(ipAddress);
        userRepository.save(user);
        emailService.sendMail( user.getEmail(), "Registration Success", "Registered successfully");

        return user;
    }

    public String signout(String token) {
        tokenBlockList.invalidateToken(token);
        return "User logged out";
    }

    private boolean isFieldsAreEmpty(UserDTO userDTO) {
        if (userDTO.getEmail() == null || userDTO.getPassword() == null || userDTO.getRole() == null) {
            throw new InvalidInputException("Input fields are empty");
        }
        return false;
    }

    public String changePassword(ChangePasswordDTO changePasswordDTO, String email, String token) {

        User user = userRepository.findByEmailIgnoreCase(email);
        if (!passwordEncoder.matches(changePasswordDTO.getCurrentPassword(), user.getPassword())) {
            throw new InvalidInputException("Password does not match");
        }
        if (isValidPassword(changePasswordDTO.getNewPassword())) {
            user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        }
        userRepository.save(user);
        tokenBlockList.invalidateToken(token);
        return "Passord changed";
    }

    public String changeUserRole(String newRole, String email) {
        User user = userRepository.findByEmailIgnoreCase(email);
        if (roleRepository.existsByRole(newRole)) {
            user.setRole(roleRepository.findByRole(newRole));
        } else {
            Role role = modelSingletonManager.getRoleInstance();
            role.setRole(newRole);
            roleRepository.save(role);
            user.setRole(role);
        }
        userRepository.save(user);

        return "Role updated";
    }

}
