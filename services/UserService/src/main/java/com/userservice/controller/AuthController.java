package com.userservice.controller;

import com.microservices.payload.dto.UserDto;
import com.microservices.payload.request.User.ChangePasswordRequest;
import com.microservices.payload.request.User.LoginRequest;
import com.microservices.payload.response.ApiResponse;
import com.microservices.payload.response.User.AuthResponse;
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
    public ResponseEntity<ApiResponse<AuthResponse>> signup(@Valid @RequestBody UserDto userDto) throws Exception {
        AuthResponse authResponse = authService.signup(userDto);
        ApiResponse<AuthResponse> response = new ApiResponse<>(true, "SignUp Successfully", authResponse);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest loginRequest) throws Exception {
        AuthResponse authResponse = authService.login(loginRequest.getEmail(), loginRequest.getPassword());
        ApiResponse<AuthResponse> response = new ApiResponse<>(true, "Login Successfully", authResponse);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(Authentication authentication, @Valid @RequestBody ChangePasswordRequest request){
        String email = authentication.getName();
        authService.changePassword(email, request.getOldPassword(), request.getNewPassword());
        ApiResponse<Void> apiResponse = new ApiResponse<>(true, "Password change successfully", null);
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(@RequestParam String email, HttpServletRequest request){
        String baseUrl = request.getRequestURL().toString()
                .replace(request.getServletPath(), "");
        authService.forgotPassword(email, baseUrl);
        ApiResponse<Void> apiResponse = new ApiResponse<>(true, "Reset link sent to email", null);
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@RequestParam String token, @RequestParam String newPassword){
        authService.resetPassword(token, newPassword);
        ApiResponse<Void> apiResponse = new ApiResponse<>(true, "Password reset successful", null);
        return ResponseEntity.ok(apiResponse);
    }

}
