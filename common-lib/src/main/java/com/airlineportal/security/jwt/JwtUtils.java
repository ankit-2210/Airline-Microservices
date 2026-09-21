package com.airlineportal.security.jwt;

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
                .claim(JwtConstant.USER_ID, userId)
                .claim(JwtConstant.ROLES, roles)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    public String generateTokenFromEmail(String email, Long userId, String role){

        return Jwts.builder()
                .subject(email)
                .claim(JwtConstant.USER_ID, userId)
                .claim(JwtConstant.ROLES, List.of(role))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();

    }

    // validate token
    public boolean validateToken(String token){
        try{
            getClaims(token);
            return true;
        }
        catch (JwtException | IllegalArgumentException e){
            return false;
        }
    }

    // extract email
    public String getEmailFromToken(String token){
        return getClaims(token)
                .getSubject();
    }

    public Long getUserIdFromToken(String token){
        return getClaims(token)
                .get(JwtConstant.USER_ID, Long.class);
    }

    // Get Roles
    @SuppressWarnings("unchecked")
    public List<String> getRolesFromToken(String token) {
        return getClaims(token)
                .get(JwtConstant.ROLES, List.class);
    }

    // Get First Role
    public String getRoleFromToken(String token){
        List<String> roles = getRolesFromToken(token);
        if(roles == null || roles.isEmpty())
            return null;

        return roles.getFirst();
    }

    // Check Token Expiration
    public boolean isTokenExpired(String token){
        return getClaims(token)
                .getExpiration()
                .before(new Date());
    }


    private String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<String> roles = new HashSet<>();
        for (GrantedAuthority auth : authorities) {
            roles.add(auth.getAuthority());
        }

        return String.join(",", roles);
    }

    // Parse Claims
    private Claims getClaims(String token){

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
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