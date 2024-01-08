package com.zerp.taskmanagement.appconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        UserDetails admin = User.withUsername("magesh").password(encoder.encode("pwd123")).roles("admin").build();
        UserDetails user = User.withUsername("balaji").password(encoder.encode("pwd1234")).roles("user").build();

        return new InMemoryUserDetailsManager(admin , user);
    }

    // public SecurityFilterChain securityFilterChain(HttpSecurity http){
    //     return http.authorizeHttpRequests().requestMatchers("/taskmanagement/tasks").hasRole("admin" ).requestMatchers;
    // }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
