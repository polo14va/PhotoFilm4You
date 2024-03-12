package edu.uoc.epcsd.notification.domain.service;

import edu.uoc.epcsd.notification.application.rest.dtos.GetProductResponse;
import edu.uoc.epcsd.notification.application.rest.dtos.GetUserResponse;

import java.time.LocalDate;

public interface RestClient {
    GetProductResponse getProductDetails(Long productId);
    GetUserResponse[] getUsersToAlert(Long productId, LocalDate date);
}
