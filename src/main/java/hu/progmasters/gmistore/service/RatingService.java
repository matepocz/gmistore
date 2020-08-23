package hu.progmasters.gmistore.service;

import hu.progmasters.gmistore.dto.NewRatingRequest;
import hu.progmasters.gmistore.dto.RatingDetails;
import hu.progmasters.gmistore.dto.RatingInitData;
import hu.progmasters.gmistore.exception.ProductNotFoundException;
import hu.progmasters.gmistore.model.Product;
import hu.progmasters.gmistore.model.Rating;
import hu.progmasters.gmistore.repository.ProductRepository;
import hu.progmasters.gmistore.repository.RatingRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class RatingService {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(RatingService.class);

    private final RatingRepository ratingRepository;
    private final ProductRepository productRepository;

    @Autowired
    public RatingService(RatingRepository ratingRepository, ProductRepository productRepository) {
        this.ratingRepository = ratingRepository;
        this.productRepository = productRepository;
    }

    public List<RatingDetails> getRatingsByProductSlug(String productSlug) {
        return ratingRepository.findAllByProductSlug(productSlug)
                .stream()
                .filter(Rating::getActive)
                .map(RatingDetails::new)
                .collect(Collectors.toList());
    }

    public RatingInitData getInitData(String productSlug) {
        LOGGER.debug("Rating init data requested, slug: {}", productSlug);
        Optional<Product> productBySlug = productRepository.findProductBySlug(productSlug);
        String name = productBySlug.map(Product::getName)
                .orElseThrow(() -> new ProductNotFoundException("Product not found! slug: " + productSlug));
        LOGGER.debug("Rating init data found!");
        return new RatingInitData(name);
    }

    public boolean create(NewRatingRequest newRatingRequest) {
        String productSlug = newRatingRequest.getProduct();
        Optional<Product> productBySlug = productRepository.findProductBySlug(productSlug);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (productBySlug.isPresent() && authentication != null) {
            Product actualProduct = productBySlug.get();
            Rating rating = new Rating();
            rating.setActive(true);
            rating.setUsername(authentication.getName());
            rating.setProduct(actualProduct);
            rating.setActualRating(newRatingRequest.getActualRating());
            rating.setTitle(newRatingRequest.getTitle());
            rating.setPositiveComment(newRatingRequest.getPositiveComment());
            rating.setNegativeComment(newRatingRequest.getNegativeComment());
            rating.setPictures(newRatingRequest.getPictures());
            rating.setTimeStamp(LocalDateTime.now());
            ratingRepository.save(rating);
            calculateAndSetAverageRating(actualProduct);
            LOGGER.info("Product rating created, id: {}", rating.getId());
            return true;
        }
        LOGGER.info("Product rating creation failed, product slug: {}", productSlug);
        return false;
    }

    private void calculateAndSetAverageRating(Product actualProduct) {
        List<Rating> ratingsForCurrentProduct = ratingRepository.findAllByProductSlug(actualProduct.getSlug());
        double sum = ratingsForCurrentProduct.stream().mapToInt(Rating::getActualRating).sum();
        double averageRating = sum / ratingsForCurrentProduct.size();
        actualProduct.setAverageRating(averageRating);
    }

    public boolean removeRating(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<Rating> ratingById = ratingRepository.findById(id);
        if (ratingById.isPresent() && authentication != null) {
            if (authentication.getAuthorities().stream()
                    .noneMatch(authority -> authority.getAuthority().equals("ADMIN"))){
                LOGGER.info("Unauthorized delete request, rating id: {}, username: {}", id, authentication.getName());
                return false;
            }
            ratingById.get().setActive(false);
            LOGGER.info("Product rating set to inactive, id: {}", id);
            return true;
        }
        LOGGER.info("Product rating not found, id: {}", id);
        return false;
    }
}