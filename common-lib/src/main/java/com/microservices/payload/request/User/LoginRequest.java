package com.microservices.payload.request.User;

import lombok.*;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
