package edu.uoc.epcsd.user.repositories;

import edu.uoc.epcsd.user.entities.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {

    @Query("SELECT a FROM Alert a WHERE a.productId = ?1 AND a.from <= ?2 AND a.to >= ?2")
    List<Alert> findAlertsByProductIdAndInterval(Long productId, LocalDate availableOnDate);

    @Query("SELECT a FROM Alert a WHERE a.productId = ?1 AND a.to = ?2")
    List<Alert> findByProductIdAndAlertDate(Long productId, LocalDate alertDate);

    @Query("SELECT a FROM Alert a WHERE a.user.id = ?1 AND a.from <= ?2 AND a.to >= ?2")
    List<Alert> findByUserIdAndDateInterval(Long userId, LocalDate fromDate, LocalDate toDate);
}
