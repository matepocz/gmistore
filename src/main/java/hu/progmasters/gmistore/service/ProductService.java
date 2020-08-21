package hu.progmasters.gmistore.service;

import com.github.slugify.Slugify;
import hu.progmasters.gmistore.dto.ProductDto;
import hu.progmasters.gmistore.enums.Category;
import hu.progmasters.gmistore.enums.Role;
import hu.progmasters.gmistore.exception.ProductNotFoundException;
import hu.progmasters.gmistore.model.Product;
import hu.progmasters.gmistore.model.User;
import hu.progmasters.gmistore.repository.ProductRepository;
import hu.progmasters.gmistore.repository.UserRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class ProductService {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;
    private final InventoryService inventoryService;
    private final UserRepository userRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, InventoryService inventoryService,
                          UserRepository userRepository) {
        this.productRepository = productRepository;
        this.inventoryService = inventoryService;
        this.userRepository = userRepository;
    }

    /**
     * Saves a product to the database
     *
     * @param productDto A ProductDto
     */
    public void addProduct(ProductDto productDto) {
        Product product = mapProductDtoToProduct(productDto);
        productRepository.save(product);
        inventoryService.saveInventory(product, productDto.getQuantityAvailable());
        LOGGER.debug("Product has been added! name: {}", product.getName());
    }

    private Product mapProductDtoToProduct(ProductDto productDto) {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setProductCode(productDto.getProductCode());
        product.setSlug(generateSlug(productDto.getName(), productDto.getProductCode()));
        product.setDescription(productDto.getDescription());
        product.setCategory(Category.valueOf(productDto.getCategory().toUpperCase()));
        product.setPictureUrl(productDto.getPictureUrl());
        product.setPictures(productDto.getPictures());
        product.setPrice(productDto.getPrice());
        product.setDiscount(productDto.getDiscount());
        product.setWarrantyMonths(productDto.getWarrantyMonths());
        product.setAverageRating(productDto.getAverageRating());
        product.setActive(productDto.isActive());
        product.setAddedBy(productDto.getAddedBy());
        return product;
    }

    private String generateSlug(String name, String productCode) {
        Slugify slugify = new Slugify();
        return slugify.slugify(name + "-" + productCode);
    }

    /**
     * Attempt to retrieve a product by the specified slug field.
     *
     * @param slug the product's unique identifier
     * @return A ProductDto, if not found throws ProductNotFoundException
     */
    public ProductDto getProductBySlug(String slug) {
        Product product = productRepository.findProductBySlug(slug)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        return mapProductToProductDto(product);
    }

    /**
     * Get all active products from the database
     *
     * @return A List of ProductDto
     */
    public List<ProductDto> getAllActiveProducts() {
        List<Product> allProduct = productRepository.findAll();
        return allProduct.stream().map(this::mapProductToProductDto)
                .filter(ProductDto::isActive).collect(Collectors.toList());
    }

    /**
     * Get all inactive products from the database
     *
     * @return A List of ProductDto
     */
    public List<ProductDto> getAllInActiveProducts() {
        List<Product> allProduct = productRepository.findAll();
        return allProduct.stream().map(this::mapProductToProductDto)
                .filter(productDto -> !productDto.isActive()).collect(Collectors.toList());
    }

    /**
     * Get all products added by the user
     *
     * @param username The user's username
     * @return A List of ProductDto
     */
    public List<ProductDto> getAllProductsAddedByUser(String username) {
        List<Product> productsAddedByUser = productRepository.findProductsByAddedBy(username);
        return productsAddedByUser.stream().map(this::mapProductToProductDto)
                .collect(Collectors.toList());
    }

    private ProductDto mapProductToProductDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setProductCode(product.getProductCode());
        productDto.setSlug(product.getSlug());
        productDto.setDescription(product.getDescription());
        productDto.setCategory(product.getCategory().getDisplayName());
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

    /**
     * Updates a product in the database with the specified id
     * and values
     *
     * @param slug       The product's unique slug ID.
     * @param productDto A ProductDto containing the values to update
     * @return A boolean, true if updated, false otherwise
     */
    public boolean updateProduct(String slug, ProductDto productDto) {
        Optional<Product> optionalProduct = productRepository.findProductBySlug(slug);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            if (isAuthorized(product.getAddedBy())) {
                updateProductValues(productDto, product);
                product.getInventory().setQuantityAvailable(productDto.getQuantityAvailable());
                productRepository.save(product);
                LOGGER.debug("Product updated! Id :{}", product.getId());
                return true;
            }
        }
        return false;
    }

    private void updateProductValues(ProductDto productDto, Product product) {
        product.setId(productDto.getId());
        product.setName(productDto.getName());
        product.setProductCode(productDto.getProductCode());
        product.setDescription(productDto.getDescription());
        product.setCategory(Category.valueOf(productDto.getCategory().toUpperCase()));
        product.setPictureUrl(productDto.getPictureUrl());
        product.setPictures(productDto.getPictures());
        product.setPrice(productDto.getPrice());
        product.setDiscount(productDto.getDiscount());
        product.setWarrantyMonths(productDto.getWarrantyMonths());
        product.setActive(productDto.isActive());
    }

    /**
     * Set a product to inactive if the product presents in the database
     *
     * @param id The product's unique id
     * @return True if successful, false otherwise.
     */
    public boolean deleteProduct(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            if (isAuthorized(product.getAddedBy())) {
                product.setActive(false);
                LOGGER.debug("Product has been set to inactive Id : {}", id);
                return true;
            }
        }
        return false;
    }

    private boolean isAuthorized(String productAddedBy) {
        String authenticatedUsername = productAddedBy;
        boolean isAdmin = false;
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            authenticatedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        }
        Optional<User> userByUsername = userRepository.findUserByUsername(productAddedBy);
        if (userByUsername.isPresent()) {
            isAdmin = userByUsername.get().getRoles().contains(Role.ROLE_ADMIN);
        }
        return isAdmin || productAddedBy.equalsIgnoreCase(authenticatedUsername);
    }

    public Map<Category, String> getProductCategories() {
        Map<Category, String> categoriesWithDisplayNames = new EnumMap<>(Category.class);
        Category[] categories = Category.values();
        List<String> displayNames =
                Stream.of(Category.values()).map(Category::getDisplayName).collect(Collectors.toList());
        for (int i = 0; i < categories.length; i++) {
            categoriesWithDisplayNames.put(categories[i], displayNames.get(i));
        }
        return categoriesWithDisplayNames;
    }
}
