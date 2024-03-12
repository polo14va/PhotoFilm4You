package edu.uoc.epcsd.productcatalog.application.rest.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FindCategoriesCriteriaUnitTest {
    @Test
    void whenCreateACategorythenShouldBeReturn(){
        Long parentCategoryId = 1L;
        String name = "category1";
        String description = "description";
        FindCategoriesCriteria findCategoriesCriteria = new FindCategoriesCriteria(name, description, parentCategoryId);

        assertNotNull(findCategoriesCriteria);
        assertEquals(parentCategoryId, findCategoriesCriteria.getParentId());
        assertEquals(name, findCategoriesCriteria.getName());
        assertEquals(description, findCategoriesCriteria.getDescription());

    }
}