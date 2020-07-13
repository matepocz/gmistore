package hu.progmasters.gmistore.repository;

import hu.progmasters.gmistore.model.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, String> {
    ConfirmationToken findByToken(String token);
}
