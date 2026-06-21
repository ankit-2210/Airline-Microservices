package com.userservice.service;


import com.microservices.exception.ResourceNotFoundException;
import com.microservices.payload.response.User.UserResponse;
import com.microservices.utils.Users.UserRole;
import com.userservice.model.User;
import com.userservice.repository.UserRepository;
import com.userservice.service.Impl.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Should get user by email")
    void testGetUserByEmail() {
        User user = User.builder()
                .id(1L)
                .fullName("John Doe")
                .email("john@gmail.com")
                .phone("123456789")
                .userRole(UserRole.ROLE_USER)
                .build();

        when(userRepository.findByEmail("john@gmail.com"))
                .thenReturn(Optional.of(user));

        UserResponse response = userService.getUserByEmail("john@gmail.com");

        assertNotNull(response);
        assertEquals("John Doe", response.getFullName());
        assertEquals("john@gmail.com", response.getEmail());

        verify(userRepository).findByEmail("john@gmail.com");
    }

    @Test
    @DisplayName("Should throw exception when email not found")
    void testGetUserByEmail_NotFound() {
        when(userRepository.findByEmail("abc@gmail.com"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, ()-> userService.getUserByEmail("abc@gmail.com"));
    }

    @Test
    @DisplayName("Should get user by id")
    void testGetUserById(){
        User user = User.builder()
                .id(10L)
                .fullName("Alex")
                .email("alex@gmail.com")
                .userRole(UserRole.ROLE_USER)
                .build();

        when(userRepository.findById(10L))
                .thenReturn(Optional.of(user));

        UserResponse response = userService.getUserById(10L);

        assertNotNull(response);
        assertEquals(10L, response.getId());
        assertEquals("Alex", response.getFullName());

        verify(userRepository).findById(10L);
    }

    @Test
    @DisplayName("Should throw exception when user id not found")
    void testGetUserById_NotFound(){
        when(userRepository.findById(100L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(100L));
    }


    @Test
    @DisplayName("Should return all users")
    void testGetAllUsers(){
        List<User> users = List.of(
            User.builder()
                    .id(1L)
                    .fullName("John")
                    .email("john@gmail.com")
                    .userRole(UserRole.ROLE_USER)
                    .build(),
            User.builder()
                    .id(2L)
                    .fullName("Mike")
                    .email("mike@gmail.com")
                    .userRole(UserRole.ROLE_USER)
                    .build()
        );

        when(userRepository.findAll())
                .thenReturn(users);

        List<UserResponse> response = userService.getAllUsers();

        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("John", response.get(0).getFullName());

        verify(userRepository).findAll();
    }




}







