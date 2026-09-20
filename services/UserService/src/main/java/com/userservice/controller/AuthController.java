package com.userservice.controller;

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
    public ApiResponse<AuthResponse> signup(@Valid @RequestBody com.microservices.payload.dto.UserDto userDto) throws Exception {
        return ApiResponse.success(authService.signup(userDto));
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) throws Exception {
        return ApiResponse.success(authService.login(loginRequest.getEmail(), loginRequest.getPassword()));
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
