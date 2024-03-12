package edu.uoc.epcsd.user;

import edu.uoc.epcsd.user.entities.Alert;
import edu.uoc.epcsd.user.repositories.AlertRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AlertImplementationTest {

    @Mock
    private AlertRepository alertRepository;

    private Alert testAlert;

    @BeforeEach
    void setUp() {
        testAlert = Alert.builder().id(1L).productId(1L).build();
        when(alertRepository.findById(1L)).thenReturn(Optional.of(testAlert));
    }

    @Test
    void whenFindAlertById_thenShouldReturnIt() {
        Optional<Alert> found = alertRepository.findById(1L);

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(testAlert.getId());
    }
}