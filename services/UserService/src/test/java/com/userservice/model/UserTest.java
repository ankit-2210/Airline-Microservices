package com.userservice.model;

import com.microservices.utils.UserRole;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    @Test
    void testUserBuilder(){
        User user = User.builder()
                .id(1L)
                .fullName("John Deo")
                .email("john@example.com")
                .phone("1234567890")
                .userRole(UserRole.ROLE_SYSTEM_ADMIN)
                .password("secret")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .lastLogin(LocalDateTime.now())
                .build();

        assertNotNull(user);
        assertEquals("John Deo", user.getFullName());
        assertEquals("john@example.com", user.getEmail());
        assertEquals(UserRole.ROLE_SYSTEM_ADMIN, user.getUserRole());
    }

    @Test
    void testSettersAndGetters(){
        User user = new User();
        user.setFullName("Jane Deo");
        user.setEmail("jane@example.com");
        user.setUserRole(UserRole.ROLE_USER);

        assertEquals("Jane Deo", user.getFullName());
        assertEquals("jane@example.com", user.getEmail());
        assertEquals(UserRole.ROLE_USER, user.getUserRole());
    }

}
