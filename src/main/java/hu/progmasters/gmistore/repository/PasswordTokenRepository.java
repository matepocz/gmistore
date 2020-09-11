package hu.progmasters.gmistore.repository;

import hu.progmasters.gmistore.model.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordTokenRepository extends JpaRepository<PasswordResetToken,Long> {
}
