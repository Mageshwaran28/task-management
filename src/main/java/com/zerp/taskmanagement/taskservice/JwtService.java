package com.zerp.taskmanagement.taskservice;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.zerp.taskmanagement.dbrepository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;


@Component
public class JwtService {
    
    @Autowired
    UserRepository userInfoRepository;

    private static final String SECERET = "!@#$FDGSDFGSGSGSGSHSHSHSSHGFFDSGSFGSSGHSDFSDFSFSFSFSDFSFSFSF";

    public String generateToken(String userName) throws UnknownHostException {
        InetAddress address = InetAddress.getLocalHost();
        String ipAddress = address.getHostAddress();
        System.out.println("ip"+ ipAddress);
        String registeredAddress = userInfoRepository.findByEmailIgnoreCase(userName).getIpAddress();

        System.out.println("registeredAddress"+ registeredAddress);

        if(!registeredAddress.equals(ipAddress)){
            throw new UnknownHostException("Invalid IP address");
        }

        Map<String, Objects> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .claim("address", ipAddress)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECERET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
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

    public Boolean validateToken(String token, UserDetails userDetails) throws UnknownHostException {
        final String userName = extractUserName(token);
        
        Claims claims = extractAllClaims(token);
        String loginedAddress = claims.get("address" , String.class );

        InetAddress address = InetAddress.getLocalHost();
        String localAddress = address.getHostAddress();

        if(!localAddress.equals(loginedAddress)){
            throw new UnknownHostException("Invalid local address");
        }

        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
