package edu.uoc.epcsd.productcatalog.application.rest.response;

import edu.uoc.epcsd.productcatalog.domain.Product;
import lombok.*;

@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
public class GetProductsAndAvailabilityResponse {

    private final Long id;

    private final String name;

    private final String description;

    private final Double dailyPrice;

    private final String brand;

    private final String model;

    private final Long categoryId;
    
    private final int quantityAvailable;

    public static GetProductsAndAvailabilityResponse fromDomain(Product product, int quantity) {
        return GetProductsAndAvailabilityResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .dailyPrice(product.getDailyPrice())
                .brand(product.getBrand())
                .model(product.getModel())
                .categoryId(product.getCategoryId())
                .quantityAvailable(quantity)
                .build();
    }
}
