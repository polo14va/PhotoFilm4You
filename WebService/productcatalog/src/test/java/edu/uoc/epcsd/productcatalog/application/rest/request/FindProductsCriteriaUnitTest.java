package edu.uoc.epcsd.productcatalog.application.rest.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FindProductsCriteriaUnitTest {

    @Test
    void whenCreateAnItemthenShouldBeReturn() {
        String name = "Item1";
        Long categoryId = 1L;

        FindProductsCriteria findProductsCriteria = new FindProductsCriteria(name, categoryId);



        assertNotNull(findProductsCriteria);

        assertEquals(name, findProductsCriteria.getName());
        assertEquals(categoryId, findProductsCriteria.getCategoryId());

    }

}