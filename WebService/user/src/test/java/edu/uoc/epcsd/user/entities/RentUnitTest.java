package edu.uoc.epcsd.user.entities;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RentUnitTest {

    private final Long RENT_ID = 1L;
    private final long ORDER_ID = 101L;
    private final Long PRODUCT_ID = 201L;
    private final Long USER_ID = 301L;
    private final char ESTADO = 'E';
    private final int QUANTITY = 3;

    @Test
    void createRentInstance() {
        Rent rent = Rent.builder()
                .id(RENT_ID)
                .orderId(ORDER_ID)
                .productId(PRODUCT_ID)
                .userId(USER_ID)
                .estado(ESTADO)
                .quantity(QUANTITY)
                .build();

        assertThat(rent).isNotNull();
        assertThat(rent.getId()).isEqualTo(RENT_ID);
        assertThat(rent.getOrderId()).isEqualTo(ORDER_ID);
        assertThat(rent.getProductId()).isEqualTo(PRODUCT_ID);
        assertThat(rent.getUserId()).isEqualTo(USER_ID);
        assertThat(rent.getEstado()).isEqualTo(ESTADO);
        assertThat(rent.getQuantity()).isEqualTo(QUANTITY);
    }

    @Test
    void createRentWithoutId() {
        Rent rent = Rent.builder()
                .orderId(ORDER_ID)
                .productId(PRODUCT_ID)
                .userId(USER_ID)
                .estado(ESTADO)
                .quantity(QUANTITY)
                .build();

        assertThat(rent).isNotNull();
        assertThat(rent.getId()).isNull();
        assertThat(rent.getOrderId()).isEqualTo(ORDER_ID);
        assertThat(rent.getProductId()).isEqualTo(PRODUCT_ID);
        assertThat(rent.getUserId()).isEqualTo(USER_ID);
        assertThat(rent.getEstado()).isEqualTo(ESTADO);
        assertThat(rent.getQuantity()).isEqualTo(QUANTITY);
    }

    @Test
    void testEqualsAndHashCode() {
        Rent rent1 = Rent.builder().id(RENT_ID).orderId(ORDER_ID).productId(PRODUCT_ID).userId(USER_ID).estado(ESTADO).quantity(QUANTITY).build();
        Rent rent2 = Rent.builder().id(RENT_ID).orderId(ORDER_ID).productId(PRODUCT_ID).userId(USER_ID).estado(ESTADO).quantity(QUANTITY).build();
        Rent rent3 = Rent.builder().id(2L).orderId(102L).productId(202L).userId(302L).estado('B').quantity(4).build();

        // Test Equals
        assertThat(rent1.equals(rent2)).isTrue();
        assertThat(rent1.equals(rent3)).isFalse();

        // Test HashCode
        assertThat(rent1.hashCode()).isEqualTo(rent2.hashCode());
        assertThat(rent1.hashCode()).isNotEqualTo(rent3.hashCode());
    }


}
