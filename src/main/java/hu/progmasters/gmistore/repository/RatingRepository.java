package hu.progmasters.gmistore.repository;

import hu.progmasters.gmistore.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    List<Rating> findAllByProductSlug(String productSlug);
}
