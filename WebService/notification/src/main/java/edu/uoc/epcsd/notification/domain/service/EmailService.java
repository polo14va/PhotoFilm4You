package edu.uoc.epcsd.notification.domain.service;

import edu.uoc.epcsd.notification.application.kafka.EmailMessage;

public interface EmailService {
    void sendEmail(EmailMessage email);
}
