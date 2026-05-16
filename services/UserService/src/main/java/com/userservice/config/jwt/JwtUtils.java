package com.userservice.config.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;

@Component
public class JwtUtils {
    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.expirationTime}")
    private long expirationTime;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // generate token
    public String generateToken(Authentication authentication, Long userId){
        String email = authentication.getName();
        String roles = populateAuthorities(authentication.getAuthorities());

        return Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey())
                .compact();
    }

    // validate token
    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        }
        catch(JwtException | IllegalArgumentException e){
            throw e;
        }
    }

    // extract email
    public String getEmailFromToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Helper
    private String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<String> roles = new HashSet<>();
        for (GrantedAuthority auth : authorities) {
            roles.add(auth.getAuthority());
        }
        return String.join(",", roles);
    }

}
//
//AuthServiceImpl
//   ↓
//generateToken() ✅
//
//Client
//   ↓
//Authorization: Bearer <token>
//
//AuthTokenFilter  ← 🔥 MOST IMPORTANT
//   ↓
//validateToken() ✅
//getEmailFromToken() ✅
//        ↓
//CustomUserDetailService
//   ↓
//SecurityContext updated
//   ↓
//Controller runs