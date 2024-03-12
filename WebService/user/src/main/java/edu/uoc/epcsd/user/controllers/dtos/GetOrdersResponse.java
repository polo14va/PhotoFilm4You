package edu.uoc.epcsd.user.controllers.dtos;

import lombok.*;

@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
public class GetOrdersResponse {

    private Long userId;

    private GetProductResponse product;

    private int quantity;

    private double price;

    private double tax;

    private long rentId;

    private long orderId;


    public static GetOrdersResponse fromDomain(Long userId, GetProductResponse productResponse,
                                               int quantity, double price, double tax, long rentId,
                                               long orderId) {
        return GetOrdersResponse.builder()
                .userId(userId)
                .product(productResponse)
                .quantity(quantity)
                .price(price)
                .tax(tax)
                .rentId(rentId)
                .orderId(orderId)
                .build();
    }
}