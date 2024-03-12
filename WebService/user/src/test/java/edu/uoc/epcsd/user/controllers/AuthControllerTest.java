package edu.uoc.epcsd.user.controllers;


import edu.uoc.epcsd.user.config.ErrorResponse;
import edu.uoc.epcsd.user.controllers.dtos.LoginRequest;
import edu.uoc.epcsd.user.controllers.dtos.UserResponse;
import edu.uoc.epcsd.user.entities.User;
import edu.uoc.epcsd.user.services.TokenService;
import edu.uoc.epcsd.user.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class AuthControllerTest {
    @Mock
    private TokenService tokenService;
    @Mock
    private  UserService userService;
    @Mock
    private  PasswordEncoder passwordEncoder;
    @InjectMocks
    private AuthController authController;
    @Mock
    private AuthenticationManager authenticationManager;

    private LoginRequest loginRequest;
    private User mockUser;
    private String mockToken;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        loginRequest = new LoginRequest();
        mockUser = User.builder()
                .id(1L)
                .fullName("John Doe")
                .email("test@example.com")
                .phoneNumber("123456789")
                .role("USER")
                .password("password").active(1)
                .build();
        loginRequest.setEmail(mockUser.getEmail());
        loginRequest.setPassword(mockUser.getPassword());
        mockToken = "mockToken";

    }




    @Test
    void testLogin_ValidCredentials_Success() {
        // Arrange

        when(userService.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("password", "password")).thenReturn(true);
        when(tokenService.generateToken(mockUser)).thenReturn(mockToken);

        // Act
        ResponseEntity<Object> responseEntity = authController.login(loginRequest);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockToken, responseEntity.getBody());
    }

    @Test
    void testLogin_InvalidPassword_Unauthorized() {
        // Arrange

        when(userService.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("incorrectPassword", "password")).thenReturn(false);

        // Act
        ResponseEntity<Object> responseEntity = authController.login(loginRequest);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals("Invalid login", ((ErrorResponse) responseEntity.getBody()).getError());
    }

    @Test
    void testLogin_InactiveAccount_Forbidden() {
        // Arrange
        mockUser.setActive(0);
        when(userService.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));

        // Act
        ResponseEntity<Object> responseEntity = authController.login(loginRequest);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        assertEquals("Account not activated. Please activate your account.", ((ErrorResponse) responseEntity.getBody()).getError());
    }

    @Test
    void testLogin_UserNotFound_NotFound() {
        // Arrange


        when(userService.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Object> responseEntity = authController.login(loginRequest);

        // Assert
        assertEquals(HttpStatus.FAILED_DEPENDENCY, responseEntity.getStatusCode());
        assertEquals("Invalid login broken", ((ErrorResponse) responseEntity.getBody()).getError());
    }

    @Test
    void testLogin_UnexpectedError_InternalServerError() {
        // Arrange

        when(userService.findByEmail("test@example.com")).thenThrow(new RuntimeException("Simulated error"));

        // Act
        ResponseEntity<Object> responseEntity = authController.login(loginRequest);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Unexpected error occurred", ((ErrorResponse) responseEntity.getBody()).getError());
    }






    @Test
    void testLoginUser_ValidCredentials_Success() {
        // Arrange


        when(userService.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));

        // Act
        ResponseEntity<Object> responseEntity = authController.loginUser(loginRequest);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        UserResponse userResponse = (UserResponse) responseEntity.getBody();
        assertNotNull(userResponse);
        assertEquals(1L, userResponse.getId());
        assertEquals("John Doe", userResponse.getFullName());
        assertEquals("test@example.com", userResponse.getEmail());
        assertEquals("123456789", userResponse.getPhoneNumber());
        assertEquals("USER", userResponse.getRole());
    }

    @Test
    void testLoginUser_InvalidCredentials_Unauthorized() {
        // Arrange


        when(userService.findByEmail("test@example.com")).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Object> responseEntity = authController.loginUser(loginRequest);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals("Invalid credentials", ((ErrorResponse) responseEntity.getBody()).getError());
    }

    @Test
    void testLoginUser_ExceptionThrown_Unauthorized() {
        // Arrange


        when(userService.findByEmail("test@example.com")).thenThrow(new RuntimeException("Simulated error"));

        // Act
        ResponseEntity<Object> responseEntity = authController.loginUser(loginRequest);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals("Invalid credentials", ((ErrorResponse) responseEntity.getBody()).getError());
    }


    @Test
    void testGetAllAdminUsers_AdminUsersExist_Success() {
        // Arrange
        List<Integer> mockAdminUsers = Arrays.asList(1, 2, 3);

        when(userService.findAllAdminUsers()).thenReturn(mockAdminUsers);

        // Act
        ResponseEntity<List<Integer>> responseEntity = authController.getAllAdminUsers();

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockAdminUsers, responseEntity.getBody());
    }

    @Test
    void testGetAllAdminUsers_NoAdminUsers_NoContent() {
        // Arrange
        when(userService.findAllAdminUsers()).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<Integer>> responseEntity = authController.getAllAdminUsers();

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }


    @Test
    void testConfirmUser_UserActivation_Success() {
        // Arrange
        Long userId = 1L;

        when(userService.activateUser(userId)).thenReturn(true);

        // Act
        ResponseEntity<String> responseEntity = authController.confirmUser(userId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Usuario activado con éxito.", responseEntity.getBody());
    }

    @Test
    void testConfirmUser_UserActivationFailed_BadRequest() {
        // Arrange
        Long userId = 1L;

        when(userService.activateUser(userId)).thenReturn(false);

        // Act
        ResponseEntity<String> responseEntity = authController.confirmUser(userId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("No se pudo activar el usuario.", responseEntity.getBody());
    }


    @Test
    void testResendEmailConfirmation_EmailResentSuccessfully_Success() {
        // Arrange
        String userEmail = "test@example.com";

        // Mocking the service to indicate successful email resend
        doNothing().when(userService).resendEmailConfirmation(userEmail);

        // Act
        ResponseEntity<Object> responseEntity = authController.resendEmailConfirmation(userEmail);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Email confirmation resent successfully.", responseEntity.getBody());
    }

    @Test
    void testResendEmailConfirmation_EmailNotSent_BadRequest() {
        // Arrange
        String userEmail = "test@example.com";

        // Mocking the service to indicate failure in email resend
        doThrow(new RuntimeException("Simulated error")).when(userService).resendEmailConfirmation(userEmail);

        // Act
        ResponseEntity<Object> responseEntity = authController.resendEmailConfirmation(userEmail);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Email not sent", responseEntity.getBody());
    }

}