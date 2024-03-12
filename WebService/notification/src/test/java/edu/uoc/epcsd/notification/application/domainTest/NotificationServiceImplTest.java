package edu.uoc.epcsd.notification.application.domainTest;
import edu.uoc.epcsd.notification.application.kafka.EmailMessage;
import edu.uoc.epcsd.notification.application.kafka.KafkaClassListener;
import edu.uoc.epcsd.notification.application.kafka.ProductMessage;
import edu.uoc.epcsd.notification.application.rest.dtos.GetProductResponse;
import edu.uoc.epcsd.notification.application.rest.dtos.GetUserResponse;
import edu.uoc.epcsd.notification.domain.service.EmailService;
import edu.uoc.epcsd.notification.domain.service.NotificationServiceImpl;
import edu.uoc.epcsd.notification.domain.service.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class NotificationServiceImplTest {

    @InjectMocks
    private KafkaClassListener kafkaClassListener;
    @Mock
    private EmailService emailService;
    @Mock
    private NotificationServiceImpl notificationService;

    @Mock
    private RestClient restClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testProductAvailableNotification() {

        ProductMessage productMessage = new ProductMessage();
        productMessage.setProductId(123L);


        EmailMessage[] emailMessages = {new EmailMessage(
                "prueba@gmail.com",
                "¡Producto disponible!",
                "correo.html",
                "El producto X ya está disponible.",
                "http://localhost/product/" + productMessage.getProductId(),
                "Ver detalles"
        )};
        when(notificationService.notifyProductAvailable(productMessage)).thenReturn(emailMessages);
        kafkaClassListener.productAvailable(productMessage);

        for (EmailMessage emailMessage : emailMessages) {
            verify(emailService).sendEmail(emailMessage);
        }
    }


    @Test
    void testNotifyProductAvailable_ProductAndUsersNotNull() {

        NotificationServiceImpl notificationService = new NotificationServiceImpl(restClient);
        // Arrange
        ProductMessage productMessage = new ProductMessage();
        productMessage.setProductId(123L);

        GetProductResponse product = new GetProductResponse();
        product.setId(123L);
        product.setName("Producto de prueba");

        GetUserResponse user1 = new GetUserResponse();
        user1.setEmail("test1@example.com");

        GetUserResponse user2 = new GetUserResponse();
        user2.setEmail("test2@example.com");

        when(restClient.getProductDetails(productMessage.getProductId())).thenReturn(product);
        when(restClient.getUsersToAlert(productMessage.getProductId(), LocalDate.now())).thenReturn(new GetUserResponse[]{user1, user2});

        // Act
        EmailMessage[] emailMessages = notificationService.notifyProductAvailable(productMessage);

        // Assert
        assertNotNull(emailMessages);
        assertEquals(2, emailMessages.length);

        // Verificar que los EmailMessage se han creado correctamente
        assertEquals("test1@example.com", emailMessages[0].getTo());
        assertEquals("¡Producto disponible!", emailMessages[0].getSubject());
        assertEquals("correo.html", emailMessages[0].getTemplateName());
        assertEquals("El producto \"Producto de prueba\" ya está disponible.", emailMessages[0].getBody());
        assertEquals("http://localhost/product/123", emailMessages[0].getButtonLink());
        assertEquals("Ver detalles", emailMessages[0].getButtonText());

        assertEquals("test2@example.com", emailMessages[1].getTo());
        assertEquals("¡Producto disponible!", emailMessages[1].getSubject());
        assertEquals("correo.html", emailMessages[1].getTemplateName());
        assertEquals("El producto \"Producto de prueba\" ya está disponible.", emailMessages[1].getBody());
        assertEquals("http://localhost/product/123", emailMessages[1].getButtonLink());
        assertEquals("Ver detalles", emailMessages[1].getButtonText());
    }

    @Test
    void testNotifyProductAvailable_UsersNull() {
        ProductMessage productMessage = new ProductMessage();
        productMessage.setProductId(123L);

        GetProductResponse product = new GetProductResponse();
        product.setId(123L);
        product.setName("Producto de prueba");

        when(restClient.getProductDetails(productMessage.getProductId())).thenReturn(product);
        when(restClient.getUsersToAlert(productMessage.getProductId(), LocalDate.now())).thenReturn(null);

        EmailMessage[] emailMessages = notificationService.notifyProductAvailable(productMessage);

        assertNull( emailMessages);
    }


    @Test
    void testNotifyProductAvailable_UsersEmpty() {
        ProductMessage productMessage = new ProductMessage();
        productMessage.setProductId(123L);

        GetProductResponse product = new GetProductResponse();
        product.setId(123L);
        product.setName("Producto de prueba");

        when(restClient.getProductDetails(productMessage.getProductId())).thenReturn(product);
        when(restClient.getUsersToAlert(productMessage.getProductId(), LocalDate.now())).thenReturn(new GetUserResponse[0]);
        EmailMessage[] emailMessages = notificationService.notifyProductAvailable(productMessage);

        assertNull( emailMessages);
    }


    @Test
    void testNotifyProductAvailable_ProductNull() {
        ProductMessage productMessage = new ProductMessage();
        productMessage.setProductId(123L);

        GetUserResponse user1 = new GetUserResponse();
        user1.setEmail("test1@example.com");

        GetUserResponse user2 = new GetUserResponse();
        user2.setEmail("test2@example.com");

        GetUserResponse[] usersToAlert = new GetUserResponse[]{user1, user2};

        when(restClient.getProductDetails(productMessage.getProductId())).thenReturn(null);
        when(restClient.getUsersToAlert(productMessage.getProductId(), LocalDate.now())).thenReturn(usersToAlert);

        EmailMessage[] emailMessages = notificationService.notifyProductAvailable(productMessage);

        assertNull( emailMessages);
    }

    @Test
    void testNotifyProductAvailable() {
        // Arrange
        ProductMessage productMessage = new ProductMessage();
        productMessage.setProductId(123L);

        GetProductResponse product = new GetProductResponse();
        product.setId(123L);
        product.setName("Producto de prueba");

        GetUserResponse user1 = new GetUserResponse();
        user1.setEmail("test1@example.com");

        GetUserResponse user2 = new GetUserResponse();
        user2.setEmail("test2@example.com");

        when(restClient.getProductDetails(productMessage.getProductId())).thenReturn(product);
        when(restClient.getUsersToAlert(productMessage.getProductId(), LocalDate.now())).thenReturn(new GetUserResponse[]{user1, user2});

        // Act
        EmailMessage[] emailMessages = notificationService.notifyProductAvailable(productMessage);

        // Assert
        assertNull(emailMessages);
    }

    @Test
    void testNotifyProductAvailable_ProductNull2() {
        NotificationServiceImpl notificationService = new NotificationServiceImpl(restClient);
        // Arrange
        ProductMessage productMessage = new ProductMessage();
        productMessage.setProductId(123L);

        // Simula que restClient.getProductDetails() retorna un producto nulo
        when(restClient.getProductDetails(productMessage.getProductId())).thenReturn(null);

        // Act
        EmailMessage[] emailMessages = notificationService.notifyProductAvailable(productMessage);

        // Assert
        assertEquals(0, emailMessages.length);
    }
}
