package edu.uoc.epcsd.notification.domain.service;

import edu.uoc.epcsd.notification.application.kafka.EmailMessage;
import edu.uoc.epcsd.notification.application.kafka.ProductMessage;
import edu.uoc.epcsd.notification.application.rest.dtos.GetProductResponse;
import edu.uoc.epcsd.notification.application.rest.dtos.GetUserResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


@Service
public class NotificationServiceImpl implements NotificationService {

    private final RestClient restClient;

    public NotificationServiceImpl( RestClient restClient) {

        this.restClient = restClient;
    }

    @Override
    public EmailMessage[] notifyProductAvailable(ProductMessage productMessage) {
        GetProductResponse product = restClient.getProductDetails(productMessage.getProductId());
        GetUserResponse[] usersToAlert = restClient.getUsersToAlert(productMessage.getProductId(), LocalDate.now());

        if (usersToAlert == null || usersToAlert.length == 0 || product == null) {
            return new EmailMessage[0];
        }

        EmailMessage[] messages = new EmailMessage[usersToAlert.length];
        int count = 0;
        for (GetUserResponse user : usersToAlert) {
            EmailMessage emailMessage = new EmailMessage(
                    user.getEmail(),
                    "¡Producto disponible!",
                    "correo.html",
                    "El producto \"" + product.getName() + "\" ya está disponible.",
                    "http://localhost/product/" + product.getId(),
                    "Ver detalles"
            );
            messages[count] = emailMessage;
            count++;
        }
        return messages;

    }

}
