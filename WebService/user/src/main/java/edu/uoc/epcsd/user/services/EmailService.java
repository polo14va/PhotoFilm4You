package edu.uoc.epcsd.user.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import edu.uoc.epcsd.user.entities.EmailMessage;

@Service
public class EmailService {


    private final KafkaTemplate<String, EmailMessage> kafkaTemplate;


    @Autowired
    public EmailService(KafkaTemplate<String, EmailMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEmail(EmailMessage emailMessage) {
        kafkaTemplate.send("email-topic", emailMessage);
    }
}