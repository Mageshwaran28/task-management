package com.zerp.taskmanagement.taskservice;

import java.net.UnknownHostException;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.zerp.taskmanagement.dbrepository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtService {

    @Autowired
    UserRepository userInfoRepository;

    private static final String SECERET = "!dsuDFkmGhHyfdDfKiUgJjGuDhKgFfYuJ6rf5fF66D8rDoKdFDe";
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24;

    public String generateToken(String userName , HttpServletRequest request) throws UnknownHostException {
        System.out.println(getSignKey().toString());
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
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECERET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserName(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
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

        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
