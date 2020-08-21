package hu.progmasters.gmistore.service;

import hu.progmasters.gmistore.dto.RatingDetails;
import hu.progmasters.gmistore.model.Rating;
import hu.progmasters.gmistore.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RatingService {

    private final RatingRepository ratingRepository;

    @Autowired
    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    public List<RatingDetails> getRatingsByProductSlug(String productSlug) {
        return ratingRepository.findAllByProductSlug(productSlug)
                .stream()
                .filter(Rating::getActive)
                .map(RatingDetails::new)
                .collect(Collectors.toList());
    }
}
