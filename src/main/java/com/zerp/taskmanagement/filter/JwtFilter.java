package com.zerp.taskmanagement.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.zerp.taskmanagement.exceptions.InvalidInputException;
import com.zerp.taskmanagement.service.JwtService;
import com.zerp.taskmanagement.service.UserInfoService;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private TokenBlockList tokenBlockList;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException, java.io.IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String userName = null;
        if (authHeader != null && authHeader.startsWith("Bearer")) {
            token = authHeader.substring(7);
            userName = jwtService.extractUserName(token);
            System.out.println();
            System.out.println(userName);
            System.out.println();
        }

        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userInfoService.loadUserByUsername(userName);
            System.out.println();
            System.out.println("not null");
            System.out.println(tokenBlockList.isBlackListed(token));
            System.out.println(jwtService.validateToken(token, userDetails, request));
            System.out.println();
            if (!tokenBlockList.isBlackListed(token) && jwtService.validateToken(token, userDetails, request)) {
                System.out.println();
                System.out.println("token valid");
                System.out.println();
                Authentication authToken = new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
                        userDetails.getPassword(), userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                throw new InvalidInputException("Invalid token");
            }
        }
        filterChain.doFilter(request, response);
    }

}
