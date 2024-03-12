package edu.uoc.epcsd.notification.application.domainTest;

import edu.uoc.epcsd.notification.application.rest.dtos.GetProductResponse;
import edu.uoc.epcsd.notification.application.rest.dtos.GetUserResponse;
import edu.uoc.epcsd.notification.domain.service.RestClientImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RestClientImplTest {

    @Mock
    private RestTemplate restTemplate;

    private RestClientImpl restClient;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        restClient = new RestClientImpl(restTemplate);
    }

    @Test
    void testGetProductDetails() {
        // Mocking the response from the external service
        Long productId = 123L;
        GetProductResponse expectedResponse = new GetProductResponse();
        expectedResponse.setId(123L);
        expectedResponse.setName("Test Product");
        expectedResponse.setDescription("Description of the product");
        expectedResponse.setDailyPrice(19.99);
        expectedResponse.setBrand("Brand");
        expectedResponse.setModel("Model");
        expectedResponse.setCategoryId(456L);

        when(restTemplate.getForObject(anyString(), eq(GetProductResponse.class)))
                .thenReturn(expectedResponse);

        GetProductResponse actualResponse = restClient.getProductDetails(productId);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void testGetUsersToAlert() {
        Long productId = 123L;
        LocalDate date = LocalDate.now();

        GetUserResponse user1 = new GetUserResponse();
        user1.setEmail("test1@example.com");

        GetUserResponse user2 = new GetUserResponse();
        user2.setEmail("test2@example.com");

        GetUserResponse[] expectedResponse = {user1, user2};

        ResponseEntity<GetUserResponse[]> responseEntity = ResponseEntity.ok(expectedResponse);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                eq(null),
                any(ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);

        GetUserResponse[] actualResponse = restClient.getUsersToAlert(productId, date);
        assertEquals(expectedResponse, actualResponse);
    }


}
