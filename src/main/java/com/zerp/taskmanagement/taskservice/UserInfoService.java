package com.zerp.taskmanagement.taskservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.zerp.taskmanagement.dbentity.User;
import com.zerp.taskmanagement.dbrepository.UserRepository;


@Service
public class UserInfoService implements UserDetailsService  {
    
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userInfo = userRepository.findByEmailIgnoreCase(username);
        if (userInfo != null) {
            return new UserInfoDetails(userInfo);
        } else {
            throw new UsernameNotFoundException("User not found" + username);
        }
    }

}
