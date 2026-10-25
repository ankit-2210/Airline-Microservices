package com.userservice.service.Impl;

import com.airlineportal.exception.ResourceAlreadyExistsException;
import com.airlineportal.exception.ResourceNotFoundException;
import com.airlineportal.exception.UnauthorizedException;
import com.airlineportal.payload.dto.UserDto;
import com.airlineportal.payload.response.User.AuthResponse;
import com.airlineportal.payload.response.User.RefreshResult;
import com.airlineportal.utils.Users.UserRole;
import com.airlineportal.security.jwt.JwtUtils;
import com.userservice.helper.AuthHelper;
import com.userservice.mapper.UserMapper;
import com.userservice.model.PasswordResetToken;
import com.userservice.model.RefreshToken;
import com.userservice.model.User;
import com.userservice.repository.PasswordResetTokenRepository;
import com.userservice.repository.RefreshTokenRepository;
import com.userservice.repository.UserRepository;
import com.userservice.service.AuthService;
import com.userservice.utils.EmailUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final EmailUtil emailUtil;
    private final AuthHelper helper;

    @Value("${security.jwt.refreshExpirationDays:7}")
    private long refreshExpirationDays;

//    Check if email already exists
//    Encode password using BCrypt
//    Save user in database
//    Generate JWT token
//    Return token and user information
    @Override
    @Transactional
    public AuthResponse signup(UserDto userDto){
        String email = helper.normalizeEmail(userDto.getEmail());

        if(userRepository.existsByEmail(email)) {
            throw new ResourceAlreadyExistsException("Email already registered");
        }

        if (userDto.getPassword() == null || userDto.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }

        // Public signup must not trust a role supplied by the client.
        // Replace ROLE_USER if your enum uses a different customer role.
        User user = User.builder()
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .phone(userDto.getPhone())
                .fullName(userDto.getFullName())
                .userRole(UserRole.ROLE_USER)
                .build();

        User savedUser = userRepository.save(user);

        // Authenticate via Spring Security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, userDto.getPassword()));

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
    @Transactional
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
        String jwt = jwtUtils.generateToken(authentication, savedUser.getId());

        // Response
        return AuthResponse.builder()
                .jwt(jwt)
                .userDto(UserMapper.toDto(savedUser))
                .title("Welcome " + savedUser.getFullName())
                .message("Login successful")
                .build();
    }


    // Issue Refresh Token
    @Override
    @Transactional
    public String issueRefreshToken(String email) {
        User user = helper.findUserByEmail(helper.normalizeEmail(email));

        String rawToken = helper.generateOpaqueToken();
        RefreshToken token = RefreshToken.builder()
                .user(user)
                .tokenHash(helper.hashToken(rawToken))
                .familyId(UUID.randomUUID().toString())
                .expiresAt(Instant.now().plus(refreshExpirationDays, ChronoUnit.DAYS))
                .revoked(false)
                .build();

        refreshTokenRepository.save(token);
        return rawToken;

    }

    // Refresh Access Token + Rotate Refresh Token
    @Override
    @Transactional(noRollbackFor = UnauthorizedException.class)
    public RefreshResult refreshAccessToken(String rawToken) {
        if(rawToken == null || rawToken.isBlank()) {
            throw new UnauthorizedException("Refresh token is missing");
        }

        String oldHash = helper.hashToken(rawToken);
        RefreshToken oldToken = refreshTokenRepository.findByTokenHash(oldHash)
                .orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));

        // Detect replay of a token that has already been rotated/revoked.
        if (oldToken.isRevoked()) {
            refreshTokenRepository.revokeFamily(oldToken.getFamilyId());
            throw new UnauthorizedException("Refresh token reuse detected. Please log in again.");
        }

        if (!oldToken.getExpiresAt().isAfter(Instant.now())) {
            oldToken.setRevoked(true);
            refreshTokenRepository.save(oldToken);
            throw new UnauthorizedException("Refresh token has expired");
        }

        User user = oldToken.getUser();

        // Generate the replacement token.
        String newRawToken = helper.generateOpaqueToken();
        String newHash = helper.hashToken(newRawToken);

        // Revoke the old token and link it to the replacement.
        oldToken.setRevoked(true);
        oldToken.setReplacedByHash(newHash);
        refreshTokenRepository.save(oldToken);

        RefreshToken replacement = RefreshToken.builder()
                .user(user)
                .tokenHash(newHash)
                .familyId(oldToken.getFamilyId())
                .expiresAt(Instant.now().plus(refreshExpirationDays, ChronoUnit.DAYS))
                .revoked(false)
                .build();

        refreshTokenRepository.save(replacement);

        // Generate a new access JWT using the user's current role.
        String jwt = jwtUtils.generateTokenFromEmail(
                user.getEmail(),
                user.getId(),
                user.getUserRole().name()
        );

        AuthResponse authResponse = AuthResponse.builder()
                .jwt(jwt)
                .userDto(UserMapper.toDto(user))
                .title("Welcome " + user.getFullName())
                .message("Session refreshed")
                .build();

        return new RefreshResult(
                authResponse,
                newRawToken
        );
    }

    // LOGOUT
    @Override
    @Transactional
    public void logout(String rawToken) {
        if (rawToken == null || rawToken.isBlank()) {
            return;
        }

        String tokenHash = helper.hashToken(rawToken);
        refreshTokenRepository.findByTokenHash(helper.hashToken(tokenHash))
                .ifPresent(token -> {
                    refreshTokenRepository.revokeFamily(token.getFamilyId());
                });

    }


    // CHANGE PASSWORD
    @Override
    @Transactional
    public void changePassword(String email, String oldPassword, String newPassword) {
        User user = helper.findUserByEmail(email);

        // check old password
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new UnauthorizedException("Old password is incorrect");
        }

        if (newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("New password is required");
        }

        // set new password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        refreshTokenRepository.revokeAllByUserId(user.getId());
    }

    // FORGOT PASSWORD
    @Override
    @Transactional
    public void forgotPassword(String email, String baseUrl) {
        User user = helper.findUserByEmail(email);

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusMinutes(15))
                .build();

        passwordResetTokenRepository.save(resetToken);

        String resetUrl = baseUrl + "/auth/reset-password?token="+token;

        try{
            emailUtil.sendResetMail(user.getEmail(), resetUrl);
        }
        catch (Exception e){
            throw new IllegalArgumentException("Password reset email could not be sent", e);
        }
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid password reset token"));

        if(resetToken.getExpiryDate().isBefore(LocalDateTime.now())){
            passwordResetTokenRepository.delete(resetToken);
            throw new UnauthorizedException("Password reset token has expired");
        }

        if (newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("New password is required");
        }

        User user = resetToken.getUser();

        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);
        passwordResetTokenRepository.delete(resetToken);

        // Revoke all refresh sessions after a password reset.
        refreshTokenRepository.revokeAllByUserId(user.getId());
    }




//    private Authentication authentication(String email, String password){
//        UserDetails userDetails = customUserDetailService.loadUserByUsername(email);
//        if(!passwordEncoder.matches(password, userDetails.getPassword())){
//            throw new BadCredentialsException("Invalid password");
//        }
//        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//    }




}
