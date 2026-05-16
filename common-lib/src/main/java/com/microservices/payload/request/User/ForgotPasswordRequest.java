package com.microservices.payload.request.User;

import lombok.Data;

@Data
public class ForgotPasswordRequest {
    private String email;
}
