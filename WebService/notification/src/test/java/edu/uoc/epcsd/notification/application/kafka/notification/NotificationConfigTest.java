package edu.uoc.epcsd.notification.application.kafka.notification;

import edu.uoc.epcsd.notification.application.configuration.NotificationConfig;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class NotificationConfigTest {

    @Test
    void testRestTemplateBean() {
        NotificationConfig config = new NotificationConfig();
        RestTemplate restTemplate = config.restTemplate();

        // Verificar que el bean RestTemplate se haya configurado correctamente
        assertNotNull(restTemplate, "RestTemplate bean should not be null");
    }
}