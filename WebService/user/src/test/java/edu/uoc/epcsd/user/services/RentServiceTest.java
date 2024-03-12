package edu.uoc.epcsd.user.services;

import static edu.uoc.epcsd.user.services.RentService.ITEM_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import edu.uoc.epcsd.user.controllers.dtos.GetProductResponse;
import edu.uoc.epcsd.user.entities.Rent;
import edu.uoc.epcsd.user.repositories.RentRepository;

@ExtendWith(MockitoExtension.class)
class RentServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private RentRepository rentRepository;


    @InjectMocks
    private RentService rentService;

    @Value("productCatalog.getAvailableProductQuantity.url")
    private String productItemAvailableQty;

    @Test
    void deleteOrderLine_OrderNotFound_ReturnsFalse() {
        // Arrange
        Long orderId = 1L;
        when(rentRepository.findCarritoById(orderId)).thenReturn(Optional.empty());

        // Act
        boolean result = rentService.deleteOrderLine(orderId);

        // Assert
        assertThat(result).isFalse();
        verify(rentRepository, never()).deleteLineaDeCarritoById(anyLong());
    }

    @Test
    void deleteOrderLine_OrderFound_ReturnsTrue() {
        // Arrange
        Long orderId = 1L;
        when(rentRepository.findCarritoById(orderId)).thenReturn(Optional.of(new Rent()));

        // Act
        boolean result = rentService.deleteOrderLine(orderId);

        // Assert
        assertThat(result).isTrue();
        verify(rentRepository).deleteLineaDeCarritoById(orderId);
    }

    @Test
    void getCartByUser_ReturnsListOfOrders() {
        // Arrange
        Long userId = 1L;
        List<Rent> expectedOrders = Arrays.asList(new Rent(), new Rent());
        when(rentRepository.findCarritoByUser(userId)).thenReturn(expectedOrders);

        // Act
        List<Rent> result = rentService.getCartByUser(userId);

        // Assert
        assertThat(result).isEqualTo(expectedOrders);
    }

    @Test
    void payCart_CartPaid_ReturnsTrue() {
        // Arrange
        Long userId = 1L;
        when(rentRepository.payCart(userId)).thenReturn(1);

        // Act
        boolean result = rentService.payCart(userId);

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    void getReservationsById_Returns() {
        // Arrange
        Long reservationId = 1L;
        List<Rent> expectedOrders = Arrays.asList(new Rent());
        when(rentRepository.getReservationsById(reservationId)).thenReturn(expectedOrders);

        // Act
        List<Rent> result = rentService.getReservationsById(reservationId);

        // Assert
        assertThat(result).isEqualTo(expectedOrders);
    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    void getReservationsByUser_Returns() {
        // Arrange
        Long userId = 1L;
        List<Rent> expectedOrders = Arrays.asList();
        when(rentRepository.getReservationsByUser(userId)).thenReturn(expectedOrders);

        // Act
        List<Rent> result = rentService.getReservationsById(userId);

        // Assert
        assertThat(result).isEqualTo(expectedOrders);
    }


    @Test
    void updateReservation_Returns() {
        // Arrange
        Long userId = 1L;
        Long productId = 1L;
        Long id = 1L;
        int quantity = 3;
        when(rentRepository.updateReservation(id, productId, userId, quantity)).thenReturn(1);

        // Act
        long result = rentService.updateReservation(id, productId, userId, quantity);

        // Assert
        assertThat(result).isEqualTo(1);
    }

    @Test
    void getStockReservedByProduct_Returns() {
        // Arrange
        long productId = 1L;
        when(rentRepository.getReservedQty(productId, -1)).thenReturn(Optional.of(1));

        // Act
        int result = rentService.getStockReservedByProduct(productId);

        // Assert
        assertThat(result).isEqualTo(1);
    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    void getAvailableQty_ReturnsOk() {
        // Arrange
        long productId = 1L;
        when(rentRepository.getReservedQty(productId, -1)).thenReturn(Optional.of(1));
        when(restTemplate.getForObject(productItemAvailableQty, Integer.class, productId)).thenReturn(5);

        // Act
        int result = rentService.getStockReservedByProduct(productId);

        // Assert
        assertThat(result).isEqualTo(1);
    }

    @Test
    void testAddRentLine() throws NoSuchFieldException, IllegalAccessException {
        // Arrange
        Long productId = 1L;
        Long userId = 2L;
        int quantity = 3;

        Rent rent = Rent.builder()
                .productId(productId)
                .userId(userId)
                .quantity(quantity)
                .estado('E')
                .build();
        String productCatalogUrl = "http://example.com/products/{productId}"; // replace with your actual URL
        String productItemAvailableQty = "http://example.com/availableQty/{productId}"; // replace with your actual URL

        Field productCatalogUrlField = RentService.class.getDeclaredField("productCatalogUrl");
        productCatalogUrlField.setAccessible(true);
        productCatalogUrlField.set(rentService, productCatalogUrl);

        Field productItemAvailableQtyField = RentService.class.getDeclaredField("productItemAvailableQty");
        productItemAvailableQtyField.setAccessible(true);
        productItemAvailableQtyField.set(rentService, productItemAvailableQty);

        // Simulate response for RestTemplate
        when(restTemplate.getForEntity(productCatalogUrl, GetProductResponse.class, productId))
                .thenReturn(ResponseEntity.ok(new GetProductResponse
                        (1L,"name","description",99.9,"brand","model","category")));

        when(restTemplate.getForObject(productItemAvailableQty, Integer.class, productId)).thenReturn(10); // Adjust available items as needed

        // Simulate response for rentRepository
        when(rentRepository.getReservedQty(productId, -1)).thenReturn(Optional.of(2)); // Adjust reservedQty as needed

        when(rentRepository.findCarritoByUser(userId)).thenReturn(Collections.emptyList()); // Simulate an empty list for currentRent

        when(rentRepository.save(any(Rent.class))).thenAnswer(invocation -> {
            Rent savedRent = invocation.getArgument(0);
            savedRent.setId(1L); // Set a specific ID for the saved rent
            return savedRent;
        });
        // Act
        Long result = rentService.addRentLine(productId, userId, quantity);

        // Assert
        assertEquals(userId, result);
        // Add assertions based on the expected behavior of your method and services

        // Verify interactions with RestTemplate and rentRepository

        verify(rentRepository).findCarritoByUser(userId);
        verify(rentRepository).save(any(Rent.class));
        verify(rentRepository).updateOrderId(anyLong());



    }



    @Test
    void testUpdateCartQty() throws NoSuchFieldException, IllegalAccessException {
        // Arrange
        long rentId = 1L;
        int quantity = 3;
        long productId = 2L;

        String productItemAvailableQty = "http://example.com/availableQty/{productId}";
        Field productItemAvailableQtyField = RentService.class.getDeclaredField("productItemAvailableQty");
        productItemAvailableQtyField.setAccessible(true);
        productItemAvailableQtyField.set(rentService, productItemAvailableQty);


        // Simulate response for rentRepository.getProductByRentId(rentId)
        when(rentRepository.getProductByRentId(rentId)).thenReturn(productId);

        // Simulate response for restTemplate.getForObject
        when(restTemplate.getForObject(anyString(), eq(Integer.class), eq(productId)))
                .thenReturn(10); // Adjust available items as needed

        // Simulate response for rentRepository.getReservedQty
        when(rentRepository.getReservedQty(productId, rentId)).thenReturn(Optional.of(2)); // Adjust reservedQty as needed

        // Act
        int result = rentService.updateCartQty(rentId, quantity);

        // Assert
        // Add assertions based on the expected behavior of your method

        // Verify interactions with RestTemplate and rentRepository
        verify(rentRepository).getProductByRentId(rentId);
        verify(restTemplate).getForObject(anyString(), eq(Integer.class), eq(productId));
        verify(rentRepository).getReservedQty(productId, rentId);
        verify(rentRepository).updateCartQty(rentId, quantity);
    }


    @Test
    void testGetAvailableQty() throws NoSuchFieldException, IllegalAccessException {
        // Arrange
        Long productId = 1L;

        String productItemAvailableQty = "http://example.com/availableQty/{productId}";
        Field productItemAvailableQtyField = RentService.class.getDeclaredField("productItemAvailableQty");
        productItemAvailableQtyField.setAccessible(true);
        productItemAvailableQtyField.set(rentService, productItemAvailableQty);

        // Simulate response for restTemplate.getForObject
        when(restTemplate.getForObject(anyString(), eq(Integer.class), eq(productId)))
                .thenReturn(10); // Adjust available items as needed

        // Simulate response for rentRepository.getReservedQty
        when(rentRepository.getReservedQty(productId, -1)).thenReturn(Optional.of(2)); // Adjust reservedQty as needed

        // Act
        int result = rentService.getAvailableQty(productId);

        // Assert
        // Add assertions based on the expected behavior of your method

        // Verify interactions with RestTemplate and rentRepository
        verify(restTemplate).getForObject(anyString(), eq(Integer.class), eq(productId));
        verify(rentRepository).getReservedQty(productId, -1);
    }

    @Test
    void testGetReservationsByUser() {
        // Arrange
        long userId = 1L;
        Rent rent1 = Rent.builder().id(1L).build();
        Rent rent2 = Rent.builder().id(2L).build();;

        // Mock the behavior of rentRepository
        when(rentRepository.getReservationsByUser(userId)).thenReturn(Arrays.asList(rent1, rent2));

        // Act
        List<Rent> reservations = rentService.getReservationsByUser(userId);

        // Assert
        assertEquals(2, reservations.size()); // Check if the expected number of reservations is returned
        // You can further check the contents of the list if needed
    }

    @Test
    void testGetReservationsByOrderId() {
        // Arrange
        long orderId = 1L;
        Rent rent1 = Rent.builder().id(1L).build();
        Rent rent2 = Rent.builder().id(2L).build();

        // Mock the behavior of rentRepository
        when(rentRepository.getReservationsByOrderId(orderId)).thenReturn(Arrays.asList(rent1, rent2));

        // Act
        List<Rent> reservations = rentService.getReservationsByOrderId(orderId);

        // Assert
        assertEquals(2, reservations.size()); // Check if the expected number of reservations is returned
        // You can further check the contents of the list if needed
    }


    @Test
    void testGetAvailableQtyRestClientException() {
        // Arrange
        Long productId = 1L;
        when(restTemplate.getForObject(productItemAvailableQty, Integer.class, productId)).thenThrow(new RestClientException("Error fetching product"));
        // Act and Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> rentService.getAvailableQty(productId));

        assertEquals(ITEM_NOT_FOUND + productId, exception.getMessage());
    }

    @Test
    void testAddRentLineRestClientException() throws NoSuchFieldException, IllegalAccessException {
        // Arrange
        Long productId = 1L;
        String productCatalogUrl = "http://example.com/products/{productId}";// replace with your actual URL
        Field productCatalogUrlField = RentService.class.getDeclaredField("productCatalogUrl");
        productCatalogUrlField.setAccessible(true);
        productCatalogUrlField.set(rentService, productCatalogUrl);

        when(restTemplate.getForEntity(productCatalogUrl, GetProductResponse.class, productId)).thenThrow(new RestClientException("Error fetching product"));

        // Act and Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> rentService.addRentLine(productId, 2L, 3));

        assertEquals("Could not found the productId: " + productId, exception.getMessage());
    }

    @Test
    void testAddRentLineRestClientExceptionSecondCatch() throws NoSuchFieldException, IllegalAccessException {
        // Arrange
        Long productId = 1L;
        String productCatalogUrl = "http://example.com/products/{productId}";// replace with your actual URL
        Field productCatalogUrlField = RentService.class.getDeclaredField("productCatalogUrl");
        productCatalogUrlField.setAccessible(true);
        productCatalogUrlField.set(rentService, productCatalogUrl);

        Field productItemAvailableQtyField = RentService.class.getDeclaredField("productItemAvailableQty");
        productItemAvailableQtyField.setAccessible(true);
        productItemAvailableQtyField.set(rentService, productItemAvailableQty);


        when(restTemplate.getForEntity(productCatalogUrl, GetProductResponse.class, productId)).thenReturn(new ResponseEntity<>(HttpStatus.OK)); // Mock successful restTemplate call
        when(restTemplate.getForObject(productItemAvailableQty, Integer.class, productId)).thenThrow(new RestClientException("Error fetching available items"));

        // Act and Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> rentService.addRentLine(productId, 2L, 3));

        assertEquals(ITEM_NOT_FOUND + productId, exception.getMessage());
    }

}
