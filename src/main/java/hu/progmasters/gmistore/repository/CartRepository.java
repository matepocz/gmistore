package hu.progmasters.gmistore.repository;

import hu.progmasters.gmistore.model.Cart;
import hu.progmasters.gmistore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}
