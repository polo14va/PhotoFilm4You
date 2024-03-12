package edu.uoc.epcsd.user.repositories;

import edu.uoc.epcsd.user.entities.Rent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface RentRepository extends JpaRepository<Rent, Long> {
    @Modifying
    @Query("DELETE FROM Rent WHERE id = :orderId AND estado = 'E'")
    void  deleteLineaDeCarritoById(Long orderId);

    @Query("Select o FROM Rent o WHERE o.id = :orderId AND estado = 'E'")
    Optional<Rent> findCarritoById(Long orderId);

    @Query("Select o FROM Rent o WHERE o.userId = :userId AND estado = 'E' ORDER BY o.id ASC")
    List<Rent> findCarritoByUser(Long userId);

    @Modifying
    @Query("UPDATE Rent o SET o.estado = 'P' WHERE o.userId = :userId AND estado = 'E'")
    int payCart(Long userId);

    @Modifying
    @Query("UPDATE Rent o SET o.quantity = :quantity WHERE o.id = :rentId AND estado = 'E'")
    int updateCartQty(Long rentId, int quantity);

    @Modifying
    @Query("UPDATE Rent SET orderId = :orderId WHERE id = :orderId")
    int updateOrderId(Long orderId);

    @Query("Select o FROM Rent o WHERE o.orderId = :reservationId AND estado = 'P'")
    List<Rent> getReservationsById(Long reservationId);

    @Query("Select o FROM Rent o WHERE o.userId = :userId AND estado = 'P'")
    List<Rent> getReservationsByUser(Long userId);

    @Query("Select o FROM Rent o WHERE o.orderId = :orderId AND estado = 'P'")
    List<Rent> getReservationsByOrderId(Long orderId);

    @Modifying
    @Query("UPDATE Rent o SET o.productId = :productId, o.quantity = :quantity WHERE o.id = :id AND o.userId = :userId")
    int updateReservation(Long id, Long productId, Long userId, int quantity);

    @Query("SELECT COALESCE(SUM(o.quantity), 0) FROM Rent o WHERE o.productId = :productId AND Id != :rentId")
    Optional<Integer> getReservedQty(Long productId, long rentId);

    @Query("SELECT o.productId FROM Rent o WHERE o.Id = :rentId")
    long getProductByRentId(Long rentId);
}