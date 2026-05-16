package com.userservice.service;

import com.microservices.payload.dto.UserDto;
import com.microservices.payload.request.User.ChangePasswordRequest;
import com.userservice.model.User;
import java.util.*;

public interface UserService {
    UserDto getUserByEmail(String email);
    UserDto getUserById(Long id);
    List<UserDto> getAllUsers();
}
