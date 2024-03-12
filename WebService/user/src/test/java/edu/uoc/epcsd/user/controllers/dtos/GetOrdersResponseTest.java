package edu.uoc.epcsd.user.controllers.dtos;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
class GetOrdersResponseTest {


    @Test
    void testBuilder() {
        // Given
        Long userId = 1L;
        GetProductResponse product = new GetProductResponse(/* provide product details */);
        int quantity = 2;
        double price = 20.0;
        double tax = 2.0;
        long rentId = 101L;
        long orderId = 201L;

        // When
        GetOrdersResponse response = GetOrdersResponse.builder()
                .userId(userId)
                .product(product)
                .quantity(quantity)
                .price(price)
                .tax(tax)
                .rentId(rentId)
                .orderId(orderId)
                .build();

        // Then
        assertEquals(userId, response.getUserId());
        assertEquals(product, response.getProduct());
        assertEquals(quantity, response.getQuantity());
        assertEquals(price, response.getPrice());
        assertEquals(tax, response.getTax());
        assertEquals(rentId, response.getRentId());
        assertEquals(orderId, response.getOrderId());
    }

    @Test
    void testFromDomain() {
        // Given
        Long userId = 1L;
        GetProductResponse productResponse = new GetProductResponse(/* provide product details */);
        int quantity = 2;
        double price = 20.0;
        double tax = 2.0;
        long rentId = 101L;
        long orderId = 201L;

        // When
        GetOrdersResponse response = GetOrdersResponse.fromDomain(userId, productResponse, quantity, price, tax, rentId, orderId);

        // Then
        assertEquals(userId, response.getUserId());
        assertEquals(productResponse, response.getProduct());
        assertEquals(quantity, response.getQuantity());
        assertEquals(price, response.getPrice());
        assertEquals(tax, response.getTax());
        assertEquals(rentId, response.getRentId());
        assertEquals(orderId, response.getOrderId());
    }

    @Test
    void testToString() {
        // Given
        GetProductResponse product = new GetProductResponse();
        GetOrdersResponse getOrdersResponse = new GetOrdersResponse(1L,product,1, 1.99, 2,2L, 2L);

        // When
        String toStringResult = getOrdersResponse.toString();

        // Then
        assertNotNull(toStringResult);
    }

    @Test
    void testGetterAndSetter() {
        // Given
        GetProductResponse product = new GetProductResponse();
        GetOrdersResponse getOrdersResponse = new GetOrdersResponse(1L, product,1, 1.99, 2,2L, 2L);

        getOrdersResponse.setTax(40);


        assertEquals(40, getOrdersResponse.getTax());

    }

    @Test
    void testEqualsAndHashCode() {
        // Given

        GetProductResponse product = new GetProductResponse();
        GetProductResponse product2 = new GetProductResponse(2L,"name", "description", 99.00, "brand",
                "model", "category");
        GetOrdersResponse getOrdersResponse1 = new GetOrdersResponse(1L, product,1, 1.99, 2,2L, 2L);


        GetOrdersResponse getOrdersResponse2 = new GetOrdersResponse(1L, product,1, 1.99, 2,2L, 2L);

        GetOrdersResponse getOrdersResponse3 = new GetOrdersResponse(1L, product2,1, 1.99, 2,2L, 2L);

        // When
        // Check equality
        assertEquals(getOrdersResponse1, getOrdersResponse2);
        assertNotEquals(getOrdersResponse1, getOrdersResponse3);

        // Check hash code
        assertEquals(getOrdersResponse1.hashCode(), getOrdersResponse2.hashCode());
        assertNotEquals(getOrdersResponse1.hashCode(), getOrdersResponse3.hashCode());
    }


}