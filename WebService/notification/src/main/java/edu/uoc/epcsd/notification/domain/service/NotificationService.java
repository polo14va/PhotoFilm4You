package edu.uoc.epcsd.notification.domain.service;

import edu.uoc.epcsd.notification.application.kafka.EmailMessage;
import edu.uoc.epcsd.notification.application.kafka.ProductMessage;

public interface NotificationService {
    EmailMessage[] notifyProductAvailable(ProductMessage productMessage);
}
