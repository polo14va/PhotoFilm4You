package edu.uoc.epcsd.productcatalog.infrastructure.repository.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import edu.uoc.epcsd.productcatalog.domain.Item;
import org.junit.jupiter.api.Test;
class ItemEntityUnitTest {
    @Test
    void testFromDomainwhenCreateItem_thenGetItem() {
        // Given
        Long productId = 1L;

        Item item = new Item("123ABC", edu.uoc.epcsd.productcatalog.domain.ItemStatus.OPERATIONAL, productId);

        // When
        ItemEntity itemEntity = ItemEntity.fromDomain(item);

        // Then
        assertThat(itemEntity).isNotNull();
        assertThat(itemEntity.getSerialNumber()).isEqualTo(item.getSerialNumber());

    }

    @Test
    void testToDomainwhenCreateItem_thenGetItem() {
        // Given
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(1L);

        ItemEntity itemEntity = ItemEntity.builder()
                .serialNumber("123ABC")
                .product(productEntity)
                .build();

        // When
        Item item = itemEntity.toDomain();

        // Then
        assertThat(item).isNotNull();
        assertThat(item.getSerialNumber()).isEqualTo(itemEntity.getSerialNumber());
        assertThat(item.getProductId()).isEqualTo(productEntity.getId());
    }
}