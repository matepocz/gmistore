package hu.progmasters.gmistore.repository;

import hu.progmasters.gmistore.model.ShippingMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShippingMethodRepository extends JpaRepository<ShippingMethod, Integer> {
    Optional<ShippingMethod> findByMethod(String method);
}
