package edu.uoc.epcsd.notification.application.entities;

import edu.uoc.epcsd.notification.application.kafka.ProductMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ProductMessageTest {

    private ProductMessage productMessage;

    @BeforeEach
    void setUp() {
        productMessage = new ProductMessage();
    }

    @Test
    void settingProductIdReflectsInGetter() {
        Long expectedProductId = 123L;
        productMessage.setProductId(expectedProductId);
        assertEquals(expectedProductId, productMessage.getProductId());
    }

    @Test
    void settingDifferentProductIdReflectsInGetter() {
        Long unexpectedProductId = 123L;
        Long expectedProductId = 456L;
        productMessage.setProductId(expectedProductId);
        assertNotEquals(unexpectedProductId, productMessage.getProductId());
    }

    @Test
    void testToString() {
        ProductMessage productMessage = new ProductMessage();
        productMessage.setProductId(123L);

        String toStringResult = productMessage.toString();

        // Verifica que el método toString genere la cadena esperada
        assertThat(toStringResult).contains("productId=123");
    }

    @Test
    void testBuilder() {
        // Crea un objeto ProductMessage usando el constructor generado por el Builder
        ProductMessage productMessage = ProductMessage.builder()
                .productId(1L)
                .build();

        // Verifica que los valores se hayan configurado correctamente
        assertThat(productMessage.getProductId()).isEqualTo(1L);
    }

    @Test
    void testAllArgsConstructor() {
        ProductMessage productMessage = new ProductMessage(789L);

        // Verifica que el objeto se haya construido correctamente usando el constructor con argumentos
        assertThat(productMessage).isNotNull();
        assertThat(productMessage.getProductId()).isEqualTo(789L);
    }




    @Test
    void testProductIdGetter() {
        // Crea un objeto ProductMessage
        ProductMessage productMessage = new ProductMessage();
        productMessage.setProductId(1L);

        // Verifica que el método de acceso personalizado funciona correctamente
        assertThat(productMessage.getProductId()).isEqualTo(1L);
    }

}