package hu.progmasters.gmistore.repository;

import hu.progmasters.gmistore.model.PasswordResetToken;
import hu.progmasters.gmistore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordTokenRepository extends JpaRepository<PasswordResetToken,Long> {
    PasswordResetToken findByToken(String token);
}
