package hu.progmasters.gmistore.repository;

import hu.progmasters.gmistore.model.Product;
import hu.progmasters.gmistore.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findAllByProduct(Product product);
}
