package com.userservice.config.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class JwtUtils {

    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.expirationTime}")
    private long expirationTime;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // generate token
    public String generateToken(Authentication authentication, Long userId){
        String email = authentication.getName();
        String roles = populateAuthorities(authentication.getAuthorities());

        return Jwts.builder()
                .subject(email)
                .claim("userId", userId)
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey())
                .compact();
    }

    // validate token
    public boolean validateToken(String token){
        try{
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);

            return true;
        }
        catch(Exception e){
            return false;
        }
    }

    // extract email
    public String getEmailFromToken(String token){

        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    public Long getUserIdFromToken(String token){

        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        Object userId = claims.get("userId");
        if(userId instanceof Number){
            return ((Number) userId).longValue();
        }

        if(userId instanceof String){
            return Long.valueOf((String) userId);
        }

        return null;
    }


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