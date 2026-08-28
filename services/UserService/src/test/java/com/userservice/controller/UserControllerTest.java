package com.userservice.controller;

import com.microservices.payload.response.User.UserResponse;
import com.userservice.config.jwt.AuthTokenFilter;
import com.userservice.config.jwt.JwtUtils;
import com.userservice.service.Impl.CustomUserDetailService;
import com.userservice.service.UserService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(
        controllers = UserController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class
)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private AuthTokenFilter authTokenFilter;

    @MockBean
    private CustomUserDetailService customUserDetailService;

    @Test
    @WithMockUser(username = "john@gmail.com", roles = "USER")
    void getUserProfile() throws Exception {
        UserResponse response = UserResponse.builder()
                        .id(1L)
                        .fullName("John Doe")
                        .email("john@gmail.com")
                        .build();

        when(userService.getUserByEmail("john@gmail.com"))
                .thenReturn(response);

        mockMvc.perform(get("/api/users/profile"))
                .andDo(org.springframework.test.web.servlet.result.MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.email")
                        .value("john@gmail.com"));
    }

    @Test
    void testGetUserById() throws Exception {
        UserResponse response = UserResponse.builder()
                        .id(1L)
                        .fullName("John")
                        .email("john@gmail.com")
                        .build();

        when(userService.getUserById(1L))
                .thenReturn(response);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id")
                        .value(1));
    }

    @Test
    @WithMockUser(username = "john@gmail.com", roles = "USER")
    void testGetAllUsers() throws Exception {
        List<UserResponse> users =  List.of(
                UserResponse.builder()
                        .id(1L)
                        .fullName("John")
                        .email("john@gmail.com")
                        .build()
        );

        when(userService.getAllUsers())
                .thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()")
                        .value(1));
    }




}
