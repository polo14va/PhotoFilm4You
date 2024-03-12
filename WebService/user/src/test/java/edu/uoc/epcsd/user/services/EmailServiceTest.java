package edu.uoc.epcsd.user.services;

import static org.mockito.ArgumentMatchers.eq;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import edu.uoc.epcsd.user.entities.EmailMessage;

class EmailServiceTest {
    @Mock
    private  KafkaTemplate<String, EmailMessage> kafkaTemplate;
    @InjectMocks
    private EmailService emailService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }


    @Test
    void sendEmail() {
        // Create an example EmailMessage
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setSubject("Test Subject");

        // Calling the method to test
        emailService.sendEmail(emailMessage);

        // Verify that kafkaTemplate.send is called with the correct arguments
        Mockito.verify(kafkaTemplate).send(eq("email-topic"), eq(emailMessage));


    }
}