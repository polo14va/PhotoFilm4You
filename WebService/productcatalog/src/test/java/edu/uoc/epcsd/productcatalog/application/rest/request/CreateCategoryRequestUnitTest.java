package edu.uoc.epcsd.productcatalog.application.rest.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateCategoryRequestUnitTest {
    @Test
    void whenCreateACategorythenShouldBeReturn(){
        Long parentCategoryId = 1L;
        String name = "category1";
        String description = "description";
        CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest(parentCategoryId,name,description);

        assertNotNull(createCategoryRequest);
        assertEquals(parentCategoryId, createCategoryRequest.getParentCategoryId());
        assertEquals(name, createCategoryRequest.getName());
        assertEquals(description, createCategoryRequest.getDescription());

    }

    @Test
    void whenFindNotExistentCategorythenShouldBeNull(){
        CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest();
        assertNull(createCategoryRequest.getParentCategoryId());
        assertNull(createCategoryRequest.getName());
        assertNull(createCategoryRequest.getDescription());

    }
}