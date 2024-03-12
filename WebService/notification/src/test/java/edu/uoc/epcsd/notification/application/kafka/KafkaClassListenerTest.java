package edu.uoc.epcsd.notification.application.kafka;

import edu.uoc.epcsd.notification.domain.service.EmailService;
import edu.uoc.epcsd.notification.domain.service.NotificationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.Closeable;

import static org.mockito.Mockito.verify;

class KafkaClassListenerTest {

    @Mock
    private NotificationService notificationService;

    @Mock
    private EmailService emailService;

    private KafkaClassListener kafkaClassListener;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        kafkaClassListener = new KafkaClassListener(notificationService, emailService);
    }



    @Test
    void testListenForEmail() {
        EmailMessage emailMessage = new EmailMessage();
        kafkaClassListener.listenForEmail(emailMessage);

        verify(emailService).sendEmail(emailMessage);
    }
}