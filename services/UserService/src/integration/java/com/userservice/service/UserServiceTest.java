package com.userservice.service;


import com.microservices.payload.dto.UserDto;
import com.microservices.payload.response.User.UserResponse;
import com.microservices.utils.Users.UserRole;
import com.userservice.model.User;
import com.userservice.repository.UserRepository;
import com.userservice.service.Impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void test_getUserByEmail_Success(){
        User user = User.builder()
                .id(1L)
                .email("test@example.com")
                .fullName("Test User")
                .userRole(UserRole.ROLE_USER)
                .build();

        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(user));

        UserResponse userResponse = userService.getUserByEmail("test@example.com");

        assertNotNull(userResponse);
        assertEquals("test@example.com", userResponse.getEmail());
        assertEquals("Test User", userResponse.getFullName());

    }

}













