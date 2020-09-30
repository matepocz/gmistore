package hu.progmasters.gmistore.repository;

import hu.progmasters.gmistore.model.Product;
import hu.progmasters.gmistore.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    List<Rating> findAllByProductSlug(String productSlug);

    @Query("SELECT rating FROM Rating rating WHERE rating.username = :name AND rating.product = :product")
    Optional<Rating> findRatingByUsername(@Param("name") String name, @Param("product")Product product);
}
