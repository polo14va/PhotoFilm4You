package edu.uoc.epcsd.notification.application.dtos;

import edu.uoc.epcsd.notification.application.rest.dtos.GetProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class GetProductResponseTest {

    private GetProductResponse getProductResponse;

    @BeforeEach
    void setUp() {
        getProductResponse = new GetProductResponse();
    }

    @Test
    void settingIdReflectsInGetter() {
        Long expectedId = 123L;
        getProductResponse.setId(expectedId);
        assertEquals(expectedId, getProductResponse.getId());
    }

    @Test
    void settingDifferentIdReflectsInGetter() {
        Long unexpectedId = 123L;
        Long expectedId = 456L;
        getProductResponse.setId(expectedId);
        assertNotEquals(unexpectedId, getProductResponse.getId());
    }

    @Test
    void settingNameReflectsInGetter() {
        String expectedName = "Product Name";
        getProductResponse.setName(expectedName);
        assertEquals(expectedName, getProductResponse.getName());
    }

    @Test
    void settingDifferentNameReflectsInGetter() {
        String unexpectedName = "Old Product Name";
        String expectedName = "New Product Name";
        getProductResponse.setName(expectedName);
        assertNotEquals(unexpectedName, getProductResponse.getName());
    }

    @Test
    void testGetProductResponseToString() {
        GetProductResponse productResponse = GetProductResponse.builder()
                .id(1L)
                .name("Product 1")
                .description("Description of Product 1")
                .dailyPrice(19.99)
                .brand("Brand 1")
                .model("Model A")
                .categoryId(101L)
                .build();

        String expectedToString = "GetProductResponse(id=1, name=Product 1, description=Description of Product 1, dailyPrice=19.99, brand=Brand 1, model=Model A, categoryId=101)";
        assertEquals(expectedToString, productResponse.toString());
    }
}