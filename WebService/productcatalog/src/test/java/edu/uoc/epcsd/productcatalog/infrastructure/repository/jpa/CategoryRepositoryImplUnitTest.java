package edu.uoc.epcsd.productcatalog.infrastructure.repository.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import edu.uoc.epcsd.productcatalog.domain.Category;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryRepositoryImplUnitTest {
    @Mock
    private SpringDataCategoryRepository jpaRepository;

    @InjectMocks
    private CategoryRepositoryImpl categoryRepository;

    private Category mockCategory;
    private CategoryEntity mockCategoryEntity;

    private List<CategoryEntity> mockCategoryEntities;
    

    @BeforeEach
    void setUp() {
        mockCategory = Category.builder().id(1L).name("Category1").description("Description1").build();
        mockCategoryEntity = CategoryEntity.builder().id(mockCategory.getId()).build();
        mockCategoryEntities = Arrays.asList(mockCategoryEntity,
                CategoryEntity.fromDomain(Category.builder().id(2L).name("Category2").description("Description2").build()));
     

    }

    @Test
    void whenFindAllCategoriesthenGetList() {

        given(jpaRepository.findAll()).willReturn(mockCategoryEntities);
        List<Category> result = categoryRepository.findAllCategories();
        assertEquals(2, result.size());

    }

    @Test
    void whenFindCategoryByIdthenGetCategory() {


        when(jpaRepository.findById(mockCategoryEntity.getId())).then(invocation -> Optional.of(mockCategoryEntity));

        Optional<Category> result = categoryRepository.findCategoryById(mockCategoryEntity.getId());
        assertTrue(result.isPresent());
        assertEquals(mockCategory.getId(), result.get().getId());

    }

    @Test
    void whenCreateCategorythenGetCategoryId(){
        Category category = Category.builder().name("Category test").description("description test").parentId(mockCategory.getId()).build();
       CategoryEntity categoryEntity = CategoryEntity.fromDomain(category);
        when(jpaRepository.findById(mockCategory.getId())).thenReturn(Optional.of(CategoryEntity.fromDomain(mockCategory)));
        when(jpaRepository.save(any(CategoryEntity.class))).thenReturn(categoryEntity);

        Long id = categoryRepository.createCategory(category);

        assertEquals(category.getId(), id);


    }

    @Test
    void findCategoriesByExampleTest() {
        // Setup

        Category exampleCategory = new Category(/* set category details for the example */);
        Long parentId = 1L;

        when(jpaRepository.findById(parentId)).thenReturn(Optional.of(new CategoryEntity(/* set parent category details */)));

        CategoryEntity exampleCategoryEntity = CategoryEntity.fromDomain(exampleCategory);
        exampleCategoryEntity.setParent(jpaRepository.findById(parentId).orElseThrow(IllegalArgumentException::new));

        // Adjusted stubbing using any() matcher
        when(jpaRepository.findAll(any(Example.class))).thenReturn(Collections.singletonList(exampleCategoryEntity));

        // Execute
        List<Category> result = categoryRepository.findCategoriesByExample(exampleCategory);

        // Verify
        assertEquals(1, result.size()); // Assuming one category in the mocked response

        // Add more assertions based on your specific requirements
    }

    @Test
    void findCategoriesByExampleTest_2() {
        // Setup
        Category exampleCategory = new Category(/* set category details for the example */);
        Long parentId = 1L; // Assuming a non-null parent ID

        // Mock behavior for parent category retrieval
        when(jpaRepository.findById(parentId)).thenReturn(Optional.of(new CategoryEntity(/* set parent category details */)));

        // Set the non-null parent ID
        exampleCategory.setParentId(parentId);

        // Execute
        List<Category> result = categoryRepository.findCategoriesByExample(exampleCategory);

        // Verify
        assertEquals(0, result.size()); // Assuming one category in the mocked response

    }


}