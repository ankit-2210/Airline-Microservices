package com.userservice.controller;

import com.airlineportal.payload.dto.UserDto;
import com.airlineportal.payload.request.User.ChangePasswordRequest;
import com.airlineportal.payload.request.User.LoginRequest;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.User.AuthResponse;
import com.airlineportal.payload.response.User.RefreshResult;
import com.userservice.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @Value("${security.jwt.refreshCookieName:airline_refresh}")
    private String refreshCookieName;

    @Value("${security.jwt.refreshCookieSecure:false}")
    private boolean refreshCookieSecure;

    @Value("${security.jwt.refreshExpirationDays:7}")
    private long refreshExpirationDays;


    @PostMapping("/signup")
    public ApiResponse<AuthResponse> signup(@Valid @RequestBody UserDto userDto, HttpServletResponse response) {
        AuthResponse authResponse = authService.signup(userDto);

        String refreshToken = authService.issueRefreshToken(authResponse.getUserDto().getEmail());
        setRefreshCookie(response, refreshToken);

        return ApiResponse.success(authResponse);
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        AuthResponse authResponse = authService.login(request.getEmail(), request.getPassword());

        String refreshToken = authService.issueRefreshToken(authResponse.getUserDto().getEmail());
        setRefreshCookie(response, refreshToken);

        return ApiResponse.success(authResponse);
    }

    @PostMapping("/refresh")
    public ApiResponse<AuthResponse> refresh(HttpServletRequest request, HttpServletResponse response){
        String refreshToken = readRefreshCookie(request);

        RefreshResult result = authService.refreshAccessToken(refreshToken);
        setRefreshCookie(response, result.refreshToken());

        return ApiResponse.success(result.authResponse());
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = readRefreshCookie(request);

        authService.logout(refreshToken);
        clearRefreshCookie(response);

        return ApiResponse.success(null);
    }


    @PostMapping("/change-password")
    public ApiResponse<Void> changePassword(Authentication authentication, @Valid @RequestBody ChangePasswordRequest request){
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new org.springframework.security.authentication.AuthenticationCredentialsNotFoundException("Authentication is required");
        }

        authService.changePassword(authentication.getName(), request.getOldPassword(), request.getNewPassword());
        return ApiResponse.success(null);
    }

    @PostMapping("/forgot-password")
    public ApiResponse<Void> forgotPassword(@RequestParam String email, HttpServletRequest request){
        String baseUrl = request.getRequestURL()
                .toString()
                .replace(request.getServletPath(), "");

        authService.forgotPassword(email, baseUrl);
        return ApiResponse.success(null);
    }

    @PostMapping("/reset-password")
    public ApiResponse<Void> resetPassword(@RequestParam String token, @RequestParam String newPassword){
        authService.resetPassword(token, newPassword);
        return ApiResponse.success(null);
    }


    // Cookie Helpers
    private void setRefreshCookie(HttpServletResponse response, String token) {
        ResponseCookie cookie = ResponseCookie
                .from(refreshCookieName, token)
                .httpOnly(true)
                .secure(refreshCookieSecure)
                .sameSite("Lax")
                .path("/auth")
                .maxAge(Duration.ofDays(refreshExpirationDays))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private void clearRefreshCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie
                .from(refreshCookieName, "")
                .httpOnly(true)
                .secure(refreshCookieSecure)
                .sameSite("Lax")
                .path("/auth")
                .maxAge(Duration.ZERO)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private String readRefreshCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (refreshCookieName.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }


}
