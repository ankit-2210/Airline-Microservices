package com.userservice.service;

import com.microservices.payload.dto.UserDto;
import com.microservices.payload.request.User.ChangePasswordRequest;
import com.microservices.payload.request.User.UserRequest;
import com.microservices.payload.response.User.UserResponse;
import com.userservice.model.User;
import java.util.*;

public interface UserService {
    UserResponse getUserByEmail(String email);

    UserResponse getUserById(Long id);

    List<UserResponse> getAllUsers();
}
