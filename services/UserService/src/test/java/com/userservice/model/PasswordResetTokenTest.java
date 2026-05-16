package com.userservice.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.*;

public class PasswordResetTokenTest {
    @Test
    void testBuilder(){
        User user = User.builder()
                .id(1L)
                .fullName("John Deo")
                .email("john@gmail.com")
                .build();

        LocalDateTime expiry = LocalDateTime.now().plusHours(1);
        PasswordResetToken token = PasswordResetToken.builder()
                .id(1L)
                .token("abc123")
                .expiryDate(expiry)
                .user(user)
                .build();

        assertNotNull(token);
        assertEquals("abc123", token.getToken());
        assertEquals(user, token.getUser());
        assertEquals(expiry, token.getExpiryDate());
    }

    @Test
    void testSettersAndGetters(){
        PasswordResetToken token = new PasswordResetToken();

        User user = new User();
        user.setId(2L);

        LocalDateTime expiry = LocalDateTime.now().plusMinutes(30);
        token.setToken("reset-token");
        token.setUser(user);
        token.setExpiryDate(expiry);

        assertEquals("reset-token", token.getToken());
        assertEquals(user, token.getUser());
        assertEquals(expiry, token.getExpiryDate());
    }

    @Test
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
