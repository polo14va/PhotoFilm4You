package edu.uoc.epcsd.notification.application.kafka;

import edu.uoc.epcsd.notification.application.rest.dtos.GetProductResponse;
import edu.uoc.epcsd.notification.application.rest.dtos.GetUserResponse;
import edu.uoc.epcsd.notification.domain.service.RestClient;

import java.time.LocalDate;

public class MockRestClient implements RestClient {
    @Override
    public GetProductResponse getProductDetails(Long productId) {
        // Simula la respuesta del servicio de productos
        GetProductResponse mockProductResponse = new GetProductResponse();
        mockProductResponse.setName("Producto de Prueba");
        return mockProductResponse;
    }

    @Override
    public GetUserResponse[] getUsersToAlert(Long productId, LocalDate date) {
        // Simula la respuesta del servicio de usuarios
        GetUserResponse user1 = new GetUserResponse(1L, "User One", "user1@example.com", "1234567890");
        GetUserResponse user2 = new GetUserResponse(2L, "User Two", "user2@example.com", "0987654321");
        return new GetUserResponse[]{user1, user2};
    }
}
