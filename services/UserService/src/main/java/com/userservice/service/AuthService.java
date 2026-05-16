package com.userservice.service;

import com.microservices.payload.dto.UserDto;
import com.microservices.payload.request.User.ChangePasswordRequest;
import com.microservices.payload.response.User.AuthResponse;

public interface AuthService {
    AuthResponse signup(UserDto userDto);
    AuthResponse login(String email, String password);
    void changePassword(String email, String oldPassword, String newPassword);

    void forgotPassword(String email, String baseUrl);
    void resetPassword(String token, String newPassword);

}
