package com.promptservice.service;

import java.util.*;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;


@Service
public class AIContextService {

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new IllegalStateException("User is not authenticated");
        }

        Object principal = authentication.getPrincipal();

        // Standard Spring Security JWT authentication
        JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) authentication;
        Object userId = jwtAuth.getToken().getClaim("userId");
        return parseUserId(userId);

        // Custom authentication with a Map principal
    }

    private Long parseUserId(Object value) {
        if (value == null) {
            throw new IllegalStateException("Authenticated principal does not contain userId");
        }

        try {
            return Long.valueOf(value.toString());
        }
        catch (NumberFormatException ex) {
            throw new IllegalStateException("Authenticated userId is not a valid number");
        }
    }

}
