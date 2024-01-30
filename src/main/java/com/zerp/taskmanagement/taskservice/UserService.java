package com.zerp.taskmanagement.taskservice;

import org.springframework.beans.factory.annotation.Autowired;
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

@Service
public class UserService {

    @Autowired
    Validator validator;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    public User addUser(UserDTO userDTO) throws IllegalAccessException {

        if (isFieldsAreEmpty(userDTO)) {
            throw new InvalidInputException("Input fields are empty");
        }
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new DuplicateInputException();
        }
        User user = new User();
        user.setEmail(userDTO.getEmail());
        if (validator.isValidPassword(userDTO.getPassword())) {
            user.setPassword(userDTO.getPassword());
        }

        Role role = roleRepository.findByRole(userDTO.getRole());

        if (role == null) {
            role = new Role();
            role.setRole(userDTO.getRole());
        }
        user.setRole(role);
        userRepository.save(user);

        return user;
    }

    private boolean isFieldsAreEmpty(UserDTO userDTO) {

        if (userDTO.getEmail() == null || userDTO.getPassword() == null || userDTO.getRole() == null) {
            return true;
        }

        return false;
    }

    public String changePassword(String email, ChangePasswordDTO changePasswordDTO) {

        User user = userRepository.findByEmailAndPassword(email, changePasswordDTO.getCurrentPassword());

        if (user == null) {
            throw new InvalidInputException("Invalid email or password");
        }
        if (validator.isValidPassword(changePasswordDTO.getNewPassword())) {
            user.setPassword(changePasswordDTO.getNewPassword());
        }
        userRepository.save(user);

        return "Passord changed";
    }

    public String changeUserRole(String email, String newRole) {
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
