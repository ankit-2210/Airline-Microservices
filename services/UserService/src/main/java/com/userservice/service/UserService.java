package com.userservice.service;

import com.airlineportal.payload.response.User.UserResponse;
import com.userservice.model.User;
import java.util.*;

public interface UserService {
    UserResponse getUserByEmail(String email);
    UserResponse getUserById(Long id);

    List<UserResponse> getAllUsers();

}
