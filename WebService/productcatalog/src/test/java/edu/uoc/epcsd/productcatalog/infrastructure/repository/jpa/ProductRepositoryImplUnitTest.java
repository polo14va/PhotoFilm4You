package edu.uoc.epcsd.productcatalog.infrastructure.repository.jpa;

import edu.uoc.epcsd.productcatalog.domain.Category;
import edu.uoc.epcsd.productcatalog.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryImplUnitTest {
    @Mock
    private SpringDataProductRepository jpaRepository;

    @Mock
    private SpringDataCategoryRepository jpaCategoryRepository;
    @InjectMocks
    private ProductRepositoryImpl productRepository;

    private ProductEntity mockProductEntity;
    private List<ProductEntity> mockProductEntities;

    private Product mockProduct;
    private Category mockCategory;
    private CategoryEntity mockCategoryEntity;
    @BeforeEach
    void setUp(){

        mockCategory = Category.builder().id(1L).name("Category1").description("Description1").build();
        mockProduct = Product.builder().name("Product").model("model").description("description").id(1L).categoryId(mockCategory.getId()).brand("brand").build();
        mockCategoryEntity = CategoryEntity.builder().id(mockCategory.getId()).build();
        mockProductEntity = ProductEntity.builder()
                .name("Name")
                .model("Model")
                .description("Description")
                .dailyPrice(55.9)
                .category(mockCategoryEntity)
                .build();
        mockProductEntities = Arrays.asList(
                mockProductEntity,
                ProductEntity.builder()
                        .name("Name2")
                        .model("Model2")
                        .description("Description2")
                        .dailyPrice(55.9)
                        .category(mockCategoryEntity)
                        .brand("Brand")
                        .build()
        );
    }

    @Test
    void whenFindAllProductsthenGetListOfProducts(){

        when(jpaRepository.findAll()).thenReturn(mockProductEntities);

        List<Product> result = productRepository.findAllProducts();

        assertEquals(mockProductEntities.size(), result.size());
        assertEquals(mockProductEntities.get(0).getId(), result.get(0).getId());
        assertEquals(mockProductEntities.get(1).getId(), result.get(1).getId());
    }

    @Test
    void whenFindProductByIdthenGetProduct(){

        when(jpaRepository.findById(mockProductEntity.getId())).then(invocation -> Optional.of(mockProductEntity));

        Optional<Product> result = productRepository.findProductById(mockProductEntity.getId());
        assertTrue(result.isPresent());
        assertEquals(mockProductEntity.getId(), result.get().getId());
    }

    @Test
    void whenCreateProductthenGetId(){

        ProductEntity productEntity = ProductEntity.fromDomain(mockProduct);

        when(jpaCategoryRepository.findById(mockProduct.getCategoryId())).thenReturn(Optional.of(CategoryEntity.builder().id(mockCategory.getId()).build()));
        when(jpaRepository.save(any())).thenReturn(productEntity);

        Long id = productRepository.createProduct(mockProduct);

        assertEquals(id, mockProduct.getId());
    }

    @Test
    void whenCreateProductwithNonExistingCategorythenThrowsException() {
        when(jpaCategoryRepository.findById(mockProduct.getCategoryId())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> productRepository.createProduct(mockProduct));
    }

    @Test
    void findCatalogTest() {
        // Setup
        Pageable pageable = Pageable.unpaged();
        ProductEntity mockedProductEntity = ProductEntity.builder().id(1L).name("name").brand("brand")
                .description("description").category(mockCategoryEntity).build();
        Page<ProductEntity> mockedProductEntityPage = new PageImpl<>(Collections.singletonList(mockedProductEntity));

        // Mocking the repository behavior
        when(jpaRepository.findAll(pageable)).thenReturn(mockedProductEntityPage);

        // Execute
        Page<Product> result = productRepository.findCatalog(pageable);

        // Verify
        assertEquals(1, result.getTotalElements()); // Assuming one product in the mocked response


    }

    @Test
    void deleteProductTest() {

        productRepository.deleteProduct(mockProduct);
        // Verify that the delete method of the repository is called with the correct parameter
        verify(jpaRepository).delete(ProductEntity.fromDomain(mockProduct));
    }

    @Test
    void findProductsByExampleTest() {
        // Setup

        Long categoryId = 1L;

        when(jpaCategoryRepository.findById(categoryId)).thenReturn(Optional.of(mockCategoryEntity)); // Replace with your actual CategoryEntity

        ProductEntity exampleProductEntity = ProductEntity.fromDomain(mockProduct);
        exampleProductEntity.setCategory(jpaCategoryRepository.findById(categoryId).orElseThrow(IllegalArgumentException::new));

        when(jpaRepository.findAll(any(Example.class))).thenReturn(Collections.singletonList(exampleProductEntity));

        // Execute
        List<Product> result = productRepository.findProductsByExample(mockProduct);

        // Verify
        assertEquals(1, result.size()); // Assuming one product in the mocked response

        // Add more assertions based on your specific requirements
    }




}