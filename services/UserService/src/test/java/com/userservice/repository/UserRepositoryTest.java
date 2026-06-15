package com.userservice.repository;

import com.microservices.utils.Users.UserRole;
import com.userservice.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Should save user and find by email")
    void testFindByEmail(){
        User user = User.builder()
                .email("john@example.com")
                .fullName("John Deo")
                .userRole(UserRole.ROLE_USER)
                .password("password")
                .build();
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByEmail("john@example.com");
        assertTrue(foundUser.isPresent());
        assertEquals("John Deo", foundUser.get().getFullName());
        assertEquals("john@example.com", foundUser.get().getEmail());
    }

    @Test
    @DisplayName("Should return empty when email not found")
    void testFindByEmail_NotFound() {
        Optional<User> user = userRepository.findByEmail("notfound@example.com");
        assertFalse(user.isPresent());
    }

    @Test
    @DisplayName("Should enforce unique email constraint")
    void testUniqueEmailConstraint(){
        User user1 = User.builder()
                .fullName("User One")
                .email("duplicate@example.com")
                .userRole(UserRole.ROLE_USER)
                .build();
        User user2 = User.builder()
                .fullName("User Two")
                .email("duplicate@example.com")
                .userRole(UserRole.ROLE_USER)
                .build();
        userRepository.save(user1);
        assertThrows(Exception.class, () -> {
            userRepository.saveAndFlush(user2);
        });
    }
}
