package edu.uoc.epcsd.user.controllers.dtos;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GetProductResponseTest {

    @Test
    void testBuilder() {
        // Arrange
        Long id = 1L;
        String name = "Product A";
        String description = "Description of Product A";
        Double dailyPrice = 19.99;
        String brand = "Brand X";
        String model = "Model 123";
        String categoryName = "Category ABC";

        // Act
        GetProductResponse product = GetProductResponse.builder()
                .id(id)
                .name(name)
                .description(description)
                .dailyPrice(dailyPrice)
                .brand(brand)
                .model(model)
                .categoryName(categoryName)
                .build();

        // Assert
        assertNotNull(product);
        assertEquals(id, product.getId());
        assertEquals(name, product.getName());
        assertEquals(description, product.getDescription());
        assertEquals(dailyPrice, product.getDailyPrice());
        assertEquals(brand, product.getBrand());
        assertEquals(model, product.getModel());
        assertEquals(categoryName, product.getCategoryName());
    }
}