package com.zerp.taskmanagement.taskservice;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.zerp.taskmanagement.customexception.DuplicateInputException;
import com.zerp.taskmanagement.customexception.InvalidInputException;
import com.zerp.taskmanagement.dbentity.Role;
import com.zerp.taskmanagement.dbentity.User;
import com.zerp.taskmanagement.dbrepository.RoleRepository;
import com.zerp.taskmanagement.dbrepository.UserRepository;
import com.zerp.taskmanagement.dto.ChangePasswordDTO;
import com.zerp.taskmanagement.dto.UserDTO;
import com.zerp.taskmanagement.validation.Validator;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserService {

    @Autowired
    Validator validator;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User addUser(UserDTO userDTO) throws IllegalAccessException, UnknownHostException {

        if (isFieldsAreEmpty(userDTO)) {
            throw new InvalidInputException("Input fields are empty");
        }
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new DuplicateInputException();
        }
        User user = new User();
        user.setEmail(userDTO.getEmail());
        if (validator.isValidPassword(userDTO.getPassword())) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        Role role = roleRepository.findByRole(userDTO.getRole());

        if (role == null) {
            role = new Role();
            role.setRole(userDTO.getRole().toUpperCase());
            roleRepository.save(role);
        }
        user.setRole(role);

        InetAddress address = InetAddress.getLocalHost();
        user.setIpAddress(address.getHostAddress());

        userRepository.save(user);

        return user;
    }

    private boolean isFieldsAreEmpty(UserDTO userDTO) {

        if (userDTO.getEmail() == null || userDTO.getPassword() == null || userDTO.getRole() == null) {
            return true;
        }

        return false;
    }

    public String changePassword(ChangePasswordDTO changePasswordDTO, HttpServletRequest request) {
        String email = validator.getUserEmail(request);

        User user = userRepository.findByEmailIgnoreCase(email);
        if (user == null) {
            throw new InvalidInputException("Invalid email or password");
        }
        if (validator.isValidPassword(changePasswordDTO.getNewPassword())) {
            user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        }
        userRepository.save(user);

        return "Passord changed";
    }

    public String changeUserRole(String newRole, HttpServletRequest request) {

        String email = validator.getUserEmail(request);
        User user = userRepository.findByEmailIgnoreCase(email);
        if (user == null) {
            throw new InvalidInputException("Invalid email");
        }
        if (roleRepository.existsByRole(newRole)) {
            user.setRole(roleRepository.findByRole(newRole));
        } else {
            Role role = new Role();
            role.setRole(newRole);
            roleRepository.save(role);
            user.setRole(role);
        }
        userRepository.save(user);

        return "Role updated";
    }

}
