package edu.uoc.epcsd.user.controllers.dtos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class CreateUserRequestTest {

    @Test
    void testGettersAndSetters() {
        // Arrange
        String password = "password123";
        String email = "test@example.com";
        String fullName = "John Doe";
        String phoneNumber = "1234567890";
        String role = "USER";

        // Act
        CreateUserRequest userRequest = new CreateUserRequest(password, email, fullName, phoneNumber, role);

        // Assert
        assertEquals(password, userRequest.getPassword());
        assertEquals(email, userRequest.getEmail());
        assertEquals(fullName, userRequest.getFullName());
        assertEquals(phoneNumber, userRequest.getPhoneNumber());
        assertEquals(role, userRequest.getRole());


    }

    @Test
    void testNoArgsConstructor() {
        // Act
        CreateUserRequest userRequest = new CreateUserRequest();

        // Assert
        assertNotNull(userRequest);
        // Check that fields are initialized (non-null) due to @NoArgsConstructor(force = true)
        assertNull(userRequest.getPassword());
        assertNull(userRequest.getEmail());
        assertNull(userRequest.getFullName());
        assertNull(userRequest.getPhoneNumber());
        assertNull(userRequest.getRole()); // Null for non-final fields without a default value
    }

    @Test
    void testAllArgsConstructor() {
        // Arrange
        String password = "password123";
        String email = "test@example.com";
        String fullName = "John Doe";
        String phoneNumber = "1234567890";
        String role = "USER";

        // Act
        CreateUserRequest userRequest = new CreateUserRequest(password, email, fullName, phoneNumber, role);

        // Assert
        assertNotNull(userRequest);
        assertEquals(password, userRequest.getPassword());
        assertEquals(email, userRequest.getEmail());
        assertEquals(fullName, userRequest.getFullName());
        assertEquals(phoneNumber, userRequest.getPhoneNumber());
        assertEquals(role, userRequest.getRole());
    }
}