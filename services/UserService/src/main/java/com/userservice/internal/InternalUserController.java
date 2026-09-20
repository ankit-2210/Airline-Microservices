package com.userservice.internal;

import com.airlineportal.payload.response.User.UserResponse;
import com.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/users")
public class InternalUserController {
    private final UserService userService;

    @GetMapping("/{userId}")
    public UserResponse getUserById(@PathVariable Long userId){
        return userService.getUserById(userId);
    }

}
