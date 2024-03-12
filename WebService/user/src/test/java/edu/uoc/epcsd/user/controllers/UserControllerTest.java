package edu.uoc.epcsd.user.controllers;

import edu.uoc.epcsd.user.config.ClientUtils;
import edu.uoc.epcsd.user.controllers.dtos.CreateUserRequest;
import edu.uoc.epcsd.user.controllers.dtos.GetUserResponse;
import edu.uoc.epcsd.user.controllers.dtos.LoginRequest;
import edu.uoc.epcsd.user.entities.User;
import edu.uoc.epcsd.user.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserControllerTest {
    @Mock
    private  UserService userService;
    @Mock
    private  ClientUtils clientUtils;
    @InjectMocks
    private UserController userController;

    private List<User> mockUsers;

    private User mockUser;
    private CreateUserRequest updateUserRequest;
    private CreateUserRequest createUserRequest;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockUser = User.builder().id(1L).
                fullName("John Doe").
                email("john@example.com").
                password("password").
                phoneNumber("99999999").
                role("USER").
                build();
        mockUsers = Arrays.asList(
                mockUser,
                User.builder().id(2L).fullName("Jane Smith").email("jane@example.com").build()
        );
        updateUserRequest = new CreateUserRequest(mockUser.getPassword(),mockUser.getEmail(),mockUser.getFullName(),mockUser.getPhoneNumber(),mockUser.getRole());
        createUserRequest = new CreateUserRequest("newPassword","createTest@gmail.com", "Test test","99999999","USER");


    }


    @Test
    void testGetAllUsers_UsersExist_Success() {
        // Arrange
        when(userService.findAll()).thenReturn(mockUsers);

        // Act
        List<User> result = userController.getAllUsers();

        // Assert
        assertEquals(mockUsers, result);
    }

    @Test
    void testGetUserById_UserExists_Success() {
        // Arrange

        GetUserResponse expectedResponse = GetUserResponse.fromDomain(mockUser);

        when(userService.findById(mockUser.getId())).thenReturn(Optional.of(mockUser));

        // Act
        ResponseEntity<GetUserResponse> responseEntity = userController.getUserById(mockUser.getId());

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
    }

    @Test
    void testGetUserById_UserNotFound_NotFound() {
        // Arrange
        Long userId = 13L;

        when(userService.findById(userId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<GetUserResponse> responseEntity = userController.getUserById(userId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }

    @Test
    void testUpdateUser_UserUpdatedSuccessfully_Success() {
        // Arrange
        when(userService.updateUser(1L, updateUserRequest.getEmail(), updateUserRequest.getFullName(), updateUserRequest.getPhoneNumber()))
                .thenReturn(mockUser);

        // Act
        ResponseEntity<Object> responseEntity = userController.updateUser(1L, updateUserRequest);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockUser, responseEntity.getBody());
    }

    @Test
    void testUpdateUser_UserNotFound_NotFound() {
        // Arrange
        when(userService.updateUser(3L, updateUserRequest.getEmail(), updateUserRequest.getFullName(), updateUserRequest.getPhoneNumber()))
                .thenThrow(new EntityNotFoundException("User not found"));
        // Act
        ResponseEntity<Object> responseEntity = userController.updateUser(3L, updateUserRequest);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("User not found", responseEntity.getBody());
    }

    @Test
    void testUpdateUser_DataIntegrityViolation_BadRequest() {
        // Arrange
        when(userService.updateUser(2L, updateUserRequest.getEmail(), updateUserRequest.getFullName(), updateUserRequest.getPhoneNumber()))
                .thenThrow(new DataIntegrityViolationException("Data integrity violation"));

        // Act
        ResponseEntity<Object> responseEntity = userController.updateUser(2L, updateUserRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Update Error: Data integrity violation", responseEntity.getBody());
    }

    @Test
    void testUpdateUser_ExceptionThrown_InternalServerError() {
        // Arrange

        when(userService.updateUser(1L, updateUserRequest.getEmail(), updateUserRequest.getFullName(), updateUserRequest.getPhoneNumber()))
                .thenThrow(new RuntimeException("Simulated error"));

        // Act
        ResponseEntity<Object> responseEntity = userController.updateUser(1L, updateUserRequest);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Ocurrió un error inesperado", responseEntity.getBody());
    }

    @Test
    void testGetUsersToAlert_UsersExist_Success() {
        // Arrange
        Long productId = 1L;
        LocalDate availableOnDate = LocalDate.now();
        GetUserResponse[] expectedResponses = mockUsers.stream().map(GetUserResponse::fromDomain).toArray(GetUserResponse[]::new);
        Collections.reverse(Arrays.asList(expectedResponses));
        Set<User> userSet = new HashSet<>(mockUsers);
        when(userService.getUsersToAlert(productId, availableOnDate)).thenReturn(userSet);

        // Act
        ResponseEntity<GetUserResponse[]> responseEntity = userController.getUsersToAlert(productId, availableOnDate);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertArrayEquals(expectedResponses, responseEntity.getBody());
    }


    @Test
    void testCreateUser_Success() {
        // Arrange
        User createdUser = User.builder().id(1L).
                password("newPassword").
                email("createTest@gmail.com").
                fullName("Test test").phoneNumber("99999999").role("USER").build();
////
        when(userService.createUser(createUserRequest.getEmail(), createUserRequest.getPassword(), createUserRequest.getFullName(), createUserRequest.getPhoneNumber()))
                .thenReturn(createdUser);

        // Act
        ResponseEntity<Object> responseEntity = userController.createUser(createUserRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(createdUser.getId(), responseEntity.getBody());
    }

    @Test
    void testCreateUser_DataIntegrityViolation_BadRequest() {
        // Arrange

        when(userService.createUser(createUserRequest.getEmail(), createUserRequest.getPassword(), createUserRequest.getFullName(), createUserRequest.getPhoneNumber()))
                .thenThrow(new DataIntegrityViolationException("Data integrity violation"));

        // Act
        ResponseEntity<Object> responseEntity = userController.createUser(createUserRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().toString().contains("Data integrity violation"));
    }

    @Test
    void testCreateUser_ExceptionThrown_InternalServerError() {
        // Arrange

        when(userService.createUser(createUserRequest.getEmail(), createUserRequest.getPassword(), createUserRequest.getFullName(), createUserRequest.getPhoneNumber()))
                .thenThrow(new RuntimeException("Simulated error"));

        // Act
        ResponseEntity<Object> responseEntity = userController.createUser(createUserRequest);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("An unexpected error occurred", responseEntity.getBody());
    }

    @Test
    void testSetAdmin_Success() {
        // Arrange
        String email = "john@example.com";
        User updatedUser = User.builder().id(1L).fullName("John Doe").email("john@example.com").role("ADMIN").build();

        when(userService.setAdmin(email)).thenReturn(updatedUser);

        // Act
        ResponseEntity<Object> responseEntity = userController.setAdmin(email);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedUser, responseEntity.getBody());
    }

    @Test
    void testSetAdmin_UserNotFound_NotFound() {
        // Arrange
        String email = "john@example.com";

        when(userService.setAdmin(email)).thenThrow(new EntityNotFoundException("User not found"));

        // Act
        ResponseEntity<Object> responseEntity = userController.setAdmin(email);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("User not found", responseEntity.getBody());
    }

    @Test
    void testSetAdmin_DataIntegrityViolation_BadRequest() {
        // Arrange
        String email = "john@example.com";

        when(userService.setAdmin(email)).thenThrow(new DataIntegrityViolationException("Data integrity violation"));

        // Act
        ResponseEntity<Object> responseEntity = userController.setAdmin(email);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().toString().contains("Data integrity violation"));
    }

    @Test
    void testSetAdmin_ExceptionThrown_InternalServerError() {
        // Arrange
        String email = "john@example.com";

        when(userService.setAdmin(email)).thenThrow(new RuntimeException("Simulated error"));

        // Act
        ResponseEntity<Object> responseEntity = userController.setAdmin(email);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Ocurrió un error inesperado", responseEntity.getBody());
    }


    @Test
    void testSendChangePasswordEmail_Success() {
        // Arrange
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("token", "validToken");

        when(userService.sendEmailPassword("validToken")).thenReturn(true);

        // Act
        ResponseEntity<Object> responseEntity = userController.sendChangePasswordEmail(requestBody);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Email enviado \nPor favor, Revisa tu correo electronico para cambiar la contraseña", responseEntity.getBody());
    }

    @Test
    void testSendChangePasswordEmail_InvalidToken_BadRequest() {
        // Arrange
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("token", "invalidToken");

        when(userService.sendEmailPassword("invalidToken")).thenReturn(false);

        // Act
        ResponseEntity<Object> responseEntity = userController.sendChangePasswordEmail(requestBody);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Error al procesar la solicitud, porfavor contacta con el administrador", responseEntity.getBody());
    }

    @Test
    void testSendChangePasswordEmail_ExceptionThrown_InternalServerError() {
        // Arrange
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("token", "validToken");

        when(userService.sendEmailPassword("validToken")).thenThrow(new RuntimeException("Simulated error"));

        // Act
        ResponseEntity<Object> responseEntity = userController.sendChangePasswordEmail(requestBody);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("An unexpected error occurred: Simulated error", responseEntity.getBody());
    }


    @Test
    void testUnSafeSendChangePasswordEmail_Success() {
        // Arrange
        String email = "test@example.com";
        HttpServletRequest request = mock(HttpServletRequest.class);

        // Mocking behavior for successful execution
        when(clientUtils.getClientIpAddress(request)).thenReturn("127.0.0.1");
        when(userService.sendPasswordResetEmailUnsafe(email, "127.0.0.1")).thenReturn(true);

        // Act
        ResponseEntity<Object> responseEntity = userController.sendChangePasswordEmail(email, request);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Email enviado \nPor favor, Revisa tu correo electronico para cambiar la contraseña", responseEntity.getBody());
    }

    @Test
    void testUnSafeSendChangePasswordEmail_Failure() {
        // Arrange
        String email = "test@example.com";
        HttpServletRequest request = mock(HttpServletRequest.class);

        // Mocking behavior for failure
        when(clientUtils.getClientIpAddress(request)).thenReturn("127.0.0.1");
        when(userService.sendPasswordResetEmailUnsafe(email, "127.0.0.1")).thenReturn(false);

        // Act
        ResponseEntity<Object> responseEntity = userController.sendChangePasswordEmail(email, request);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Email enviado \nPor favor, Revisa tu correo electronico para cambiar la contraseña", responseEntity.getBody());
    }



    @Test
    void testChangePasswordByToken_Success() {
        // Arrange
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("token", "validToken");
        requestBody.put("tempCode", "tempCode");
        requestBody.put("newPassword", "newPassword");

        when(userService.updatePasswordByToken("validToken", "newPassword")).thenReturn(true);

        // Act
        ResponseEntity<Object> responseEntity = userController.changePassword(requestBody);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Password cambiado correctamente", responseEntity.getBody());
    }

    @Test
    void testChangePasswordUnsafe_Success() {
        // Arrange
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("safecode", "uuid");
        requestBody.put("email", "john@example.com");
        requestBody.put("newPassword", "newPassword");

        when(userService.updatePasswordUnsafe("uuid", "john@example.com", "newPassword")).thenReturn(true);

        // Act
        ResponseEntity<Object> responseEntity = userController.changePassword(requestBody);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Password modificado", responseEntity.getBody());
    }

    @Test
    void testChangePassword_InvalidNewPassword_BadRequest() {
        // Arrange
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("token", "validToken");
        requestBody.put("tempCode", "tempCode");
        requestBody.put("newPassword", null);

        // Act
        ResponseEntity<Object> responseEntity = userController.changePassword(requestBody);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Error al procesar la solicitud, porfavor contacta con el administrador", responseEntity.getBody());
    }

    @Test
    void testChangePassword_ExceptionThrown_InternalServerError() {
        // Arrange
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("token", "validToken");
        requestBody.put("tempCode", "tempCode");
        requestBody.put("newPassword", "newPassword");

        when(userService.updatePasswordByToken("validToken", "newPassword")).thenThrow(new RuntimeException("Simulated error"));

        // Act
        ResponseEntity<Object> responseEntity = userController.changePassword(requestBody);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("An unexpected error occurred: Simulated error", responseEntity.getBody());
    }

}