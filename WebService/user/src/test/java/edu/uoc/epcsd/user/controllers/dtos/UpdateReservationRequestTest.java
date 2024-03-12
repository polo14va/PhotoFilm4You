package edu.uoc.epcsd.user.controllers.dtos;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UpdateReservationRequestTest {

    @Test
    void testGetterMethods() {
        // Arrange
        Long id = 1L;
        Long userId = 2L;
        Long productId = 3L;
        int quantity = 4;

        // Act
        UpdateReservationRequest request = new UpdateReservationRequest(id, userId, productId, quantity);

        // Assert
        assertEquals(id, request.getId());
        assertEquals(userId, request.getUserId());
        assertEquals(productId, request.getProductId());
        assertEquals(quantity, request.getQuantity());
    }

    @Test
    void testAllArgsConstructor() {
        // Arrange
        Long id = 1L;
        Long userId = 2L;
        Long productId = 3L;
        int quantity = 4;

        // Act
        UpdateReservationRequest request = new UpdateReservationRequest(id, userId, productId, quantity);

        // Assert
        assertEquals(id, request.getId());
        assertEquals(userId, request.getUserId());
        assertEquals(productId, request.getProductId());
        assertEquals(quantity, request.getQuantity());
    }
}