package edu.uoc.epcsd.user.controllers.dtos;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateReservationRequest {

    private Long id;
    private Long userId;
    private Long productId;

    private int quantity;
}