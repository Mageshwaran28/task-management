package com.zerp.taskmanagement.taskservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zerp.taskmanagement.customexception.DuplicateInputException;
import com.zerp.taskmanagement.customexception.InvalidInputException;
import com.zerp.taskmanagement.dbentity.Role;
import com.zerp.taskmanagement.dbentity.User;
import com.zerp.taskmanagement.dbrepository.RoleRepository;
import com.zerp.taskmanagement.dbrepository.UserRepository;
import com.zerp.taskmanagement.dto.UserDTO;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    public User addUser(UserDTO userDTO) throws IllegalAccessException {

        if (isFieldsAreEmpty(userDTO)) {
            throw new InvalidInputException("Input fields are empty");
        }
        if(userRepository.existsByEmail(userDTO.getEmail())){
            throw new DuplicateInputException();
        }

        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());

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

        if ( userDTO.getEmail() == null) {
            return true;
        }

        if (userDTO.getPassword() == null) {
            return true;
        }

        if (userDTO.getRole() == null) {
            return true;
        }

        return false;
    }

    

    // public List<User> findAll() {

    // List<User> users = userRepository.findAll();

    // if (users.size() == 0) {
    // throw new NoSuchElementException("No value is present in database , Please
    // change your request");
    // }
    // return users;
    // }

    // public User findByUserId(long userId) {

    // User user = userRepository.findByUserId(userId);

    // if (user == null) {
    // throw new NoSuchElementException("No value is present in database , Please
    // change your request");
    // }

    // return user;
    // }

    // public String updateUser(UpdateUserDTO user, long userId) {

    // User existUser = userRepository.findByUserId(userId);

    // if (userRepository.findByUserName(user.getUserName()) != null) {
    // throw new InvalidInputException("601", "This user already exists , please
    // choose another user name");

    // } else if (user.getMobileNumber() != 0 &&
    // Long.toString(user.getMobileNumber()).length() != 10) {

    // throw new InvalidInputException("601", "Input field is invalid , Please look
    // into it");
    // }

    // if (user.getUserName() != null) {
    // existUser.setUserName(user.getUserName());
    // }
    // if (user.getMobileNumber() != 0) {
    // existUser.setMobileNumber(user.getMobileNumber());
    // }
    // if (user.getEmployeeRole() != null) {
    // existUser.setEmployeeRole(user.getEmployeeRole());
    // }

    // userRepository.save(existUser);

    // return "Updated";
    // }

    // public String changeUserPassword(long userId, ChangePasswordDTO
    // changePassword) {

    // User user = userRepository.findByUserId(userId);
    // if (user == null) {
    // throw new NoSuchElementException("No value is present in database , Please
    // change your request");
    // }
    // if (user.getPassword().equals(changePassword.getCurrentPassword()) &&
    // changePassword.getNewPassword().length()>6) {
    // user.setPassword(changePassword.getNewPassword());
    // } else if (changePassword.getNewPassword().length() < 6) {
    // throw new InvalidInputException("601", "Password length is minimum 6
    // characters");
    // } else {
    // throw new InvalidInputException("601", "Passwords do not match , enter valid
    // password");
    // }

    // userRepository.save(user);

    // return "Successfully changed";
    // }

}
