package com.userservice.model;

import com.microservices.utils.Users.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    @Test
    @DisplayName("Should create user using builder")
    void testUserBuilder(){
        LocalDateTime now = LocalDateTime.now();

        User user = User.builder()
                .id(1L)
                .fullName("John Deo")
                .email("john@example.com")
                .phone("1234567890")
                .userRole(UserRole.ROLE_SYSTEM_ADMIN)
                .password("secret")
                .createdAt(now)
                .updatedAt(now)
                .lastLogin(now)
                .build();

        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("John Deo", user.getFullName());
        assertEquals("john@example.com", user.getEmail());
        assertEquals("1234567890", user.getPhone());
        assertEquals(UserRole.ROLE_SYSTEM_ADMIN, user.getUserRole());
        assertEquals("secret", user.getPassword());

        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getUpdatedAt());
        assertEquals(now, user.getLastLogin());
    }

    @Test
    @DisplayName("Should set and get user values")
    void testSettersAndGetters(){
        User user = new User();

        user.setId(10L);
        user.setFullName("Jane Deo");
        user.setEmail("jane@example.com");
        user.setPhone("9999999999");
        user.setUserRole(UserRole.ROLE_USER);

        assertEquals(10L, user.getId());
        assertEquals("Jane Deo", user.getFullName());
        assertEquals("jane@example.com", user.getEmail());
        assertEquals("9999999999", user.getPhone());
        assertEquals(UserRole.ROLE_USER, user.getUserRole());
    }

    @Test
    @DisplayName("Should create empty user with no args constructor")
    void testNoArgsConstructor(){
        User user = new User();

        assertNotNull(user);

        assertNull(user.getId());
        assertNull(user.getEmail());
        assertNull(user.getFullName());
        assertNull(user.getUserRole());
    }

    @Test
    @DisplayName("Should update user fields")
    void testUpdateUser(){
        User user = User.builder()
                .fullName("Old Name")
                .email("old@test.com")
                .build();

        user.setFullName("New Name");
        user.setEmail("new@test.com");

        assertEquals("New Name", user.getFullName());
        assertEquals("new@test.com", user.getEmail());
    }

}
