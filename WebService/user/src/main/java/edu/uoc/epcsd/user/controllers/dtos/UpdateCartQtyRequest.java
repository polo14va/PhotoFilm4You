package edu.uoc.epcsd.user.controllers.dtos;
import lombok.*;

@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class UpdateCartQtyRequest {

    private long cartId;

    private int quantity;
}
