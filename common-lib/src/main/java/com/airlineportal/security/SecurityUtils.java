package com.airlineportal.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    private SecurityUtils() {

    }

    // Current Authentication
    public static Authentication getAuthentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    // Current Logged-in User
    public static JwtUser getCurrentUser(){
        Authentication authentication = getAuthentication();
        if(authentication == null)
            return null;

        Object principal = authentication.getPrincipal();
        if(principal instanceof JwtUser jwtUser)
            return jwtUser;

        return null;
    }

    // Logged-in User ID
    public static Long getCurrentUserId(){
        JwtUser user = getCurrentUser();
        return user != null ? user.getId() : null;
    }

    public static String getCurrentUserEmail(){
        JwtUser user = getCurrentUser();
        return user != null ? user.getEmail() : null;
    }

    // Logged-in Role
    public static String getCurrentRole(){
        JwtUser user = getCurrentUser();
        return user != null ? user.getRole() : null;
    }

    // Is Authenticated
    public static boolean isAuthenticated(){
        Authentication authentication = getAuthentication();

        return authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof UsernamePasswordAuthenticationToken
                && "anonymousUser".equals(authentication.getPrincipal()));
    }



}
