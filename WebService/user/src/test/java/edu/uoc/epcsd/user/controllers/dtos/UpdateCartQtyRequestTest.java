package edu.uoc.epcsd.user.controllers.dtos;

import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UpdateCartQtyRequestTest {


    @Test
    void testToString() {
        // Arrange
        long cartId = 1L;
        int quantity = 5;
        UpdateCartQtyRequest request = new UpdateCartQtyRequest(cartId, quantity);

        // Act
        String toStringResult = request.toString();

        // Assert
        Assert.hasText(toStringResult);
        assertFalse(toStringResult.isEmpty());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        UpdateCartQtyRequest request1 = new UpdateCartQtyRequest(1L, 5);
        UpdateCartQtyRequest request2 = new UpdateCartQtyRequest(1L, 5);
        UpdateCartQtyRequest request3 = new UpdateCartQtyRequest(2L, 5);

        // Act & Assert
        assertEquals(request1, request2);
        assertNotEquals(request1, request3);
        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1.hashCode(), request3.hashCode());
    }

    @Test
    void testBuilder() {
        // Arrange
        long cartId = 1L;
        int quantity = 5;

        // Act
        UpdateCartQtyRequest request = UpdateCartQtyRequest.builder()
                .cartId(cartId)
                .quantity(quantity)
                .build();

        // Assert
        assertEquals(cartId, request.getCartId());
        assertEquals(quantity, request.getQuantity());
    }

    @Test
    void testNoArgsConstructor() {
        // Act
        UpdateCartQtyRequest request = new UpdateCartQtyRequest();

        // Assert
        assertNotNull(request);
    }

    @Test
    void testAllArgsConstructor() {
        // Arrange
        long cartId = 1L;
        int quantity = 5;

        // Act
        UpdateCartQtyRequest request = new UpdateCartQtyRequest(cartId, quantity);

        // Assert
        assertEquals(cartId, request.getCartId());
        assertEquals(quantity, request.getQuantity());
    }

    @Test
    void testGetterAndSetter() {
        // Arrange
        long cartId = 1L;
        int quantity = 5;
        UpdateCartQtyRequest request = new UpdateCartQtyRequest();

        // Act
        request.setCartId(cartId);
        request.setQuantity(quantity);

        // Assert
        assertEquals(cartId, request.getCartId());
        assertEquals(quantity, request.getQuantity());
    }
}