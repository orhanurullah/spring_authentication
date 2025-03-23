package com.ortim.security.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ortim.core.utils.RouteConstants;
import com.ortim.model.User;
import com.ortim.repository.UserRepository;
import com.ortim.security.jwt.JwtTokenProvider;
import com.ortim.security.request.LoginRequest;
import com.ortim.security.request.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        // Mock davranışlarını başlangıçta ayarla
    }

    @Test
    void shouldRegisterUserSuccessfully() throws Exception {
        // Arrange
        RegisterRequest mockRequest = RegisterRequest.builder()
                .email("abcdyyezigabl@gmail.com")
                .password("password123")
                .build();

        String userJson = objectMapper.writeValueAsString(mockRequest);

        mockMvc.perform(post(RouteConstants.authRegisterRoute)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    System.out.println(result.getResponse().getContentAsString());
                });
    }

    @Test
    void shouldAuthenticateUserSuccessfully() throws Exception {
        // Arrange
        LoginRequest loginRequest = LoginRequest.builder()
                .email("abcdyyezigabl@gmail.com")
                .password("password123")
                .build();

        String loginJson = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(post(RouteConstants.authLoginRoute)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    System.out.println(result.getResponse().getContentAsString());
                });
    }

    @Test
    void shouldActivateEmailSuccessfully() throws Exception {
        // Arrange
        User user = userRepository.findByEmail("abcdyyezigabl@gmail.com").get();
        long expirationTime = 3600000L;
        String token = jwtTokenProvider.generateToken(user, expirationTime);

        mockMvc.perform(get(RouteConstants.authActivationRoute)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("userId", String.valueOf(user.getId()))
                        .param("token", token))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    System.out.println(result.getResponse().getContentAsString());
                });
    }

    @Test
    void shouldResendActivationEmailSuccessfully() throws Exception {
        // Arrange
        String email = "orhann.1512@gmail.com";

        mockMvc.perform(get(RouteConstants.authResendActivationRoute)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("email", email))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    System.out.println(result.getResponse().getContentAsString());
                });
    }

    @Test
    void shouldRefreshTokenSuccessfully() throws Exception {
        // Arrange
        User user = userRepository.findByEmail("orhann.1512@gmail.com").get();
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);
        String bearerToken = "Bearer " + refreshToken;

        // Act & Assert
        mockMvc.perform(post(RouteConstants.authRefreshTokenRoute)
                        .header(HttpHeaders.AUTHORIZATION, bearerToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    System.out.println(result.getResponse().getContentAsString());
                });;
    }
}