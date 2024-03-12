package edu.uoc.epcsd.productcatalog.application.rest.response;

import static org.assertj.core.api.Assertions.assertThat;

import edu.uoc.epcsd.productcatalog.domain.Product;
import org.junit.jupiter.api.Test;

class GetProductsAndAvailabilityResponseUnitTest {
    @Test
    void testGetProductsAndAvailabilityResponseBuilder() {
        // Given
        Long id = 1L;
        String name = "Test Product";
        String description = "Product description";
        Double dailyPrice = 19.99;
        String brand = "TestBrand";
        String model = "TestModel";
        Long categoryId = 2L;
        int quantityAvailable = 50;

        // When
        GetProductsAndAvailabilityResponse response = GetProductsAndAvailabilityResponse.builder()
                .id(id)
                .name(name)
                .description(description)
                .dailyPrice(dailyPrice)
                .brand(brand)
                .model(model)
                .categoryId(categoryId)
                .quantityAvailable(quantityAvailable)
                .build();

        GetProductsAndAvailabilityResponse response2 = GetProductsAndAvailabilityResponse.builder()
                .id(id)
                .name(name)
                .description(description)
                .dailyPrice(dailyPrice)
                .brand(brand)
                .model(model)
                .categoryId(categoryId)
                .quantityAvailable(quantityAvailable)
                .build();


        String result = response.toString();
        // Then
        assertThat(response).isEqualTo(response2);
        assertThat(response.hashCode()).isEqualTo(response2.hashCode());

        assertThat(result).contains("id=1");
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getName()).isEqualTo(name);
        assertThat(response.getDescription()).isEqualTo(description);
        assertThat(response.getDailyPrice()).isEqualTo(dailyPrice);
        assertThat(response.getBrand()).isEqualTo(brand);
        assertThat(response.getModel()).isEqualTo(model);
        assertThat(response.getCategoryId()).isEqualTo(categoryId);
        assertThat(response.getQuantityAvailable()).isEqualTo(quantityAvailable);
    }

    @Test
    void testGetProductsAndAvailabilityResponseFromDomain() {
        // Given
        Double dailyPrice = 150.5;

        String brand = "Brand";

        String model = "Model";

        Long categoryId = 1L;
        int quantityAvailable = 50;
        Product product = new Product(dailyPrice,brand, model,categoryId);


        // When
        GetProductsAndAvailabilityResponse response = GetProductsAndAvailabilityResponse.fromDomain(product, quantityAvailable);
        GetProductsAndAvailabilityResponse response2 = GetProductsAndAvailabilityResponse.fromDomain(product, quantityAvailable);


        // Then
        assertThat(response).isEqualTo(response2);
        assertThat(response.hashCode()).isEqualTo(response2.hashCode());

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(product.getId());
        assertThat(response.getName()).isEqualTo(product.getName());
        assertThat(response.getDescription()).isEqualTo(product.getDescription());
        assertThat(response.getDailyPrice()).isEqualTo(product.getDailyPrice());
        assertThat(response.getBrand()).isEqualTo(product.getBrand());
        assertThat(response.getModel()).isEqualTo(product.getModel());
        assertThat(response.getCategoryId()).isEqualTo(product.getCategoryId());
        assertThat(response.getQuantityAvailable()).isEqualTo(quantityAvailable);
    }
}