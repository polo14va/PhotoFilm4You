package edu.uoc.epcsd.productcatalog.application.rest.response;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import edu.uoc.epcsd.productcatalog.domain.Product;

class GetProductResponseUnitTest {

    @Test
    void testGetProductResponseBuilder() {
        // Given
        Long id = 1L;
        String name = "Test Product";
        String description = "Product description";
        Double dailyPrice = 19.99;
        String brand = "TestBrand";
        String model = "TestModel";
        Long categoryId = 2L;

        // When
        GetProductResponse productResponse = GetProductResponse.builder()
                .id(id)
                .name(name)
                .description(description)
                .dailyPrice(dailyPrice)
                .brand(brand)
                .model(model)
                .categoryId(categoryId)
                .build();

        // Then
        assertThat(productResponse).isNotNull();
        assertThat(productResponse.getId()).isEqualTo(id);
        assertThat(productResponse.getName()).isEqualTo(name);
        assertThat(productResponse.getDescription()).isEqualTo(description);
        assertThat(productResponse.getDailyPrice()).isEqualTo(dailyPrice);
        assertThat(productResponse.getBrand()).isEqualTo(brand);
        assertThat(productResponse.getModel()).isEqualTo(model);
        assertThat(productResponse.getCategoryId()).isEqualTo(categoryId);
    }

    @Test
    void testGetProductResponseFromDomain() {
        // Given


        Double dailyPrice = 150.5;

        String brand = "Brand";

        String model = "Model";

        Long categoryId = 1L;

        Product product = new Product(dailyPrice,brand, model,categoryId);
        // When
        GetProductResponse productResponse = GetProductResponse.fromDomain(product);

        // Then
        assertThat(productResponse).isNotNull();
        assertThat(productResponse.getId()).isEqualTo(product.getId());
        assertThat(productResponse.getName()).isEqualTo(product.getName());
        assertThat(productResponse.getDescription()).isEqualTo(product.getDescription());
        assertThat(productResponse.getDailyPrice()).isEqualTo(product.getDailyPrice());
        assertThat(productResponse.getBrand()).isEqualTo(product.getBrand());
        assertThat(productResponse.getModel()).isEqualTo(product.getModel());
        assertThat(productResponse.getCategoryId()).isEqualTo(product.getCategoryId());
    }
}