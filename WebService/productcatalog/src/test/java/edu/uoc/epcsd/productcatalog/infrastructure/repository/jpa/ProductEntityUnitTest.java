package edu.uoc.epcsd.productcatalog.infrastructure.repository.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import edu.uoc.epcsd.productcatalog.domain.Product;
import org.junit.jupiter.api.Test;
class ProductEntityUnitTest {
    @Test
    void testFromDomainwhenCreateProductthenGetProduct() {
        // Given

        Double dailyPrice = 150.5;

        String brand = "Brand";

        String model = "Model";

        Long categoryId = 1L;

        Product product = new Product(dailyPrice,brand, model,categoryId);

        // When
        ProductEntity productEntity = ProductEntity.fromDomain(product);

        // Then
        assertThat(productEntity).isNotNull();
        assertThat(productEntity.getId()).isEqualTo(product.getId());
        assertThat(productEntity.getName()).isEqualTo(product.getName());
        assertThat(productEntity.getDescription()).isEqualTo(product.getDescription());
        assertThat(productEntity.getDailyPrice()).isEqualTo(product.getDailyPrice());
        assertThat(productEntity.getBrand()).isEqualTo(product.getBrand());
        assertThat(productEntity.getModel()).isEqualTo(product.getModel());
    }

    @Test
    void testToDomainwhenCreateProductthenGetProduct() {
        // Given
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(2L);

        ProductEntity productEntity = ProductEntity.builder()
                .id(1L)
                .name("Test Product")
                .description("Description")
                .dailyPrice(19.99)
                .brand("Brand")
                .model("Model")
                .category(categoryEntity)
                .build();

        // When
        Product product = productEntity.toDomain();

        // Then
        assertThat(product).isNotNull();
        assertThat(product.getId()).isEqualTo(productEntity.getId());
        assertThat(product.getName()).isEqualTo(productEntity.getName());
        assertThat(product.getDescription()).isEqualTo(productEntity.getDescription());
        assertThat(product.getDailyPrice()).isEqualTo(productEntity.getDailyPrice());
        assertThat(product.getBrand()).isEqualTo(productEntity.getBrand());
        assertThat(product.getModel()).isEqualTo(productEntity.getModel());
        assertThat(product.getCategoryId()).isEqualTo(categoryEntity.getId());
    }

}