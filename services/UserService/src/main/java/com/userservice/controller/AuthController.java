package com.userservice.controller;

import com.airlineportal.payload.dto.UserDto;
import com.airlineportal.payload.request.User.ChangePasswordRequest;
import com.airlineportal.payload.request.User.LoginRequest;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.User.AuthResponse;
import com.userservice.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ApiResponse<AuthResponse> signup(@Valid @RequestBody UserDto userDto) {
        return ApiResponse.success(authService.signup(userDto));
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ApiResponse.success(authService.login(loginRequest.getEmail(), loginRequest.getPassword()));
    }

    @PostMapping("/change-password")
    public ApiResponse<Void> changePassword(Authentication authentication, @Valid @RequestBody ChangePasswordRequest request){
        String email = authentication.getName();
        authService.changePassword(email, request.getOldPassword(), request.getNewPassword());
        return ApiResponse.success(null);
    }

    @PostMapping("/forgot-password")
    public ApiResponse<Void> forgotPassword(@RequestParam String email, HttpServletRequest request){
        String baseUrl = request.getRequestURL().toString()
                .replace(request.getServletPath(), "");
        authService.forgotPassword(email, baseUrl);
        return ApiResponse.success(null);
    }

    @PostMapping("/reset-password")
    public ApiResponse<Void> resetPassword(@RequestParam String token, @RequestParam String newPassword){
        authService.resetPassword(token, newPassword);
        return ApiResponse.success(null);
    }

}
