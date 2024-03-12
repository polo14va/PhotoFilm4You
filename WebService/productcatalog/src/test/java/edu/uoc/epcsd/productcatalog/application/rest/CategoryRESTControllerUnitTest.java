package edu.uoc.epcsd.productcatalog.application.rest;


import edu.uoc.epcsd.productcatalog.application.rest.request.CreateCategoryRequest;
import edu.uoc.epcsd.productcatalog.application.rest.request.FindCategoriesCriteria;
import edu.uoc.epcsd.productcatalog.domain.Category;
import edu.uoc.epcsd.productcatalog.domain.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;


import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryRESTControllerUnitTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryRESTController categoryRestController;

    @Test
    void getAllCategories() {
        // Arrange
        List<Category> categories = Arrays.asList(
                new Category(1L),
                new Category(2L)
        );
        when(categoryService.findAllCategories()).thenReturn(categories);

        // Act
        List<Category> result = categoryRestController.getAllCategories();

        // Assert
        assertEquals(categories, result);
    }

    @Test
    void findCategoryById() {
        // Arrange
        Long categoryId = 1L;
        Category mockCategory = new Category(categoryId);
        when(categoryService.findCategoryById(categoryId)).thenReturn(Optional.of(mockCategory));

        // Act
        ResponseEntity<Category> responseEntity = categoryRestController.findCategoryById(categoryId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockCategory, responseEntity.getBody());
    }

    @Test
    void findCategoryByIdWithCategoryNotExist() {
        // Arrange
        Long categoryId = 999L;
        when(categoryService.findCategoryById(categoryId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Category> responseEntity = categoryRestController.findCategoryById(categoryId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void findCategoriesByCriteria() {
        // Arrange
        FindCategoriesCriteria criteria = new FindCategoriesCriteria("Name", "Description", 1L);
        List<Category> mockCategories = Arrays.asList(
                new Category(1L),
                new Category(2L)
        );
        when(categoryService.findCategoriesByExample(any())).thenReturn(mockCategories);

        // Act
        ResponseEntity<List<Category>> responseEntity = categoryRestController.findCategoriesByCriteria(criteria);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockCategories, responseEntity.getBody());
    }

    @Test
    void findCategoriesByCriteriaBadRequestWhenParentCategoryNotFound() {
        // Arrange
        FindCategoriesCriteria criteria = new FindCategoriesCriteria("Name", "Description", 999L);
        when(categoryService.findCategoriesByExample(any())).thenThrow(new IllegalArgumentException("Parent category not found"));

        // Act & Assert
        assertThrows(ResponseStatusException.class,
                () -> categoryRestController.findCategoriesByCriteria(criteria));
    }


    @Test
    void createCategoryShouldReturnBadRequestWhenParentCategoryNotFound() {
        // Arrange
        CreateCategoryRequest request = new CreateCategoryRequest(1L, "Category1", "Description1");
        when(categoryService.createCategory(any())).thenThrow(new IllegalArgumentException("Parent category not found"));

        // Act & Assert
        assertThrows(ResponseStatusException.class,
                () -> categoryRestController.createCategory(request));
    }
}