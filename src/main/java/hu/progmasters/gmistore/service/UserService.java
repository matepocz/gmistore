package hu.progmasters.gmistore.service;

import hu.progmasters.gmistore.dto.RolesFormDto;
import hu.progmasters.gmistore.dto.product.ProductCategoryDetails;
import hu.progmasters.gmistore.dto.product.ProductDto;
import hu.progmasters.gmistore.dto.user.UserDto;
import hu.progmasters.gmistore.dto.user.UserEditableDetailsDto;
import hu.progmasters.gmistore.dto.user.UserListDetailDto;
import hu.progmasters.gmistore.enums.Role;
import hu.progmasters.gmistore.model.PasswordResetToken;
import hu.progmasters.gmistore.model.Product;
import hu.progmasters.gmistore.model.User;
import hu.progmasters.gmistore.repository.PasswordTokenRepository;
import hu.progmasters.gmistore.repository.ProductRepository;
import hu.progmasters.gmistore.repository.UserRepository;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final SessionRegistry sessionRegistry;
    private final PasswordTokenRepository passwordTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProductRepository productRepository;

    public UserService(PasswordEncoder passwordEncoder,
                       UserRepository userRepository,
                       SessionRegistry sessionRegistry,
                       PasswordTokenRepository passwordTokenRepository,
                       ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.sessionRegistry = sessionRegistry;
        this.passwordTokenRepository = passwordTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.productRepository = productRepository;
    }

    public UserDto getUserData(String username) {
        System.out.println(sessionRegistry.getAllPrincipals());
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        return new UserDto(
                user.getId(), user.getUsername(), user.getLastName(), user.getFirstName(), user.getShippingAddress(),
                user.getBillingAddress(), user.getEmail(), user.getPhoneNumber(), user.getRoles(), user.getRegistered(),
                user.isActive(), user.getOrderList());
    }

    public List<UserListDetailDto> getUserList() {
        return userRepository.findAllUsersWithListDetails();
    }

    public User getUserByUsername(String username) {
        Optional<User> userByUsername = userRepository.findUserByUsername(username);
        return userByUsername.orElse(null);
    }

    public User getUserById(Long id) {
        System.out.println(sessionRegistry.getAllPrincipals());
        return userRepository.findUserById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User with " + id + " not found!"));
    }

    public User getUserByName(String username) {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with " + username + " not found!"));
    }

    public List<RolesFormDto> getRoles() {
        List<RolesFormDto> roles = new ArrayList<>();
        for (Role value : Role.values()) {
            roles.add(new RolesFormDto(value));
        }
        return roles;
    }

    public void updateUserById(UserEditableDetailsDto user) {
        System.out.println(user.getId());
        User userById = getUserById(user.getId());
        updateUser(user, userById);
    }

    public void updateUserByName(UserEditableDetailsDto user) {
        User userByUsername = getUserByUsername(user.getUsername());
        updateUser(user, userByUsername);
    }

    private void updateUser(UserEditableDetailsDto user, User userToEdit) {
        userToEdit.setBillingAddress(user.getBillingAddress());
        userToEdit.setFirstName(user.getFirstName());
        userToEdit.setLastName(user.getLastName());
        userToEdit.setPhoneNumber(user.getPhoneNumber());
        userToEdit.setUsername(user.getUsername());
        userToEdit.setShippingAddress(user.getShippingAddress());
        if (user.getRoles() != null) {
            userToEdit.setRoles(user.getRoles().stream().map(Role::valueOf).collect(Collectors.toList()));
        }
        userRepository.save(userToEdit);
    }

    public User findUserByEmail(String userEmail) {
        return userRepository.findUserByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User with " + userEmail + " not found!"));
    }

    public void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken myToken = new PasswordResetToken(token, user);
        passwordTokenRepository.save(myToken);
    }

    public Optional<User> getUserByPasswordResetToken(String token) {
        return Optional.ofNullable(passwordTokenRepository.findByToken(token).getUser());
    }

    public void changeUserPassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
//        PasswordResetToken byTokenByUser = passwordTokenRepository.findPasswordResetTokenByUser(user);
        passwordTokenRepository.deleteByUser(user);
        userRepository.save(user);
    }

    public Set<ProductDto> getFavoriteProducts(Principal principal) {
        Optional<User> userByUsername = userRepository.findUserByUsername(principal.getName());
        if (userByUsername.isPresent()) {
            return userByUsername.get().getFavoriteProducts()
                    .stream()
                    .map(this::mapProductToProductDto)
                    .collect(Collectors.toSet());
        }
        throw new UsernameNotFoundException("User not found");
    }

    private ProductDto mapProductToProductDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setProductCode(product.getProductCode());
        productDto.setSlug(product.getSlug());
        productDto.setDescription(product.getDescription());
        productDto.setMainCategory(new ProductCategoryDetails(product.getMainCategory()));
        productDto.setSubCategory(new ProductCategoryDetails(product.getSubCategory()));
        productDto.setFeatures(product.getFeatures());
        productDto.setPictureUrl(product.getPictureUrl());
        productDto.setPictures(product.getPictures());
        productDto.setPrice(product.getPrice());
        productDto.setDiscount(product.getDiscount());
        productDto.setWarrantyMonths(product.getWarrantyMonths());
        productDto.setQuantityAvailable(product.getInventory().getQuantityAvailable());
        productDto.setQuantitySold(product.getInventory().getQuantitySold());
        productDto.setAverageRating(product.getAverageRating());
        productDto.setActive(product.isActive());
        productDto.setAddedBy(product.getAddedBy());
        return productDto;
    }

    public int getCountOfFavoriteProducts(Principal principal) {
        Optional<User> userByUsername = userRepository.findUserByUsername(principal.getName());
        return userByUsername.map(user -> user.getFavoriteProducts().size()).orElse(0);
    }

    public boolean addProductToFavorites(Long id, Principal principal) {
        Optional<User> userByUsername = userRepository.findUserByUsername(principal.getName());
        Optional<Product> productById = productRepository.findById(id);
        if (userByUsername.isPresent() && productById.isPresent()) {
            Set<Product> favoriteProducts = userByUsername.get().getFavoriteProducts();
            favoriteProducts.add(productById.get());
            LOGGER.debug("Product saved to favorites. id: {}, username: {}", id, principal.getName());
            return true;
        }
        LOGGER.debug("Could not add product to favorites, product or user not exists. " +
                "username: {}, product id: {}", principal.getName(), id);
        return false;
    }

    public boolean removeProductFromFavorites(Long id, Principal principal) {
        Optional<User> userByUsername = userRepository.findUserByUsername(principal.getName());
        Optional<Product> productById = productRepository.findById(id);
        if (userByUsername.isPresent() && productById.isPresent()) {
            Set<Product> favoriteProducts = userByUsername.get().getFavoriteProducts();
            if (favoriteProducts.contains(productById.get())) {
                favoriteProducts.remove(productById.get());
                LOGGER.debug("Product removed from favorites. id: {}, username: {}", id, principal.getName());
                return true;
            }
        }
        LOGGER.debug("Could not delete favorite product, user not found! username: {}", principal.getName());
        return false;
    }
}
