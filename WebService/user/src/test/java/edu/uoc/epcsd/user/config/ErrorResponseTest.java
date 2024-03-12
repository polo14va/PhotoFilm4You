package edu.uoc.epcsd.user.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    void testSetError() {
        // Arrange
        String initialError = "Initial error";
        ErrorResponse errorResponse = new ErrorResponse(initialError);

        // Act
        String newError = "New error";
        errorResponse.setError(newError);

        // Assert
        assertEquals(newError, errorResponse.getError(), "Error should be updated");
    }
}