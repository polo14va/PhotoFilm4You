package edu.uoc.epcsd.productcatalog.application.rest.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateItemRequestUnitTest {

    @Test
    void whenCreateAnItemthenShouldBeReturn(){
        Long productId = 1L;
        String serialNumber = "000001";
        CreateItemRequest createItemRequest = new CreateItemRequest(productId, serialNumber);

        assertNotNull(createItemRequest);
        assertEquals(productId, createItemRequest.getProductId());
        assertEquals(serialNumber, createItemRequest.getSerialNumber());
    }

    @Test
    void whenFinNotExistentItemthenShouldBeNull(){
        CreateItemRequest createItemRequest = new CreateItemRequest();
        assertNull(createItemRequest.getProductId());
        assertNull(createItemRequest.getSerialNumber());
    }

}