package edu.uoc.epcsd.user.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import edu.uoc.epcsd.user.controllers.dtos.GetProductResponse;
import edu.uoc.epcsd.user.entities.Alert;
import edu.uoc.epcsd.user.entities.User;
import edu.uoc.epcsd.user.repositories.AlertRepository;

class AlertServiceTest {
    @Mock
    private  AlertRepository alertRepository;
    @Mock
    private  UserService userService;

    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private AlertService alertService;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void testFindAll() {
        Alert alert1 = new Alert();
        alert1.setId(1L);
        Alert alert2 = new Alert();
        alert2.setId(2L);

        List<Alert> mockAlerts = Arrays.asList(alert1, alert2);

        when(alertRepository.findAll()).thenReturn(mockAlerts);

        List<Alert> result = alertService.findAll();

        assertEquals(mockAlerts, result);

        verify(alertRepository).findAll();
    }

    @Test
    void testFindById() {

        Alert mockAlert = new Alert();
        mockAlert.setId(1L);

        when(alertRepository.findById(1L)).thenReturn(Optional.of(mockAlert));

        Optional<Alert> result = alertService.findById(1L);


        assertTrue(result.isPresent());
        assertEquals(mockAlert, result.get());

        verify(alertRepository).findById(1L);
    }

    @Test
    void testCreateAlert() throws NoSuchFieldException, IllegalAccessException {
        // Arrange
        Long productId = 1L;
        Long userId = 2L;
        LocalDate from = LocalDate.now();
        LocalDate to = from.plusDays(7);

        String mockedPropertyValue = "https://mocked-url";

        // Use reflection to set the field value
        Field field = AlertService.class.getDeclaredField("productCatalogUrl");
        field.setAccessible(true);
        field.set(alertService, mockedPropertyValue);

        Alert expectedAlert = Alert.builder()
                .from(from)
                .to(to)
                .user(new User())
                .productId(productId)
                .build();

        when(userService.findById(userId)).thenReturn(Optional.of(new User()));
        when(restTemplate.getForEntity(eq(mockedPropertyValue), eq(GetProductResponse.class), eq(productId)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
        when(alertRepository.save(expectedAlert)).thenReturn(expectedAlert);

        // Act
        Alert createdAlert = alertService.createAlert(productId, userId, from, to);

        // Assert
        assertNotNull(createdAlert);
        assertEquals(expectedAlert, createdAlert);

        // Verify interactions
        verify(userService).findById(userId);
        verify(restTemplate).getForEntity(eq(mockedPropertyValue), eq(GetProductResponse.class), eq(productId));
        verify(alertRepository).save(expectedAlert);
    }

    @Test
    void testCreateAlert_UserNotFound() {
        // Arrange
        Long productId = 1L;
        Long userId = 2L;
        LocalDate from = LocalDate.now();
        LocalDate to = from.plusDays(7);

        // Mocking user not found scenario
        when(userService.findById(userId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> alertService.createAlert(productId, userId, from, to),
                "Expected IllegalArgumentException when user is not found");

        // Verify interaction
        verify(userService).findById(userId);
        verifyNoInteractions(restTemplate, alertRepository);
    }




    @Test
    void testGetAlertsByProductAndDate() {
        // Arrange
        Long productId = 1L;
        LocalDate alertDate = LocalDate.now();
        List<Alert> expectedAlerts = Arrays.asList(
                new Alert(1L, alertDate, alertDate, productId, new User()),
                new Alert(2L, alertDate, alertDate, productId, new User())
        );

        // Mocking the repository to return alerts for the given product and date
        when(alertRepository.findByProductIdAndAlertDate(productId, alertDate)).thenReturn(expectedAlerts);

        // Act
        List<Alert> actualAlerts = alertService.getAlertsByProductAndDate(productId, alertDate);

        // Assert
        assertEquals(expectedAlerts, actualAlerts);

        // Verify interaction with the repository
        verify(alertRepository).findByProductIdAndAlertDate(productId, alertDate);
    }

    @Test
    void testGetAlertsByUserAndDateInterval() {
        // Arrange
        Long userId = 1L;
        LocalDate fromDate = LocalDate.now();
        LocalDate toDate = fromDate.plusDays(7);
        List<Alert> expectedAlerts = Arrays.asList(
                new Alert(1L, fromDate, toDate, 1L, new User()),
                new Alert(2L, fromDate, toDate, 1L, new User())
        );

        // Mocking the repository to return alerts for the given user and date interval
        when(alertRepository.findByUserIdAndDateInterval(userId, fromDate, toDate)).thenReturn(expectedAlerts);

        // Act
        List<Alert> actualAlerts = alertService.getAlertsByUserAndDateInterval(userId, fromDate, toDate);

        // Assert
        assertEquals(expectedAlerts, actualAlerts);

        // Verify interaction with the repository
        verify(alertRepository).findByUserIdAndDateInterval(userId, fromDate, toDate);
    }
}