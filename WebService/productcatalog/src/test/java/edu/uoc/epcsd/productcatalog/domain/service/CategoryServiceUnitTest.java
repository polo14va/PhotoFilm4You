package edu.uoc.epcsd.productcatalog.domain.service;

import edu.uoc.epcsd.productcatalog.domain.Category;
import edu.uoc.epcsd.productcatalog.domain.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class CategoryServiceUnitTest {
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void whenCreateCategoryShouldReturnCategoryId() {
        // Arrange
        Category category = new Category(1L);  // Replace with actual category creation logic
        when(categoryRepository.createCategory(category)).thenReturn(1L);

        // Act
        Long createdCategoryId = categoryService.createCategory(category);

        // Assert
        assertEquals(1L, createdCategoryId);
        verify(categoryRepository, times(1)).createCategory(category);
    }

    @Test
    void whenFindAllCategoriesShouldReturnAllCategories() {
        // Arrange
        List<Category> mockCategories = Arrays.asList(
                new Category(1L),
                new Category(1L)
        );
        when(categoryRepository.findAllCategories()).thenReturn(mockCategories);

        // Act
        List<Category> resultCategories = categoryService.findAllCategories();

        // Assert
        assertEquals(mockCategories, resultCategories);
        verify(categoryRepository, times(1)).findAllCategories();
    }

    @Test
    void whenFindCategoryByIdthenGetCategory(){
        Category category = new Category(1L);
        when(categoryRepository.findCategoryById(category.getId())).thenReturn(Optional.of(category));

        Optional<Category> result = categoryService.findCategoryById(category.getId());

        assertEquals(result.get().getId(), category.getId());
    }

    @Test
    void whenFindCategoriesByExamplethenGetCategories(){
        Category exampleCategory = new Category(1L);
        Category category1 = new Category(2L);
        Category category2 = new Category(3L);
        List<Category> mockCategories = Arrays.asList(
                category1,
                category2
        );

        when(categoryRepository.findCategoriesByExample(exampleCategory)).thenReturn(mockCategories);

        List<Category> result = categoryService.findCategoriesByExample(exampleCategory);

        assertEquals(mockCategories.size(), result.size());
        assertEquals(category1.getId(), result.get(0).getId());
        assertEquals(category1.getName(), result.get(0).getName());
        assertEquals(category1.getDescription(), result.get(0).getDescription());

        assertEquals(category2.getId(), result.get(1).getId());
        assertEquals(category2.getName(), result.get(1).getName());
        assertEquals(category2.getDescription(), result.get(1).getDescription());
    }
}