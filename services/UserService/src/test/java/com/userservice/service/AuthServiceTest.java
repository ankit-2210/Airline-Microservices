package com.userservice.service;

import com.airlineportal.exception.ResourceNotFoundException;
import com.airlineportal.exception.UnauthorizedException;
import com.airlineportal.payload.dto.UserDto;
import com.airlineportal.payload.response.User.AuthResponse;
import com.airlineportal.utils.Users.UserRole;
import com.airlineportal.security.jwt.JwtUtils;
import com.userservice.model.PasswordResetToken;
import com.userservice.model.User;
import com.userservice.repository.PasswordResetTokenRepository;
import com.userservice.repository.UserRepository;
import com.userservice.service.Impl.AuthServiceImpl;
import com.userservice.utils.EmailUtil;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private EmailUtil emailUtil;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void testSignupSuccess(){
        UserDto dto = UserDto.builder()
                .email("john@gmail.com")
                .password("123456")
                .fullName("John Doe")
                .phone("123456")
                .userRole(UserRole.ROLE_USER)
                .build();

        when(userRepository.existsByEmail(dto.getEmail()))
                .thenReturn(false);

        when(passwordEncoder.encode("123456"))
                .thenReturn("encodedPassword");

        User savedUser = User.builder()
                .id(1L)
                .email(dto.getEmail())
                .fullName(dto.getFullName())
                .userRole(UserRole.ROLE_USER)
                .build();

        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);
        when(authenticationManager.authenticate(any()))
                .thenReturn(authentication);

        when(jwtUtils.generateToken(authentication, 1L))
                .thenReturn("jwt-token");

        AuthResponse authResponse = authService.signup(dto);

        assertNotNull(authResponse);
        assertEquals("jwt-token", authResponse.getJwt());

        verify(userRepository).save(any(User.class));
    }

    @Test
    void testLoginSuccess(){
        User user = User.builder()
                .email("john@gmail.com")
                .password("123456")
                .fullName("John Doe")
                .phone("123456")
                .userRole(UserRole.ROLE_USER)
                .build();

        when(authenticationManager.authenticate(any()))
                .thenReturn(authentication);
        when(userRepository.findByEmail("john@gmail.com"))
                .thenReturn(Optional.of(user));

        when(jwtUtils.generateToken(authentication, 1L))
                .thenReturn("jwt-token");

        AuthResponse response = authService.login("john@gmail.com", "123456");

        assertEquals("jwt-token", response.getJwt());

        verify(userRepository).save(user);
    }

    @Test
    void testLoginUserNotFound(){
        when(authenticationManager.authenticate(any()))
                .thenReturn(authentication);
        when(userRepository.findByEmail("abc@gmail.com"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> authService.login("abc@gmail.com", "123"));
    }

    @Test
    void testChangePasswordSuccess(){
        User user = User.builder()
                .email("john@gmail.com")
                .password("old")
                .build();

        when(userRepository.findByEmail("john@gmail.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("oldPassword", "old"))
                .thenReturn(true);

        when(passwordEncoder.encode("newPassword"))
                .thenReturn("newEncoded");

        authService.changePassword("john@gmail.com", "oldPassword", "newPassword");
        assertEquals("newEncoded", user.getPassword());
    }

    @Test
    void testChangePasswordWrongOldPassword(){
        User user = User.builder()
                .email("john@gmail.com")
                .password("old")
                .build();
        when(userRepository.findByEmail("john@gmail.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("wrong", "old"))
                .thenReturn(false);

        assertThrows(UnauthorizedException.class, () -> authService.changePassword("john@gmail.com", "wrong", "new"));
    }

    @Test
    void testResetPassword(){
        User user = User.builder()
                .id(1L)
                .password("old")
                .build();

        PasswordResetToken token = PasswordResetToken.builder()
                .token("abc")
                .user(user)
                .expiryDate(LocalDateTime.now().plusMinutes(15))
                .build();

        when(passwordResetTokenRepository.findByToken("abc"))
                .thenReturn(Optional.of(token));
        when(passwordEncoder.encode("new"))
                .thenReturn("encodedNew");

        authService.resetPassword("abc", "new");

        assertEquals("encodedNew", user.getPassword());

        verify(passwordResetTokenRepository).delete(token);
    }



}
