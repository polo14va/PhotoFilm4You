package edu.uoc.epcsd.productcatalog.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import edu.uoc.epcsd.productcatalog.domain.Item;
import edu.uoc.epcsd.productcatalog.domain.ItemStatus;
import edu.uoc.epcsd.productcatalog.domain.Product;
import edu.uoc.epcsd.productcatalog.domain.repository.ItemRepository;
import edu.uoc.epcsd.productcatalog.infrastructure.kafka.ProductMessage;


@ExtendWith(MockitoExtension.class)
class ItemServiceUnitTest {
    @Mock
    private ItemRepository itemRepository;

    @Mock
    private KafkaTemplate<String, ProductMessage> productKafkaTemplate;

    @InjectMocks
    private ItemService itemService;

  
    @Test
    void findAllItemsTest() {

        // Arrange
        when(itemRepository.findAllItems()).thenReturn(Arrays.asList(new Item(), new Item()));

        // Act
        List<Item> items = itemService.findAllItems();

        // Assert
        assert !items.isEmpty();
        verify(itemRepository, times(1)).findAllItems();
    }

    @Test
    void whenFindBySerialNumberthenItemExist() {
        // Arrange
        String serialNumber = "123";
        when(itemRepository.findBySerialNumber(serialNumber)).thenReturn(Optional.of(new Item()));

        // Act
        Optional<Item> item = itemService.findBySerialNumber(serialNumber);

        // Assert
        assertTrue(item.isPresent());
        verify(itemRepository, times(1)).findBySerialNumber(serialNumber);
    }

    @Test
    void createItemTest() {
        // Arrange
        Long productId = 1L;
        String serialNumber = "123";
        Item item = Item.builder().serialNumber(serialNumber).productId(productId).build();

        // Act
        lenient().when(itemRepository.save(any(Item.class))).thenReturn(item);
        itemService.createItem(productId, serialNumber);

        // Assert
        verify(itemRepository, times(1)).createItem(item);
        verify(productKafkaTemplate, times(1)).send(anyString(), any(ProductMessage.class));
    }


    @Test
    void deleteItem() {
        // Arrange
        String serialNumber = "123";

        // Act
        itemService.deleteItem(serialNumber);

        // Assert
        verify(itemRepository, times(1)).deleteItem(serialNumber);
    }

    @Test
    void setOperational() {
        // Arrange
        String serialNumber = "123";
        Item item = Item.builder().serialNumber(serialNumber).status(ItemStatus.NON_OPERATIONAL).build();

        // Act
        when(itemRepository.findBySerialNumber(serialNumber)).thenReturn(Optional.of(item));
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        // Set operational to true
       Item updatedItem =  itemService.setOperational(serialNumber, true);

        // Assert
        verify(itemRepository, times(1)).findBySerialNumber(serialNumber);
        verify(itemRepository, times(1)).save(item);
        verify(productKafkaTemplate, times(1)).send(anyString(), any(ProductMessage.class));
        assertEquals(ItemStatus.OPERATIONAL, updatedItem.getStatus());
        // Set operational to false (no additional Kafka send expected)
        reset(productKafkaTemplate);
        itemService.setOperational(serialNumber, false);
        verify(productKafkaTemplate, times(0)).send(anyString(), any(ProductMessage.class));

    }

    @Test
    void whenFindProductByIdthenGetProduct(){

        Long id = 1L;
        Product product = new Product();
        product.setId(id);
        Item item1 = Item.builder().serialNumber("0001").status(ItemStatus.NON_OPERATIONAL).productId(id).build();
        Item item2 = Item.builder().serialNumber("0002").status(ItemStatus.NON_OPERATIONAL).productId(id).build();
        List<Item> items = Arrays.asList(item1,item2);

        when(itemRepository.findByProductId(id)).thenReturn(items);
        List<Item> result = itemService.findByProductId(id);
        assertEquals(items.size(),result.size());

    }
}