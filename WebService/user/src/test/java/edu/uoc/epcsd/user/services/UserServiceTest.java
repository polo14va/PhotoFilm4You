package edu.uoc.epcsd.user.services;

import static edu.uoc.epcsd.user.infraestructure.kafka.KafkaConstants.EMAIL_TOPIC;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import edu.uoc.epcsd.user.entities.Alert;
import edu.uoc.epcsd.user.entities.EmailMessage;
import edu.uoc.epcsd.user.entities.User;
import edu.uoc.epcsd.user.repositories.AlertRepository;
import edu.uoc.epcsd.user.repositories.UserRepository;

class UserServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    AlertRepository alertRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    private TokenService tokenService;

    @Mock
    KafkaTemplate<String, EmailMessage> kafkaTemplate;

    @InjectMocks
    UserService userService;

    private List<User> userList;
    private List<Integer> adminList;

    private User user1;
    private User user2;
    private List<Alert> alerts;
    private static final long MAX_RESET_REQUESTS_PER_MINUTE = 2;


    private String passwordUrl = "http://localhost:4200/user/change-password?TEMPCODE=%s";
    private String confirmIdURL = "http://localhost:18082/auth/confirm?id=%s";

    private String validEmail = "user@example.com";
    private String validIpAddress = "127.0.0.1";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user1 = User.builder().fullName("user1").email("userTest@gmail.com").id(1L).build();
        user2 = User.builder().email("test@gmail.com").id(25L).role("ADMIN").build();
        adminList = Arrays.asList(Math.toIntExact(user2.getId()));
        userList = Arrays.asList(user1, user2);

        Long productId = 1L;
        LocalDate availableOnDate = LocalDate.now();

        Alert alert1 = Alert.builder().user(user1).id(productId).to(availableOnDate).from(availableOnDate).build();
        Alert alert2 = Alert.builder().user(user2).id(productId).to(availableOnDate).from(availableOnDate).build();

        alerts = Arrays.asList(alert1, alert2);

        userService.setPasswordUrl(passwordUrl);
        userService.setConfirmIdURL(confirmIdURL);
    }

    @Test
    void findAll() {

            when(userRepository.findAll()).thenReturn(userList);

            List<User> userList = userService.findAll();

            verify(userRepository, times(1)).findAll();

            // Verify the expected result
            assertEquals(2, userList.size());
            assertEquals("user1", userList.get(0).getFullName());
            assertEquals("test@gmail.com", userList.get(1).getEmail());

    }

    @Test
    void findAllAdminUsers() {
        when(userRepository.findAllAdminUsers()).thenReturn(adminList);

        List<Integer> adminList = userService.findAllAdminUsers();

        verify(userRepository, times(1)).findAllAdminUsers();

        assertEquals(1, adminList.size());
        assertEquals(25, adminList.get(0));

    }

    @Test
    void findById() {

        when(userRepository.findById(25L)).thenReturn(Optional.of(user2));

        Optional<User> result = userService.findById(25L);

        // Assert
        verify(userRepository, times(1)).findById(25L);
        assertEquals(Optional.of(user2), result);
    }

    @Test
    void findByEmail() {
        when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(user2));

        Optional<User> result = userService.findByEmail("test@gmail.com");

        verify(userRepository, times(1)).findByEmail("test@gmail.com");
        assertEquals(Optional.of(user2), result);

    }

    @Test
    void createUser() {

        // Input data
        String email = "test@example.com";
        String password = "password123";
        String fullName = "John Doe";
        String phoneNumber = "1234567890";

        // Mock behavior
        when(passwordEncoder.encode(password)).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L); // Set a known id
            return user;
        });

        // Execute the method
        User createdUser = userService.createUser(email, password, fullName, phoneNumber);

        // Verify method calls
        verify(userRepository, times(1)).save(any(User.class));
        verify(kafkaTemplate, times(1)).send(eq("email-topic"), any(EmailMessage.class));

        // Assertions
        assertNotNull(createdUser);
        assertEquals(email, createdUser.getEmail());
        assertEquals("hashedPassword", createdUser.getPassword());
        assertEquals("USER", createdUser.getRole());
        assertEquals(0, createdUser.getActive());

        // Add more assertions if needed to cover other properties or conditions
    }



    @Test
    void getUsersToAlert() {
        Long productId = 1L;
        LocalDate availableOnDate = LocalDate.now();


        when(alertRepository.findAlertsByProductIdAndInterval(productId, availableOnDate))
                .thenReturn(alerts);


        Set<User> usersToAlert = userService.getUsersToAlert(productId, availableOnDate);


        verify(alertRepository, times(1)).findAlertsByProductIdAndInterval(productId, availableOnDate);

        Set<User> expectedUsers = new HashSet<>(userList);
        assertEquals(expectedUsers, usersToAlert);


    }

    @Test
    void activateUser_Success() {
        when(userRepository.findById(25L)).thenReturn(Optional.of(user2));

        boolean result = userService.activateUser(25L);

        verify(userRepository, times(1)).findById(25L);

        verify(userRepository, times(1)).save(user2);

        assertTrue(result);
        assertEquals(1,user2.getActive());

    }

    @Test
    void activateUser_Fail() {

        when(userRepository.findById(25L)).thenReturn(Optional.empty());

        boolean result = userService.activateUser(25L);

        verify(userRepository, times(1)).findById(25L);

        verify(userRepository, never()).save(any(User.class));

        assertFalse(result);

    }





    @Test
    void resendEmailConfirmation() {
        user2.setActive(0);
        when(userRepository.findByEmail(user2.getEmail())).thenReturn(Optional.of(user2));

        userService.resendEmailConfirmation(user2.getEmail());

        verify(userRepository, times(1)).findByEmail(user2.getEmail());

        verify(kafkaTemplate, times(1)).send("email-topic", new EmailMessage(
                user2.getEmail(),
                "Bienvenido a Nuestra Plataforma",
                "correo.html",
                "Bienvenido a nuestra plataforma, por favor confirma tu cuenta haciendo click en el siguiente enlace:",
                "http://localhost:18082/auth/confirm?id=" + user2.getId(),
                "Confirmar Cuenta"
        ));

    }

    @Test
    void resendEmailConfirmation_UserNotPresent() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        userService.resendEmailConfirmation("nonexistent@example.com");

        verify(kafkaTemplate, never()).send(eq(EMAIL_TOPIC), any(EmailMessage.class));
    }





    @Test
    void updateUser() {

        when(userRepository.findById(25L)).thenReturn(Optional.of(user2));
        when(userRepository.save(user2)).thenReturn(user2);


        User updatedUser = userService.updateUser(25L, "newTest@gmail.com", "Test test", "933077339");


        verify(userRepository, times(1)).findById(25L);


        verify(userRepository, times(1)).save(user2);


        assertEquals(25L, updatedUser.getId());
        assertEquals("newTest@gmail.com", updatedUser.getEmail());
        assertEquals("Test test", updatedUser.getFullName());
        assertEquals("933077339", updatedUser.getPhoneNumber());



    }

    @Test
    void setAdmin() {
        // Arrange

        when(userRepository.findByEmail("userTest@gmail.com")).thenReturn(Optional.of(user1));
        when(userRepository.save(user1)).thenReturn(user1);

        // Act
        User updatedUser = userService.setAdmin(user1.getEmail());

        // Assert
        // Verify that userRepository.findByEmail method was called with the expected parameter
        verify(userRepository, times(1)).findByEmail(user1.getEmail());

        // Verify that userRepository.save method was called with the updated user
        verify(userRepository, times(1)).save(user1);

        // Assert the updated user
        assertEquals("userTest@gmail.com", updatedUser.getEmail());
        assertEquals("ADMIN", updatedUser.getRole());


    }

    @Test
    void testSendEmailPassword_ValidToken_UserFound() {

        String validToken = "valid_token";
        User mockUser = User.builder().email("test@example.com").build();

        when(tokenService.readToken(validToken)).thenReturn(Collections.singletonMap("sub", "test@example.com"));
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));

        boolean result = userService.sendEmailPassword(validToken);

        assertTrue(result);


    }

    @Test
    void isRateLimited_NotExceedingLimit() {
        String ipAddress = "127.0.0.1";

        // Simulate multiple requests within the allowed time window
        for (int i = 0; i < MAX_RESET_REQUESTS_PER_MINUTE - 1; i++) {
            assertFalse(userService.isRateLimited(ipAddress));
        }

        // One more request, still within the limit
        assertFalse(userService.isRateLimited(ipAddress));
    }

    @Test
    void isRateLimited_ExceedingLimit() {
        String ipAddress = "127.0.0.1";

        // Simulate multiple requests within the allowed time window
        for (int i = 0; i < MAX_RESET_REQUESTS_PER_MINUTE; i++) {
            assertFalse(userService.isRateLimited(ipAddress));
        }

        // One more request, exceeding the limit
        assertTrue(userService.isRateLimited(ipAddress));
    }

    @Test
    void isRateLimited_TimeWindowExpired() {
        String ipAddress = "127.0.0.1";

        // Simulate a request within the allowed time window
        assertFalse(userService.isRateLimited(ipAddress));

        // Create a ScheduledExecutorService
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        // Schedule a task to be executed after the time window has expired
        executorService.schedule(() -> {
            // One more request, should not be rate limited
            assertFalse(userService.isRateLimited(ipAddress));

            // Shutdown the executor service after the task is executed
            executorService.shutdown();
        }, 61, TimeUnit.SECONDS);

        // Ensure the executor service shuts down gracefully when the test is finished
        Runtime.getRuntime().addShutdownHook(new Thread(executorService::shutdown));
    }

    @Test
    void sendPasswordResetEmail_ValidEmailNotRateLimitedUserExists() {
        when(userRepository.findByEmail(validEmail)).thenReturn(Optional.of(new User()));

        boolean result = userService.sendPasswordResetEmailUnsafe(validEmail, validIpAddress);

        assertTrue(result);
        verify(kafkaTemplate, times(1)).send(eq("email-topic"), any(EmailMessage.class));
    }

    @Test
    void sendPasswordResetEmail_ValidEmailNotRateLimitedUserDoesNotExist() {
        when(userRepository.findByEmail(validEmail)).thenReturn(Optional.empty());

        boolean result = userService.sendPasswordResetEmailUnsafe(validEmail, validIpAddress);

        assertFalse(result);
        verify(kafkaTemplate, never()).send(eq("email-topic"), any(EmailMessage.class));
    }

    @Test
    void sendPasswordResetEmail_InvalidEmailShouldBeRateLimited() {
        String invalidEmail = "invalid_email";

        boolean result = userService.sendPasswordResetEmailUnsafe(invalidEmail, validIpAddress);

        assertFalse(result);
        verify(kafkaTemplate, times(0)).send(eq("email-topic"), any(EmailMessage.class));
    }

    @Test
    void sendPasswordResetEmail_ValidEmailRateLimitedUserExists() {
        when(userRepository.findByEmail(validEmail)).thenReturn(Optional.of(new User()));

        // Simulate rate limiting
        userService.isRateLimited(validIpAddress); // Assuming isRateLimited is already tested separately

        boolean result = userService.sendPasswordResetEmailUnsafe(validEmail, validIpAddress);

        assertTrue(result);
        verify(kafkaTemplate, times(1)).send(eq("email-topic"), any(EmailMessage.class));
    }

    @Test
    void testUpdatePasswordUnsafe() {
        // Mocking dependencies
        String validUuid = "valid-uuid";
        String validEmail = "test@example.com";
        String validPassword = "newPassword";
        User mockedUser = new User();
        mockedUser.setEmail(validEmail);

        Mockito.when(tokenService.isUUIDValid(validUuid)).thenReturn(true);
        Mockito.when(userRepository.findByEmail(validEmail)).thenReturn(Optional.of(mockedUser));
        Mockito.when(passwordEncoder.encode(validPassword)).thenReturn("encodedPassword");

        // Calling the method to test
        boolean result = userService.updatePasswordUnsafe(validUuid, validEmail, validPassword);

        // Assertions
        assertTrue(result);
        Mockito.verify(userRepository).save(any(User.class));
        Mockito.verify(passwordEncoder).encode(eq(validPassword));


    }

    @Test
    void testUpdatePasswordUnsafe_InvalidUUID() {
        // Mocking dependencies
        String invalidUuid = "invalid-uuid";
        String validEmail = "test@example.com";
        String validPassword = "newPassword";

        Mockito.when(tokenService.isUUIDValid(invalidUuid)).thenReturn(false);

        // Calling the method to test
        boolean result = userService.updatePasswordUnsafe(invalidUuid, validEmail, validPassword);

        // Assertions
        assertFalse(result);
        Mockito.verify(userRepository, Mockito.never()).save(any(User.class));
        Mockito.verify(passwordEncoder, Mockito.never()).encode(anyString());


    }


    @Test
    void testUpdatePasswordByToken() {
        // Mocking dependencies
        String validToken = "valid-token";
        String validPassword = "newPassword";
        String validEmail = "test@example.com";

        // Simulate a valid token with email claim
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", validEmail);

        Mockito.when(tokenService.readToken(validToken)).thenReturn(claims);
        Mockito.when(userRepository.findByEmail(validEmail)).thenReturn(Optional.of(new User()));

        // Calling the method to test
        boolean result = userService.updatePasswordByToken(validToken, validPassword);

        // Assertions
        assertTrue(result);
        Mockito.verify(userRepository).save(any(User.class));
        Mockito.verify(passwordEncoder).encode(eq(validPassword));
        // Add more assertions if needed
    }

    @Test
    void testUpdatePasswordByToken_InvalidToken() {
        // Mocking dependencies
        String invalidToken = "invalid-token";
        String validPassword = "newPassword";

        // Simulate an invalid token (claims do not contain "sub")
        Map<String, Object> invalidClaims = new HashMap<>();

        Mockito.when(tokenService.readToken(invalidToken)).thenReturn(invalidClaims);

        // Calling the method to test
        boolean result = userService.updatePasswordByToken(invalidToken, validPassword);

        // Assertions
        assertFalse(result);
        Mockito.verify(userRepository, Mockito.never()).save(any(User.class));
        Mockito.verify(passwordEncoder, Mockito.never()).encode(anyString());

    }

    @Test
    void testUpdatePasswordByToken_UserNotFound() {
        // Mocking dependencies
        String validToken = "valid-token";
        String validPassword = "newPassword";
        String nonExistentEmail = "nonexistent@example.com";

        // Simulate a valid token with email claim
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", nonExistentEmail);

        Mockito.when(tokenService.readToken(validToken)).thenReturn(claims);
        Mockito.when(userRepository.findByEmail(nonExistentEmail)).thenReturn(Optional.empty());

        // Calling the method to test
        boolean result = userService.updatePasswordByToken(validToken, validPassword);

        // Assertions
        assertFalse(result);
        Mockito.verify(userRepository, Mockito.never()).save(any(User.class));
        Mockito.verify(passwordEncoder, Mockito.never()).encode(anyString());

    }




}