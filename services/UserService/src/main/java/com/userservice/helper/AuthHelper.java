package com.userservice.helper;


import com.airlineportal.exception.ResourceNotFoundException;
import com.userservice.model.User;
import com.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthHelper {
    private final UserRepository userRepository;

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(normalizeEmail(email))
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    public String normalizeEmail(String email) {
        if(email == null)
            return null;

        return email.trim().toLowerCase();
    }


}
