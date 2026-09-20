package com.userservice.service.Impl;

import com.userservice.helper.UserHelper;
import com.userservice.mapper.UserMapper;
import com.userservice.model.User;
import com.userservice.repository.UserRepository;
import com.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserHelper helper;

    @Override
    public UserResponse getUserByEmail(String email) {
        User user =  helper.findUserByEmail(email);
        return UserMapper.toResponse(user);
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = helper.findUserById(id)
        return UserMapper.toResponse(user);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toResponse)
                .toList();
    }

}
