package com.airlineportal.payload.response.User;

import com.airlineportal.payload.dto.UserDto;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {
    private String jwt;
    private String message;
    private String title;
    private UserDto userDto;

}
