package edu.uoc.epcsd.user.controllers.dtos;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class CreateAlertRequestTest {

    @Test
    void testGetterMethods() {
        // Arrange
        Long productId = 1L;
        Long userId = 2L;
        LocalDate from = LocalDate.now();
        LocalDate to = LocalDate.now().plusDays(1);

        // Act
        CreateAlertRequest request = new CreateAlertRequest(productId, userId, from, to);

        // Assert
        assertEquals(productId, request.getProductId());
        assertEquals(userId, request.getUserId());
        assertEquals(from, request.getFrom());
        assertEquals(to, request.getTo());
    }

    @Test
    void testAllArgsConstructor() {
        // Arrange
        Long productId = 1L;
        Long userId = 2L;
        LocalDate from = LocalDate.now();
        LocalDate to = LocalDate.now().plusDays(1);

        // Act
        CreateAlertRequest request = new CreateAlertRequest(productId, userId, from, to);

        // Assert
        assertEquals(productId, request.getProductId());
        assertEquals(userId, request.getUserId());
        assertEquals(from, request.getFrom());
        assertEquals(to, request.getTo());
    }

    @Test
    void testNonNullAnnotations() {
        // Arrange
        Long productId = 1L;
        Long userId = 2L;
        LocalDate from = LocalDate.now();
        LocalDate to = LocalDate.now().plusDays(1);

        // Act
        assertDoesNotThrow(() -> new CreateAlertRequest(productId, userId, from, to));

        // Assert
        assertThrows(NullPointerException.class, () -> new CreateAlertRequest(null, userId, from, to));
        assertThrows(NullPointerException.class, () -> new CreateAlertRequest(productId, null, from, to));
        assertThrows(NullPointerException.class, () -> new CreateAlertRequest(productId, userId, null, to));
        assertThrows(NullPointerException.class, () -> new CreateAlertRequest(productId, userId, from, null));
    }




}