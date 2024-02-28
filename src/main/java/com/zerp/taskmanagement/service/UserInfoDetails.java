package com.zerp.taskmanagement.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.zerp.taskmanagement.model.User;

public class UserInfoDetails implements UserDetails {
    
    String userName = null;
    String password = null;
    List<GrantedAuthority> authorities;

    public UserInfoDetails(User userInfo) {
        userName = userInfo.getEmail().toLowerCase();
        password = userInfo.getPassword();
        authorities = Arrays.asList(new SimpleGrantedAuthority(userInfo.getRole().getRole()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
