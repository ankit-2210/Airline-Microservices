package com.userservice.repository;

import com.airlineportal.utils.Users.UserRole;
import com.userservice.model.PasswordResetToken;
import com.userservice.model.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Optional;

@DataJpaTest
public class PasswordTokenRepositoryTest {
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Test
    @DisplayName("Should save token and find by token")
    void testFindByToken(){
        User user = User.builder()
                .email("john@example.com")
                .fullName("John Deo")
                .userRole(UserRole.ROLE_USER)
                .password("password")
                .build();

        PasswordResetToken token = PasswordResetToken.builder()
                .id(1L)
                .token("reset123")
                .expiryDate(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();

        PasswordResetToken saved = passwordResetTokenRepository.save(token);
        Optional<PasswordResetToken> found = passwordResetTokenRepository.findByToken("reset123");

        assertTrue(found.isPresent());

        assertEquals(saved.getToken(), found.get().getToken());
        assertEquals(user.getEmail(), found.get().getUser().getEmail());
    }

    @Test
    @DisplayName("Should return empty when token does not exist")
    void testFindByToken_NotFound(){
        Optional<PasswordResetToken> token = passwordResetTokenRepository.findByToken("invalid-token");

        assertFalse(token.isPresent());
    }

    @Test
    @DisplayName("Should delete password reset token")
    void testDeleteToken(){
        User user = User.builder()
                .email("john@example.com")
                .fullName("John Deo")
                .userRole(UserRole.ROLE_USER)
                .build();

        PasswordResetToken token = PasswordResetToken.builder()
                .token("delete-token")
                .expiryDate(LocalDateTime.now().plusMinutes(10))
                .user(user)
                .build();

        PasswordResetToken saved = passwordResetTokenRepository.save(token);
        passwordResetTokenRepository.delete(token);

        Optional<PasswordResetToken> result = passwordResetTokenRepository.findByToken("delete-token");
        assertFalse(result.isPresent());
    }

}
