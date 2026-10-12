package com.adminservice.service;

import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.User.UserResponse;

import java.util.*;

public interface AdminUserService {
    ApiResponse<UserResponse> getUserById(Long userId);

    ApiResponse<List<UserResponse>> getAllUsers();

}
