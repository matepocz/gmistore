package hu.progmasters.gmistore.repository;

import hu.progmasters.gmistore.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
