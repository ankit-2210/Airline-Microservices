package com.userservice.controller;

import com.microservices.payload.dto.UserDto;
import com.microservices.payload.response.ApiResponse;
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
    public ResponseEntity<ApiResponse<UserDto>> getUserProfile(Authentication authentication){
        String email = authentication.getName();
        System.out.println(userService.getUserByEmail(email));
        UserDto userDto = userService.getUserByEmail(email);
        ApiResponse<UserDto> apiResponse = new ApiResponse<>(true, "User profile fetched successfully", userDto);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserDto>> getUserById(@PathVariable Long userId){
        UserDto userDto = userService.getUserById(userId);
        ApiResponse<UserDto> apiResponse = new ApiResponse<>(true, "User fetch by id", userDto);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDto>>> getUsers(){
        List<UserDto> users = userService.getAllUsers();
        ApiResponse<List<UserDto>> apiResponse = new ApiResponse<>(true, "Users fetched", users);
        return ResponseEntity.ok(apiResponse);
    }


}
