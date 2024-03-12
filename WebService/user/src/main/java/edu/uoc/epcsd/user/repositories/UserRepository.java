package edu.uoc.epcsd.user.repositories;

import edu.uoc.epcsd.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByEmail(String email);

    @Query("SELECT u.id FROM User u WHERE u.role = 'ADMIN'")
    public List<Integer> findAllAdminUsers();
}
