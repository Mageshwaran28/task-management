package com.zerp.taskmanagement.taskservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.zerp.taskmanagement.dbentity.User;
import com.zerp.taskmanagement.dbrepository.UserRepository;

@Service
public class UserInfoService implements UserDetailsService {
    @Autowired
    private UserRepository userInfoRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userInfo = userInfoRepository.findByEmailIgnoreCase(username);
        if (userInfo != null) {
            return new UserInfoDetailsService(userInfo);
        } else {
            throw new UsernameNotFoundException("User not found" + username);
        }
    }
}
