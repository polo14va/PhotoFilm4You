package edu.uoc.epcsd.user.controllers;

import edu.uoc.epcsd.user.controllers.RentController;
import edu.uoc.epcsd.user.controllers.dtos.*;
import edu.uoc.epcsd.user.entities.Rent;
import edu.uoc.epcsd.user.services.RentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RentControllerTest {

    @Mock
    private RentService rentService;

    @InjectMocks
    private RentController rentController;



    @Test
    void updateCartQty() {
        // Arrange
        UpdateCartQtyRequest updateCartQtyRequest = new UpdateCartQtyRequest(1, 5);
        when(rentService.updateCartQty(updateCartQtyRequest.getCartId(), updateCartQtyRequest.getQuantity())).thenReturn(1);

        // Act
        ResponseEntity<Map<String, String>> result = rentController.updateCartQty(updateCartQtyRequest);

        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().get("message")).isEqualTo("Quantity updated successfully");
    }

    @Test
    void deleteProduct() {
        // Arrange
        int cartId = 1;
        when(rentService.deleteOrderLine((long) cartId)).thenReturn(true);

        // Act
        ResponseEntity<Map<String, String>> result = rentController.deleteProduct(cartId);

        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().get("message")).isEqualTo("Product deleted successfully");
    }

    @Test
    void payCart() {
        // Arrange
        int userId = 1;
        when(rentService.payCart((long) userId)).thenReturn(true);

        // Act
        ResponseEntity<Map<String, String>> result = rentController.payCart(userId);

        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().get("message")).isEqualTo("Product deleted successfully");
    }

    @Test
    void getReservation() {
        // Arrange
        Long reservationId = 1L;
        Rent rent1 = new Rent();
        Rent rent2 = new Rent();
        List<Rent> rents = Arrays.asList(rent1, rent2);

        when(rentService.getReservationsById(reservationId)).thenReturn(rents);

        // Act
        List<Rent> result = rentController.getReservation(reservationId);

        // Assert
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(rents.size());
    }

    @Test
    void testCreateCart() {
        // Arrange
        CreateRentRequest createRentRequest = new CreateRentRequest(1L,1L,1);
        Long userId = 1L; // Adjust as needed
        List<Rent> sampleRents = new ArrayList<>(); // Adjust as needed


        when(rentService.addRentLine(anyLong(), anyLong(), anyInt())).thenReturn(userId);
        when(rentService.getCartByUser(userId)).thenReturn(sampleRents);

        // Act
        List<GetOrdersResponse> result = rentController.createCart(createRentRequest);

        // Assert
        // Add your assertions based on the expected behavior of your method and services
        assertNotNull(result);
        // You might want to check other conditions based on your implementation

        // Verify interactions with orderService
        verify(rentService).addRentLine(anyLong(), anyLong(), anyInt());
        verify(rentService).getCartByUser(userId);
    }

    @Test
    void testCreateCart_Fail() {
        // Arrange
        CreateRentRequest createRentRequest = new CreateRentRequest(1L, 1L, 1);
        Long negativeUserId = -1L; // Simulate an error condition
        List<Rent> sampleRents = new ArrayList<>(); // Adjust as needed

        when(rentService.addRentLine(anyLong(), anyLong(), anyInt())).thenReturn(negativeUserId);

        // Act
        List<GetOrdersResponse> result = rentController.createCart(createRentRequest);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty()); // Expect an empty list due to the simulated error condition

        // Verify interactions with rentService
        verify(rentService).addRentLine(anyLong(), anyLong(), anyInt());
        // Make sure that getCartByUser is not called when an error condition occurs
        verify(rentService, never()).getCartByUser(anyLong());
    }


    @Test
    void testGetCartByUser() {
        // Arrange
        Long userId = 1L;
        List<Rent> sampleRents = new ArrayList<>(); // Adjust as needed

        when(rentService.getCartByUser(userId)).thenReturn(sampleRents);

        // Act
        List<GetOrdersResponse> result = rentController.getCartByUser(userId);

        // Assert
        assertNotNull(result);

        verify(rentService).getCartByUser(userId);
    }

    @Test
    void testIsProductAvailable_EnoughStock() {
        // Arrange
        Long productId = 1L;
        int qtyAvailable = 10; // Adjust as needed

        when(rentService.getAvailableQty(productId)).thenReturn(qtyAvailable);

        // Act
        ResponseEntity<Map<String, String>> result = rentController.isProductAvailable(productId);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());

        Map<String, String> response = result.getBody();
        assertNotNull(response);
        assertEquals("Quantity available", response.get("message"));

        // Verify interactions with orderservice
        verify(rentService).getAvailableQty(productId);
    }

    @Test
    void testIsProductAvailable_NotEnoughStock() {
        // Arrange
        Long productId = 1L;
        int qtyAvailable = 0; // Simulate not enough stock

        when(rentService.getAvailableQty(productId)).thenReturn(qtyAvailable);

        // Act
        ResponseEntity<Map<String, String>> result = rentController.isProductAvailable(productId);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());

        Map<String, String> response = result.getBody();
        assertNotNull(response);
        assertEquals("Error: Not enough stock", response.get("message"));

        // Verify interactions with orderservice
        verify(rentService).getAvailableQty(productId);
    }

    @Test
    void testUpdateCartQty_NotEnoughStock() {
        // Arrange
        UpdateCartQtyRequest updateCartQty = new UpdateCartQtyRequest(/* provide necessary parameters */);
        int cartUpdated = -1; // Simulate not enough stock

        when(rentService.updateCartQty(anyLong(), anyInt())).thenReturn(cartUpdated);

        // Act
        ResponseEntity<Map<String, String>> result = rentController.updateCartQty(updateCartQty);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());

        Map<String, String> response = result.getBody();
        assertNotNull(response);
        assertEquals("Error: Not enough stock", response.get("message"));

        // Verify interactions with orderservice
        verify(rentService).updateCartQty(updateCartQty.getCartId(), updateCartQty.getQuantity());
    }


    @Test
    void testUpdateCartQty_NotFound() {
        // Arrange
        UpdateCartQtyRequest updateCartQty = new UpdateCartQtyRequest(/* provide necessary parameters */);
        int cartUpdated = 0; // Simulate not found

        when(rentService.updateCartQty(anyLong(), anyInt())).thenReturn(cartUpdated);

        // Act
        ResponseEntity<Map<String, String>> result = rentController.updateCartQty(updateCartQty);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());


        verify(rentService).updateCartQty(updateCartQty.getCartId(), updateCartQty.getQuantity());
    }

    @Test
    void testDeleteProduct_NotFound() {
        // Arrange
        int cartId = 1; // Provide a valid cartId
        when(rentService.deleteOrderLine(anyLong())).thenReturn(false);

        // Act
        ResponseEntity<Map<String, String>> result = rentController.deleteProduct(cartId);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());

        // Verify interactions with orderservice
        verify(rentService).deleteOrderLine((long) cartId);
    }

    @Test
    void testPayCart_NotFound() {
        // Arrange
        int userId = 1; // Provide a valid userId
        when(rentService.payCart(anyLong())).thenReturn(false);

        // Act
        ResponseEntity<Map<String, String>> result = rentController.payCart(userId);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());


        // Verify interactions with orderservice
        verify(rentService).payCart((long) userId);
    }

    @Test
    void testGetReservation() {
        // Arrange
        int userId = 1; // Provide a valid userId
        List<Rent> sampleReservations = new ArrayList<>(); // Adjust as needed

        when(rentService.getReservationsByUser(anyLong())).thenReturn(sampleReservations);

        // Act
        List<GetOrdersResponse> result = rentController.getReservation(userId);

        // Assert
        assertNotNull(result);

        verify(rentService).getReservationsByUser((long) userId);
    }


    @Test
    void testGetReservationByOrderId() {
        // Arrange
        int userId = 1; // Provide a valid userId
        List<Rent> sampleReservations = new ArrayList<>(); // Adjust as needed

        when(rentService.getReservationsByOrderId(anyLong())).thenReturn(sampleReservations);

        // Act
        List<GetOrdersResponse> result = rentController.getReservationByOrderId(userId);

        // Assert
        assertNotNull(result);


        // Verify interactions with orderservice
        verify(rentService).getReservationsByOrderId((long) userId);
    }

    @Test
    void testUpdateReservation() {
        // Arrange
        UpdateReservationRequest updateReservationRequest = new UpdateReservationRequest(1L,1L,1L,1);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(rentController).build();
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        Long orderId = 1L;
        when(rentService.updateReservation(updateReservationRequest.getId(),
                updateReservationRequest.getProductId(),
                updateReservationRequest.getUserId(),
                updateReservationRequest.getQuantity())).thenReturn(1L);


        // Act
        ResponseEntity<Long> responseEntity = rentController.updateReservation(updateReservationRequest);

        // Assert
        assertEquals(201, responseEntity.getStatusCodeValue()); // Check if created status
        assertEquals(orderId, responseEntity.getBody()); // Check if the body matches the orderId

        // You can also check if the URI is constructed correctly if needed
        URI expectedUri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(orderId).toUri();
        assertEquals(expectedUri, responseEntity.getHeaders().getLocation());
    }

    @Test
    void testStockReservedByProduct() {
        // Arrange
        int productId = 1;
        int reservedQty = 10;
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(rentController).build();
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(rentService.getStockReservedByProduct(productId)).thenReturn(reservedQty);

        // Act
        ResponseEntity<Integer> responseEntity = rentController.stockReservedByProduct(productId);

        // Assert
        assertEquals(200, responseEntity.getStatusCodeValue()); // Check if OK status
        assertEquals(reservedQty, responseEntity.getBody()); // Check if the body matches the reservedQty
    }

}


