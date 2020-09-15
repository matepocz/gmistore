package hu.progmasters.gmistore.controller;

import hu.progmasters.gmistore.dto.NewRatingRequest;
import hu.progmasters.gmistore.dto.RatingDetails;
import hu.progmasters.gmistore.dto.RatingInitData;
import hu.progmasters.gmistore.service.RatingService;
import hu.progmasters.gmistore.validator.RatingRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/api/ratings")
public class RatingController {

    private final RatingService ratingService;
    private final RatingRequestValidator ratingRequestValidator;

    @Autowired
    public RatingController(RatingService ratingService, RatingRequestValidator ratingRequestValidator) {
        this.ratingService = ratingService;
        this.ratingRequestValidator = ratingRequestValidator;
    }

    @InitBinder
    public void bind(WebDataBinder binder) {
        binder.addValidators(ratingRequestValidator);
    }

    @GetMapping("/{productSlug}")
    public ResponseEntity<List<RatingDetails>> getRatingsByProductSlug(
            @PathVariable("productSlug") String productSlug) {
        List<RatingDetails> ratings = ratingService.getRatingsByProductSlug(productSlug);
        return new ResponseEntity<>(ratings, HttpStatus.OK);
    }

    @GetMapping("/init-data/{productSlug}")
    public ResponseEntity<RatingInitData> getInitData(@PathVariable("productSlug") String productSlug) {
        RatingInitData initData = ratingService.getInitData(productSlug);
        return initData != null ?
                new ResponseEntity<>(initData, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Void> createNewRating(@Valid @RequestBody NewRatingRequest newRatingRequest) {
        boolean result = ratingService.create(newRatingRequest);
        return result ?
                new ResponseEntity<>(HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/report/{id}")
    public ResponseEntity<Boolean> reportRating(@PathVariable("id") Long id) {
        boolean result = ratingService.reportRating(id);
        return result ?
                new ResponseEntity<>(true, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> removeRating(@PathVariable("id") Long id) {
        boolean result = ratingService.removeRating(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/up-vote-rating/{id}")
    public ResponseEntity<Void> upVoteRating(@PathVariable("id") Long id, Principal principal) {
        ratingService.upVoteRating(id, principal.getName());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/up-vote-rating/{id}")
    public ResponseEntity<Void> removeUpVoteRating(@PathVariable("id") Long id, Principal principal) {
        ratingService.removeUpVoteRating(id, principal.getName());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
