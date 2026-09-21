package com.userservice.service.Impl;

import com.airlineportal.exception.ResourceAlreadyExistsException;
import com.airlineportal.exception.ResourceNotFoundException;
import com.airlineportal.exception.UnauthorizedException;
import com.airlineportal.payload.dto.UserDto;
import com.airlineportal.payload.response.User.AuthResponse;
import com.airlineportal.utils.Users.UserRole;
import com.airlineportal.security.jwt.JwtUtils;
import com.userservice.helper.AuthHelper;
import com.userservice.mapper.UserMapper;
import com.userservice.model.PasswordResetToken;
import com.userservice.model.User;
import com.userservice.repository.PasswordResetTokenRepository;
import com.userservice.repository.UserRepository;
import com.userservice.service.AuthService;
import com.userservice.utils.EmailUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.*;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final EmailUtil emailUtil;
    private final AuthHelper helper;

//    Check if email already exists
//    Encode password using BCrypt
//    Save user in database
//    Generate JWT token
//    Return token and user information
    @Override
    public AuthResponse signup(UserDto userDto){
        String email = helper.normalizeEmail(userDto.getEmail());

        if(userRepository.existsByEmail(email)) {
            throw new ResourceAlreadyExistsException("Email already registered");
        }

        if(userDto.getUserRole() == null) {
            throw new IllegalArgumentException("User role is required");
        }

        if (userDto.getUserRole() == UserRole.ROLE_SYSTEM_ADMIN) {
            throw new IllegalArgumentException("You cannot sign up as system admin");
        }

        // Create user
        User user = User.builder()
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .phone(userDto.getPhone())
                .userRole(userDto.getUserRole())
                .fullName(userDto.getFullName())
                .build();

        User savedUser = userRepository.save(user);

        // Authenticate via Spring Security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword()));

        // Generate JWT
        String jwt = jwtUtils.generateToken(authentication, savedUser.getId());

        return AuthResponse.builder()
                .jwt(jwt)
                .userDto(UserMapper.toDto(savedUser))
                .title("Welcome " + savedUser.getFullName())
                .message("Registration successful")
                .build();
    }

//    Load user by email
//    Compare password with BCrypt
//    Update lastLogin time
//    Generate Jwt Token
//    Return token and user information
    @Override
    public AuthResponse login(String email, String password) {
        String normalizedEmail = helper.normalizeEmail(email);

        // Authenticate via Spring Security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(normalizedEmail, password)
        );

        User user = helper.findUserByEmail(normalizedEmail);
        user.setLastLogin(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        // Generate JWT
        String jwt = jwtUtils.generateToken(authentication, user.getId());

        // Response
        return AuthResponse.builder()
                .jwt(jwt)
                .userDto(UserMapper.toDto(user))
                .title("Welcome " + user.getFullName())
                .message("Login successful")
                .build();
    }

    @Override
    public void changePassword(String email, String oldPassword, String newPassword) {
        User user = helper.findUserByEmail(email);

        // check old password
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new UnauthorizedException("Old password is incorrect");
        }

        // set new password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public void forgotPassword(String email, String baseUrl) {
        User user = helper.findUserByEmail(email);

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusMinutes(15))
                .build();

        passwordResetTokenRepository.save(resetToken);
        String url = baseUrl + "/auth/reset-password?token="+token;

        try{
            emailUtil.sendResetMail(user.getEmail(), url);
        }
        catch (Exception e){
            throw new RuntimeException("Email sending failed");
        }
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid token"));

        if(resetToken.getExpiryDate().isBefore(LocalDateTime.now())){
            passwordResetTokenRepository.delete(resetToken);
            throw new UnauthorizedException("Token expired");
        }

        User user = resetToken.getUser();

        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);
        passwordResetTokenRepository.delete(resetToken);
    }


//    private Authentication authentication(String email, String password){
//        UserDetails userDetails = customUserDetailService.loadUserByUsername(email);
//        if(!passwordEncoder.matches(password, userDetails.getPassword())){
//            throw new BadCredentialsException("Invalid password");
//        }
//        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//    }




}
