package edu.uoc.epcsd.productcatalog.application.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;


import edu.uoc.epcsd.productcatalog.application.rest.request.CreateItemRequest;
import edu.uoc.epcsd.productcatalog.domain.Item;
import edu.uoc.epcsd.productcatalog.domain.ItemStatus;
import edu.uoc.epcsd.productcatalog.domain.service.ItemService;

@ExtendWith(MockitoExtension.class)
class ItemRESTControllerUnitTest {

    @Mock
    private ItemService itemService;


    @InjectMocks
    private ItemRESTController itemRestController;

    private Item mockItem;
    private List<Item> mockItems;

    
   

    @BeforeEach
    void setUp() {

        mockItem = Item.builder().serialNumber("0001").productId(1L).status(ItemStatus.OPERATIONAL).build();
        mockItems = Arrays.asList(mockItem, Item.builder().serialNumber("0002").productId(1L).status(ItemStatus.OPERATIONAL).build());


    }

    @Test
    void whenGetAllItemsthenShouldReturnListOfItems() {
        // Arrange

        when(itemService.findAllItems()).thenReturn(mockItems);

        // Act
        List<Item> result = itemRestController.getAllItems();

        // Assert
        assertEquals(mockItems, result);
    }

    @Test
    void whenGetItemByIdthenShouldReturnItemWhenItemExists() {
        // Arrange
        String serialNumber = "0001";

        when(itemService.findBySerialNumber(serialNumber)).thenReturn(Optional.of(mockItem));

        // Act
        ResponseEntity<Item> responseEntity = itemRestController.getItemById(serialNumber);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockItem, responseEntity.getBody());
    }

    @Test
    void whenGtItemByIdthenShouldReturnNotFoundWhenItemDoesNotExist() {
        // Arrange
        String serialNumber = "invalidSerialNumber";
        when(itemService.findBySerialNumber(serialNumber)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Item> responseEntity = itemRestController.getItemById(serialNumber);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void whenGetItemsByProductthenShouldReturnItemCount() {
        // Arrange
        int productId = 1;

        when(itemService.findByProductId((anyLong()))).thenReturn(mockItems);

        // Act
        ResponseEntity<Integer> responseEntity = itemRestController.getItemsByProduct(productId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockItems.size(), responseEntity.getBody());
        assertEquals(mockItems.size(), responseEntity.getBody().intValue());
    }

    @Test
    void setOperationalShouldReturnItemWhenSettingOperational() {
        // Arrange
        String serialNumber = "0001";
        boolean operational = true;

        when(itemService.setOperational(serialNumber, operational)).thenReturn(mockItem);

        // Act
        ResponseEntity<Item> responseEntity = itemRestController.setOperational(serialNumber, operational);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockItem, responseEntity.getBody());
    }

    @Test
    void whenSetOperationalthenShouldReturnItemWhenSettingNonOperational() {
        // Arrange
        String serialNumber = "0001";
        boolean operational = false;

        when(itemService.setOperational(serialNumber, operational)).thenReturn(mockItem);
        // Act
        ResponseEntity<Item> responseEntity = itemRestController.setOperational(serialNumber, operational);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockItem, responseEntity.getBody());
    }

    @Test
    void setOperationalShouldReturnBadRequestWhenItemNotFound() {
        // Arrange
        String serialNumber = "invalidSerialNumber";
        boolean operational = true;
        when(itemService.setOperational(serialNumber, operational)).thenThrow(new IllegalArgumentException("Item not found"));

        // Act & Assert
        assertEquals(HttpStatus.BAD_REQUEST, assertThrows(ResponseStatusException.class,
                () -> itemRestController.setOperational(serialNumber, operational)).getStatusCode());
    }

    @Test
    void whenCreateItemthenGetSerialNumber() {

        CreateItemRequest createItemRequest = new CreateItemRequest(1L, "0001");
        String serialNumber = "0001";

        MockMvcBuilders.standaloneSetup(itemRestController).build();
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));


        when(itemService.createItem(any(),any())).thenReturn(serialNumber);

        // Act
        ResponseEntity<String> responseEntity = itemRestController.createItem(createItemRequest);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(serialNumber, responseEntity.getBody());
    }

    @Test
    void testCreateItemWithInvalidProductId(){
        CreateItemRequest createItemRequest = new CreateItemRequest(1L,"0001");

        when(itemService.createItem(any(Long.class), any(String.class)))
                .thenThrow(new IllegalArgumentException("Invalid product ID"));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                itemRestController.createItem(createItemRequest));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("The specified product id 1 does not exist.", exception.getReason());

    }


}