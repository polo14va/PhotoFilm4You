package edu.uoc.epcsd.user.controllers;

import edu.uoc.epcsd.user.controllers.dtos.CreateAlertRequest;
import edu.uoc.epcsd.user.entities.Alert;
import edu.uoc.epcsd.user.services.AlertService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class AlertControllerTest {

    @Mock
    private AlertService alertService;

    @InjectMocks
    private AlertController alertController;

    @Test
    void getAllAlerts() {
        // Arrange
        Alert alert1 = new Alert();
        Alert alert2 = new Alert();
        List<Alert> alerts = Arrays.asList(alert1, alert2);

        when(alertService.findAll()).thenReturn(alerts);

        // Act
        List<Alert> result = alertController.getAllAlerts();

        // Assert
        assertThat(result).isEqualTo(alerts);
    }

    @Test
    void getAlertById() {
        // Arrange
        Long alertId = 1L;
        Alert alert = new Alert();

        when(alertService.findById(alertId)).thenReturn(Optional.of(alert));

        // Act
        ResponseEntity<Alert> result = alertController.getAlertById(alertId);

        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(alert);
    }

    @Test
    void getAlertsByProductAndDate() {
        // Arrange
        Long productId = 1L;
        LocalDate alertDate = LocalDate.now();
        Alert alert1 = new Alert();
        Alert alert2 = new Alert();
        List<Alert> alerts = Arrays.asList(alert1, alert2);

        when(alertService.getAlertsByProductAndDate(productId, alertDate)).thenReturn(alerts);

        // Act
        ResponseEntity<List<Alert>> result = alertController.getAlertsByProductAndDate(productId, alertDate);

        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(alerts);
    }

    @Test
    void getAlertsByUserAndDateInterval() {
        // Arrange
        Long userId = 1L;
        LocalDate fromDate = LocalDate.now().minusDays(7);
        LocalDate toDate = LocalDate.now();
        Alert alert1 = new Alert();
        Alert alert2 = new Alert();
        List<Alert> alerts = Arrays.asList(alert1, alert2);

        when(alertService.getAlertsByUserAndDateInterval(userId, fromDate, toDate)).thenReturn(alerts);

        // Act
        ResponseEntity<List<Alert>> result = alertController.getAlertsByUserAndDateInterval(userId, fromDate, toDate);

        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(alerts);
    }

    @Test
    void testCreateAlert() {
        // Arrange
        CreateAlertRequest createAlertRequest = new CreateAlertRequest(1L,1L,LocalDate.now(),LocalDate.now());

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(alertController).build();
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        Long alertId = 123L;
        Alert alert = Alert.builder().id(alertId).build();
        when(alertService.createAlert(createAlertRequest.getProductId(),
                createAlertRequest.getUserId(),
                createAlertRequest.getFrom(),
                createAlertRequest.getTo())).thenReturn(alert);

        // Act
        ResponseEntity<Long> responseEntity = alertController.createAlert(createAlertRequest);

        // Assert
        assertEquals(201, responseEntity.getStatusCodeValue()); // Check if created status
        assertEquals(alertId, responseEntity.getBody()); // Check if the body matches the alertId

        // You can also check if the URI is constructed correctly if needed
        URI expectedUri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(alertId).toUri();
        assertEquals(expectedUri, responseEntity.getHeaders().getLocation());
    }

    @Test
    void testCreateAlertWithIllegalArgumentException() {
        // Arrange
        CreateAlertRequest createAlertRequest = new CreateAlertRequest(1L,1L,LocalDate.now(),LocalDate.now());


        when(alertService.createAlert(createAlertRequest.getProductId(),
                createAlertRequest.getUserId(),
                createAlertRequest.getFrom(),
                createAlertRequest.getTo())).thenThrow(new IllegalArgumentException("Invalid date range"));

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> alertController.createAlert(createAlertRequest));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode()); // Check if the status code is BAD_REQUEST
        // You can further assert the exception details if needed
    }




}
