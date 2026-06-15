package com.userservice.service.Impl;

import com.microservices.exception.*;
import com.microservices.payload.dto.UserDto;
import com.microservices.payload.response.User.AuthResponse;
import com.microservices.utils.Users.UserRole;
import com.userservice.config.jwt.JwtUtils;
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

//    Check if email already exists
//    Encode password using BCrypt
//    Save user in database
//    Generate JWT token
//    Return token and user information
    @Override
    public AuthResponse signup(UserDto userDto){
        // Check if email exists
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new ResourceAlreadyExistsException("Email already registered");
        }
        // Prevent system admin signup
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
                .lastLogin(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        User savedUser = userRepository.save(user);

        // Authenticate via Spring Security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDto.getEmail(),
                        userDto.getPassword()
                )
        );

        // Generate JWT
        String jwt = jwtUtils.generateToken(authentication, savedUser.getId());

        // Build Response
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setUserDto(UserMapper.toDto(savedUser));
        authResponse.setTitle("Welcome " + savedUser.getFullName());
        authResponse.setMessage("Registered Successfully");
        return authResponse;
    }

//    Load user by email
//    Compare password with BCrypt
//    Update lastLogin time
//    Generate Jwt Token
//    Return token and user information
    @Override
    public AuthResponse login(String email, String password) {
        // Authenticate via Spring Security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
        // Fetch user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Update last login
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        // Generate JWT
        String jwt = jwtUtils.generateToken(authentication, user.getId());

        // Response
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setUserDto(UserMapper.toDto(user));
        authResponse.setTitle("Welcome back " + user.getFullName());
        authResponse.setMessage("Login Successfully");
        return authResponse;
    }

    @Override
    public void changePassword(String email, String oldPassword, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        // check old password
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new UnauthorizedException("Old password is incorrect");
        }
        // set new password
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public void forgotPassword(String email, String baseUrl) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new ResourceNotFoundException("User not found"));
        String token = UUID.randomUUID().toString();
        System.out.println("TOKEN: " + token);
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusMinutes(15))
                .build();
        passwordResetTokenRepository.save(resetToken);
        String url = baseUrl + "/auth/reset-password?token="+token;
        try{
            emailUtil.sendResetMail(email, url);
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
            throw new UnauthorizedException("Token expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());
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
