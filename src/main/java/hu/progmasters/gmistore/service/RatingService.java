package hu.progmasters.gmistore.service;

import hu.progmasters.gmistore.dto.NewRatingRequest;
import hu.progmasters.gmistore.dto.RatingDetails;
import hu.progmasters.gmistore.dto.RatingInitData;
import hu.progmasters.gmistore.enums.Role;
import hu.progmasters.gmistore.exception.ProductNotFoundException;
import hu.progmasters.gmistore.model.Product;
import hu.progmasters.gmistore.model.Rating;
import hu.progmasters.gmistore.model.User;
import hu.progmasters.gmistore.repository.ProductRepository;
import hu.progmasters.gmistore.repository.RatingRepository;
import hu.progmasters.gmistore.repository.UserRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
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
    private final UserRepository userRepository;

    @Autowired
    public RatingService(RatingRepository ratingRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.ratingRepository = ratingRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    /**
     * Fetch all active ratings for the given product
     *
     * @param productSlug The given product's unique ID
     * @return A list of Rating DTOs
     */
    public List<RatingDetails> getRatingsByProductSlug(String productSlug) {
        return ratingRepository.findAllByProductSlug(productSlug)
                .stream()
                .filter(Rating::getActive)
                .map(RatingDetails::new)
                .collect(Collectors.toList());
    }

    /**
     * Fetch the product's name, wraps into a DTO
     *
     * @param productSlug The given product's unique ID
     * @return A DTO
     */
    public RatingInitData getInitData(String productSlug) {
        LOGGER.debug("Rating init data requested, slug: {}", productSlug);
        Optional<Product> productBySlug = productRepository.findProductBySlug(productSlug);
        String name = productBySlug.map(Product::getName)
                .orElseThrow(() -> new ProductNotFoundException("Product not found! slug: " + productSlug));
        LOGGER.debug("Rating init data found!");
        return new RatingInitData(name);
    }

    /**
     * Creates a new Rating
     *
     * @param newRatingRequest A DTO containing the required details
     * @return A boolean, true if successful, false otherwise
     */
    public boolean create(NewRatingRequest newRatingRequest, Principal principal) {
        String productSlug = newRatingRequest.getProduct();
        Optional<Product> productBySlug = productRepository.findProductBySlug(productSlug);
        if (productBySlug.isPresent() && !principal.getName().equals("anonymousUser")) {
            Product actualProduct = productBySlug.get();
            Optional<Rating> ratingByUsername =
                    ratingRepository.findRatingByUsername(principal.getName(), actualProduct);
            if (ratingByUsername.isEmpty()) {
                Rating rating = new Rating();
                rating.setActive(true);
                rating.setUsername(principal.getName());
                rating.setProduct(actualProduct);
                rating.setActualRating(newRatingRequest.getActualRating());
                rating.setTitle(newRatingRequest.getTitle());
                rating.setPositiveComment(newRatingRequest.getPositiveComment());
                rating.setNegativeComment(newRatingRequest.getNegativeComment());
                rating.setUpVotes(0);
                rating.setPictures(newRatingRequest.getPictures());
                rating.setReported(false);
                rating.setTimeStamp(LocalDateTime.now());
                ratingRepository.save(rating);
                calculateAndSetAverageRating(actualProduct);
                LOGGER.info("Product rating created, id: {}", rating.getId());
                return true;
            }
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

    /**
     * Set the given Rating's state to inactive
     *
     * @param id The given Rating's ID
     * @return A boolean, true if successful, false otherwise
     */
    public boolean removeRating(Long id, Principal principal) {
        Optional<Rating> ratingById = ratingRepository.findById(id);
        Optional<User> userByUsername = userRepository.findUserByUsername(principal.getName());
        if (ratingById.isPresent() && userByUsername.isPresent()) {
            if (userByUsername.get().getRoles().stream().anyMatch(role -> role.equals(Role.ROLE_ADMIN))) {
                ratingById.get().setActive(false);
                LOGGER.info("Product rating set to inactive, id: {}", id);
                return true;
            }
            LOGGER.info("Unauthorized delete request, rating id: {}, username: {}", id, principal.getName());
            return false;
        }
        LOGGER.info("Product rating or user not found, id: {}", id);
        return false;
    }

    /**
     * Increases the given Rating's vote counter by one.
     *
     * @param id       The given Rating's ID
     * @param username The actual user's username who up voted the Rating
     */
    public void upVoteRating(Long id, String username) {
        Optional<Rating> ratingById = ratingRepository.findById(id);
        if (ratingById.isPresent() && !ratingById.get().getVoters().contains(username)) {
            Rating rating = ratingById.get();
            rating.setUpVotes(rating.getUpVotes() + 1);
            rating.getVoters().add(username);
            LOGGER.debug("Product rating upvote saved! rating id: {}, username: {}", id, username);
        }
    }

    /**
     * Decreases the given Rating's vote counter by one.
     *
     * @param id       The given Rating's ID
     * @param username The actual user's username who removed their up vote from this Rating
     */
    public void removeUpVoteRating(Long id, String username) {
        Optional<Rating> ratingById = ratingRepository.findById(id);
        if (ratingById.isPresent() && ratingById.get().getVoters().contains(username)) {
            Rating rating = ratingById.get();
            if (rating.getUpVotes() > 0) {
                rating.setUpVotes(rating.getUpVotes() - 1);
            }
            rating.getVoters().remove(username);
            LOGGER.debug("Product rating upvote removed! rating id: {}, username: {}", id, username);
        }
    }

    /**
     * Set a Rating's reported flag to true
     *
     * @param id The given Rating's ID
     * @return A boolean, true if successful, false otherwise
     */
    public boolean reportRating(Long id) {
        Optional<Rating> ratingById = ratingRepository.findById(id);
        if (ratingById.isPresent()) {
            ratingById.get().setReported(true);
            LOGGER.debug("Product rating reported, rating id: {}", id);
            return true;
        }
        LOGGER.info("Product rating reported, but not found, id: {}", id);
        return false;
    }
}
