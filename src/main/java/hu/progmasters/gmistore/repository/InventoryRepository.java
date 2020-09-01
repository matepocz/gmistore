package hu.progmasters.gmistore.repository;

import hu.progmasters.gmistore.model.Inventory;
import hu.progmasters.gmistore.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByProduct(Product product);
}
