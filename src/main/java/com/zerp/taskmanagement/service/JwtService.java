package com.zerp.taskmanagement.service;

import java.net.UnknownHostException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.zerp.taskmanagement.repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtService {

    @Autowired
    UserRepository userInfoRepository;

    private Key SECERET_KEY;
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24;

    public JwtService() throws NoSuchAlgorithmException {
        byte[] bytes = new byte[64];
        SecureRandom random = SecureRandom.getInstanceStrong();
        random.nextBytes(bytes);
        this.SECERET_KEY = Keys.hmacShaKeyFor(bytes);
    }

    public String generateToken(String userName , HttpServletRequest request) throws UnknownHostException {
        String ipAddress = request.getRemoteAddr();
        String registeredAddress = userInfoRepository.findByEmailIgnoreCase(userName).getIpAddress();

        if (!registeredAddress.equals(ipAddress)) {
            throw new UnknownHostException("Invalid IP address");
        }
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .claim("address", ipAddress)
                .setSubject(userName)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + EXPIRATION_TIME))
                .signWith(SECERET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUserName(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECERET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails, HttpServletRequest request) throws UnknownHostException {
        final String userName = extractUserName(token);

        Claims claims = extractAllClaims(token);
        String loginedAddress = claims.get("address", String.class);
        String localAddress = request.getRemoteAddr();

        if (!localAddress.equals(loginedAddress)) {
            throw new UnknownHostException("Invalid Ip address");
        }
        System.out.println();
        System.out.println(userName);
        System.out.println(userDetails.getUsername());
        System.out.println(userName.equals(userDetails.getUsername()));
        System.out.println(!isTokenExpired(token));
        System.out.println();

        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
