package hu.progmasters.gmistore.controller;

import hu.progmasters.gmistore.dto.RatingDetails;
import hu.progmasters.gmistore.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/ratings")
public class RatingController {

    private final RatingService ratingService;

    @Autowired
    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @GetMapping("/{productSlug}")
    public ResponseEntity<List<RatingDetails>> getRatingsByProductSlug(
            @PathVariable("productSlug") String productSlug) {
        List<RatingDetails> ratings = ratingService.getRatingsByProductSlug(productSlug);
        return new ResponseEntity<>(ratings, HttpStatus.OK);
    }
}
