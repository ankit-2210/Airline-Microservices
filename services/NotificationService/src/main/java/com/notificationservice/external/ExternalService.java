package com.notificationservice.external;

import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.User.UserResponse;
import org.springframework.web.bind.annotation.*;


public interface ExternalService {

    // User Service
    @GetMapping("/internal/users/{userId}")
    ApiResponse<UserResponse> getUserById(@PathVariable Long userId);

}
