package com.microservices.payload.dto;

import com.microservices.utils.Users.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String password;   // only for auth input
    private UserRole userRole;
    private LocalDateTime lastLogin;

}
