package hu.progmasters.gmistore.repository;

import hu.progmasters.gmistore.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findProductByName(String name);
    List<Product> findProductsByAddedBy(String name);
}
