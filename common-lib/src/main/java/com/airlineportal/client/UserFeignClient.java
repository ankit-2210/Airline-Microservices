package com.airlineportal.client;


import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.User.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
        name = "USERSERVICE",
        path = "/internal/users"
)
public interface UserFeignClient {

    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUserById(@PathVariable Long userId);

    @GetMapping
    public ApiResponse<List<UserResponse>> getAllUsers();


}
