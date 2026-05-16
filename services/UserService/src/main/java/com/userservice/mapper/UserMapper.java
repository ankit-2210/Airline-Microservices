package com.userservice.mapper;

import com.microservices.payload.dto.UserDto;
import com.userservice.model.User;

import java.util.*;
import java.util.stream.Collectors;

public class UserMapper {
    public static UserDto toDto(User user){
        if(user == null)
            return null;

        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .userRole(user.getUserRole())
                .phone(user.getPhone())
                .lastLogin(user.getLastLogin())
                .build();
    }

    public static List<UserDto> toDtoList(List<User> users){
        return users.stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

}
