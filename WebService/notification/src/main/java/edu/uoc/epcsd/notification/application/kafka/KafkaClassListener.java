package edu.uoc.epcsd.notification.application.kafka;

import edu.uoc.epcsd.notification.domain.service.EmailService;
import edu.uoc.epcsd.notification.domain.service.NotificationService;
import lombok.RequiredArgsConstructor;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class KafkaClassListener {

    private final NotificationService notificationService;
    private final EmailService emailService;

    @KafkaListener(topics = "product.unit_available", groupId = "group-1", containerFactory ="kafkaListenerContainerFactory")
	public void productAvailable(ProductMessage productMessage) {

        EmailMessage[] messages =  notificationService.notifyProductAvailable(productMessage);

        for (int i = 0; i < messages.length; i++) {
            emailService.sendEmail(messages[i]);
        }

    }

    @KafkaListener(topics = "email-topic", groupId = "email-group", containerFactory = "emailKafkaListenerContainerFactory")
    public void listenForEmail(EmailMessage emailMessage) {
        emailService.sendEmail(emailMessage);
    }
}
