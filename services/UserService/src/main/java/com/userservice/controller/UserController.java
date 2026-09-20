package com.userservice.controller;

import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.User.UserResponse;
import com.userservice.model.User;
import com.userservice.service.UserService;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    public ApiResponse<UserResponse> getUserProfile(Authentication authentication){
        String email = authentication.getName();
        UserResponse userResponse = userService.getUserByEmail(email);
        return ApiResponse.success(userResponse);
    }

    @GetMapping("/{userId}")
    public ApiResponse<UserResponse> getUserById(@PathVariable Long userId){
        UserResponse userResponse = userService.getUserById(userId);
        return ApiResponse.success(userResponse);
    }

    @GetMapping
    public ApiResponse<List<UserResponse>> getUsers(){
        List<UserResponse> users = userService.getAllUsers();
        return ApiResponse.success(users);
    }

}
