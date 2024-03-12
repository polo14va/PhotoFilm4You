package edu.uoc.epcsd.productcatalog.application.rest.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class CreateProductRequestUnitTest {


    @Test
    void whenCreateAnItemthenShouldBeReturn() {
        String name = "Item1";

        String description = "Itemdescription";

        Double dailyPrice = 150.5;

        String brand = "Brand";

        String model = "Model";

        Long categoryId = 1L;

        CreateProductRequest createProductRequest = new CreateProductRequest(name,description,dailyPrice,brand,model,categoryId);



        assertNotNull(createProductRequest);
        assertEquals(name, createProductRequest.getName());
        assertEquals(description, createProductRequest.getDescription());
        assertEquals(dailyPrice, createProductRequest.getDailyPrice());
        assertEquals(brand, createProductRequest.getBrand());
        assertEquals(model, createProductRequest.getModel());
        assertEquals(categoryId, createProductRequest.getCategoryId());

    }

    @Test
    void whenFinNotExistentProductthenShouldBeNull() {
        CreateProductRequest createProductRequest = new CreateProductRequest();
        assertNull(createProductRequest.getName());
        assertNull(createProductRequest.getDescription());
        assertNull(createProductRequest.getDailyPrice());
        assertNull(createProductRequest.getBrand());
        assertNull(createProductRequest.getModel());
        assertNull(createProductRequest.getCategoryId());

    }
}