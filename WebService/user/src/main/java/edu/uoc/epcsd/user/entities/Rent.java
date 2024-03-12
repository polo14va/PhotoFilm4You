package edu.uoc.epcsd.user.entities;
import lombok.*;

import jakarta.persistence.*;

@Entity(name = "Rent")
@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="`Rent`")
public class Rent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "orderId")
    private long orderId;

    @Column(name = "product")
    private Long productId;

    @Column(name = "userId")
    private Long userId;

    @Column(name="estado")
    private char estado;

    @Column(name="quantity")
    private int quantity;
}
