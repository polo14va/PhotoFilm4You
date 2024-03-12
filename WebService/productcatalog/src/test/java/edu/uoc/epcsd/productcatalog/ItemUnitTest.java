package edu.uoc.epcsd.productcatalog;

import edu.uoc.epcsd.productcatalog.domain.Item;
import edu.uoc.epcsd.productcatalog.domain.ItemStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemUnitTest {

    @Test
    void testNonOperationalStatus() {
        Item item = Item.builder()
                .serialNumber("12345")
                .status(ItemStatus.OPERATIONAL)
                .productId(1L)
                .build();

        item.setStatus(ItemStatus.NON_OPERATIONAL);// Set status to NON_OPERATIONAL
        assertEquals(ItemStatus.NON_OPERATIONAL, item.getStatus());// Verify that the status is NON_OPERATIONAL
    }

    @Test
    void testOperationalStatus() {
        Item item = Item.builder()
                .serialNumber("12345")
                .status(ItemStatus.NON_OPERATIONAL)
                .productId(1L)
                .build();

        item.setStatus(ItemStatus.OPERATIONAL);// Set status to OPERATIONAL
        assertEquals(ItemStatus.OPERATIONAL, item.getStatus());// Verify that the status is OPERATIONAL
    }
}
