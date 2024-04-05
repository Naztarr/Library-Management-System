package com.naz.libManager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naz.libManager.dto.LoginDto;
import com.naz.libManager.dto.SignupDto;
import com.naz.libManager.enums.Role;
import com.naz.libManager.payload.ApiResponse;
import com.naz.libManager.payload.LoginResponse;
import com.naz.libManager.service.serviceImplementation.AuthImplementation;
import com.naz.libManager.service.serviceImplementation.JwtImplementation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AuthController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class AuthControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    AuthImplementation authImplementation;

    @MockBean
    JwtImplementation jwtImplementation;

    @MockBean
    UserDetailsService userDetailsService;


    @Test
    void createUser() throws Exception {
        SignupDto signupDto = new SignupDto("username", "password", "Naz@gmail.com",
                "08076678435","Naza@123", "Naza@123", Role.PATRON);

        ApiResponse<String> apiResponse = new ApiResponse<>("User created successfully", HttpStatus.OK);

        when(authImplementation.signup(any(SignupDto.class))).thenReturn(ResponseEntity.ok(apiResponse));

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(signupDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("User created successfully"));

        verify(authImplementation).signup(any(SignupDto.class));
    }

    @Test
    void loginUser() throws Exception {
        LoginDto loginDto = new LoginDto("Naz@gmail.com", "Naza@321");

        ApiResponse<LoginResponse> apiResponse = new ApiResponse<>(new LoginResponse("Naza", "Star",
                "Naz@gmail.com", "token"), "login successful");

        when(authImplementation.login(any(LoginDto.class))).thenReturn(ResponseEntity.ok(apiResponse));

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.token").value("token"));

        verify(authImplementation).login(any(LoginDto.class));
    }
}