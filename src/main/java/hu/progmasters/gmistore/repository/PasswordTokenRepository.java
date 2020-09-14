package hu.progmasters.gmistore.repository;

import hu.progmasters.gmistore.model.PasswordResetToken;
import hu.progmasters.gmistore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PasswordTokenRepository extends JpaRepository<PasswordResetToken,Long> {
    @Query("Select t from PasswordResetToken t where t.token=:token")
    PasswordResetToken findByToken(@Param("token") String token);

    PasswordResetToken findPasswordResetTokenByUser(User user);
    void deleteByUser(User user);
}
