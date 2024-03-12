package edu.uoc.epcsd.notification.domain.service;

import edu.uoc.epcsd.notification.application.rest.dtos.GetProductResponse;
import edu.uoc.epcsd.notification.application.rest.dtos.GetUserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

@Service
public class RestClientImpl implements RestClient {

    @Value("${userService.getUsersToAlert.url}")
    private String userServiceUrl;

    @Value("${productService.getProductDetails.url}")
    private String productServiceUrl;

    private final RestTemplate restTemplate;

    @Autowired
    public RestClientImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public GetProductResponse getProductDetails(Long productId) {
        return restTemplate.getForObject(productServiceUrl + productId, GetProductResponse.class);
    }

    @Override
    public GetUserResponse[] getUsersToAlert(Long productId, LocalDate date) {
        ResponseEntity<GetUserResponse[]> response = restTemplate.exchange(
                userServiceUrl  + productId + "&availableOnDate=" + date,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        return response.getBody();
    }
}

