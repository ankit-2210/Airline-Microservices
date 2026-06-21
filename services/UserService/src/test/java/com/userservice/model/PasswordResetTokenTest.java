package com.userservice.model;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.*;

public class PasswordResetTokenTest {
    @Test
    @DisplayName("Should create password reset token using builder")
    void testBuilder(){
        User user = User.builder()
                .id(1L)
                .fullName("John Deo")
                .email("john@gmail.com")
                .build();

        LocalDateTime expiry = LocalDateTime.now().plusMinutes(15);
        PasswordResetToken token = PasswordResetToken.builder()
                .id(1L)
                .token("abc123")
                .expiryDate(expiry)
                .user(user)
                .build();

        assertNotNull(token);

        assertEquals(1L, token.getId());
        assertEquals("abc123", token.getToken());
        assertEquals(user, token.getUser());
        assertEquals(expiry, token.getExpiryDate());
    }

    @Test
    @DisplayName("Should set and get password reset token fields")
    void testSettersAndGetters(){
        PasswordResetToken token = new PasswordResetToken();

        User user = new User();
        user.setId(2L);

        LocalDateTime expiry = LocalDateTime.now().plusMinutes(30);

        token.setId(5L);
        token.setToken("reset-token");
        token.setUser(user);
        token.setExpiryDate(expiry);

        assertEquals(5L, token.getId());
        assertEquals("reset-token", token.getToken());
        assertEquals(user, token.getUser());
        assertEquals(expiry, token.getExpiryDate());
    }

    @Test
    @DisplayName("Should create empty token using no args constructor")
    void testNoArgsConstructor(){
        PasswordResetToken token = new PasswordResetToken();

        assertNotNull(token);

        assertNull(token.getId());
        assertNull(token.getToken());
        assertNull(token.getUser());
        assertNull(token.getExpiryDate());

    }

    @Test
    @DisplayName("Should detect expired token")
    void testExpiryLogic(){
        LocalDateTime pastTime = LocalDateTime.now().minusMinutes(10);

        PasswordResetToken token = PasswordResetToken.builder()
                .token("expired-token")
                .expiryDate(pastTime)
                .build();

        boolean isExpired = token.getExpiryDate().isBefore(LocalDateTime.now());
        assertTrue(isExpired);
    }
}
