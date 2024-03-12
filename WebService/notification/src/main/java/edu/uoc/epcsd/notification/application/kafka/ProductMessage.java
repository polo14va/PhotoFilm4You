package edu.uoc.epcsd.notification.application.kafka;

import lombok.*;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductMessage {

    private Long productId;

}
