package edu.uoc.epcsd.notification.application.entities;

import edu.uoc.epcsd.notification.application.kafka.EmailMessage;
import edu.uoc.epcsd.notification.application.kafka.KafkaClassListener;
import edu.uoc.epcsd.notification.application.kafka.ProductMessage;
import edu.uoc.epcsd.notification.domain.service.EmailService;
import edu.uoc.epcsd.notification.domain.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class KafkaClassListenerTest {

    @Mock
    private NotificationService notificationService;

    @Mock
    private EmailService emailService;

    private KafkaClassListener kafkaClassListener;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        kafkaClassListener = new KafkaClassListener(notificationService, emailService);
    }

    @Test
    public void testProductAvailable() {
        ProductMessage productMessage = new ProductMessage();
        EmailMessage[] emailMessages = new EmailMessage[1];
        emailMessages[0] = new EmailMessage();

        when(notificationService.notifyProductAvailable(productMessage)).thenReturn(emailMessages);

        kafkaClassListener.productAvailable(productMessage);

        verify(notificationService).notifyProductAvailable(productMessage);
        verify(emailService).sendEmail(emailMessages[0]);
    }

    @Test
    public void testListenForEmail() {
        EmailMessage emailMessage = new EmailMessage();

        kafkaClassListener.listenForEmail(emailMessage);

        verify(emailService).sendEmail(emailMessage);
    }
}