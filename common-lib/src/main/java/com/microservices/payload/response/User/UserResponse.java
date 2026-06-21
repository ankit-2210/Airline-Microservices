package com.microservices.payload.response.User;

import com.microservices.utils.Users.UserRole;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private UserRole userRole;
    private LocalDateTime lastLogin;
    private LocalDateTime createdAt;

}
