package com.zerp.taskmanagement.taskservice;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zerp.taskmanagement.customexception.EmptyInputException;
import com.zerp.taskmanagement.customexception.InvalidInputException;
import com.zerp.taskmanagement.dbentity.User;
import com.zerp.taskmanagement.dbrepository.UserRepository;
import com.zerp.taskmanagement.dto.ChangePasswordDTO;
import com.zerp.taskmanagement.dto.UpdateUserDTO;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public String addUser(User user) {
        if (user.getUsername().isEmpty() ||
                user.getUsername().length() == 0 ||
                user.getEmployeeName().isEmpty() ||
                user.getEmployeeName().length() == 0 ||
                user.getEmployeeRole().isEmpty() ||
                user.getEmployeeRole().length() == 0 ||
                Long.toString(user.getMobileNumber()).isEmpty() ||
                Long.toString(user.getMobileNumber()).length() == 0 ||
                user.getPassword().isEmpty() ||
                user.getPassword().length() == 0) {

            throw new EmptyInputException("601", "Input fields are empty");

        } else if (Long.toString(user.getMobileNumber()).length() != 10 ||
                user.getPassword().length() < 6) {

            throw new InvalidInputException("601", "Input field is invalid , Please look into it");

        } else if (userRepository.findByUsername(user.getUsername()) != null) {

            throw new InvalidInputException("601", "This user already exists , please choose another user name");

        }

        userRepository.save(user);
        return "success";
    }

    public List<User> findAll() {

        List<User> users = userRepository.findAll();

        if (users.size() == 0) {
            throw new NoSuchElementException("No value is present in database , Please change your request");
        }
        return users;
    }

    public User findByUserId(long userId) {

        User user = userRepository.findByUserId(userId);

        if (user == null) {
            throw new NoSuchElementException("No value is present in database , Please change your request");
        }

        return user;
    }

    public String updateUser(UpdateUserDTO user, long userId) {

        User existUser = userRepository.findByUserId(userId);

        if (userRepository.findByUsername(user.getUserName()) != null) {
            throw new InvalidInputException("601", "This user already exists , please choose another user name");

        } else if (user.getMobileNumber() != 0 && Long.toString(user.getMobileNumber()).length() != 10) {

            throw new InvalidInputException("601", "Input field is invalid , Please look into it");
        }

        if (user.getUserName() != null) {
            existUser.setUserName(user.getUserName());
        }
        if (user.getMobileNumber() != 0) {
            existUser.setMobileNumber(user.getMobileNumber());
        }
        if (user.getEmployeeRole() != null) {
            existUser.setEmployeeRole(user.getEmployeeRole());
        }

        userRepository.save(existUser);

        return "Updated";
    }

    public String changeUserPassword(long userId, ChangePasswordDTO changePassword) {

        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new NoSuchElementException("No value is present in database , Please change your request");
        }
        if (user.getPassword().equals(changePassword.getCurrentPassword()) && changePassword.getNewPassword().length()>6) {
            user.setPassword(changePassword.getNewPassword());
        } else if (changePassword.getNewPassword().length() < 6) {
            throw new InvalidInputException("601", "Password length is minimum 6 characters");
        } else {
            throw new InvalidInputException("601", "Passwords do not match , enter valid password");
        }

        userRepository.save(user);

        return "Successfully changed";
    }

}
