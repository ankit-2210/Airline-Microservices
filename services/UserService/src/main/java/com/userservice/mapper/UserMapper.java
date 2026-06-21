package com.userservice.mapper;

import com.microservices.payload.dto.UserDto;
import com.microservices.payload.response.User.UserResponse;
import com.userservice.model.User;

import java.util.*;
import java.util.stream.Collectors;

public class UserMapper {
    public static UserResponse toResponse(User user){
        if(user == null)
            return null;
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .userRole(user.getUserRole())
                .lastLogin(user.getLastLogin())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public static UserDto toDto(User user){
        if(user == null)
            return null;
        return UserDto.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .userRole(user.getUserRole())
                .build();
    }

    public static List<UserResponse> toResponseList(List<User> users){
        return users.stream()
                .map(UserMapper::toResponse)
                .collect(Collectors.toList());
    }

}
