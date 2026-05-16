package com.microservices.payload.response.User;

import com.microservices.payload.dto.UserDto;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String jwt;
    private String message;
    private String title;
    private UserDto userDto;
}
