package hu.progmasters.gmistore.service;

import com.github.slugify.Slugify;
import hu.progmasters.gmistore.dto.product.*;
import hu.progmasters.gmistore.enums.Role;
import hu.progmasters.gmistore.exception.ProductNotFoundException;
import hu.progmasters.gmistore.model.LookupEntity;
import hu.progmasters.gmistore.model.Product;
import hu.progmasters.gmistore.repository.ProductRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;
    private final InventoryService inventoryService;
    private final LookupService lookupService;

    @Autowired
    public ProductService(ProductRepository productRepository, InventoryService inventoryService,
                          LookupService lookupService) {
        this.productRepository = productRepository;
        this.inventoryService = inventoryService;
        this.lookupService = lookupService;
    }

    /**
     * Save a Product to the database
     *
     * @param productDto The DTO containing the details
     * @return The actual saved Product object
     */
    public Product addProduct(ProductDto productDto) {
        Product product = mapProductDtoToProduct(productDto);
        Product savedProduct = productRepository.save(product);
        inventoryService.saveInventory(product, productDto.getQuantityAvailable());
        LOGGER.debug("Product saved, id: {}", savedProduct.getId());
        return savedProduct;
    }

    private Product mapProductDtoToProduct(ProductDto productDto) {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setProductCode(productDto.getProductCode());
        product.setSlug(generateSlug(productDto.getName(), productDto.getProductCode()));
        product.setDescription(productDto.getDescription());
        product.setMainCategory(getCategoryByKey(productDto.getMainCategory().getKey()));
        product.setSubCategory(getCategoryByKey(productDto.getSubCategory().getKey()));
        product.setFeatures(productDto.getFeatures());
        product.setPictureUrl(productDto.getPictureUrl());
        product.setPictures(productDto.getPictures());
        product.setPrice(productDto.getPrice());
        product.setDiscount(productDto.getDiscount());
        product.setWarrantyMonths(productDto.getWarrantyMonths());
        product.setAverageRating(productDto.getAverageRating());
        product.setActive(productDto.isActive());
        product.setAddedBy(productDto.getAddedBy());
        product.setPriceGross(getPriceGross(productDto));
        return product;
    }

    private double getPriceGross(ProductDto productDto) {
        return productDto.getPrice()* 0.5 + new Random().nextFloat() * (0.80 - 0.5);
    }

    LookupEntity getCategoryByKey(String key) {
        return lookupService.getCategoryByKey(key);
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

    public List<ProductDto> getProductInOffer() {
        List<Product> getDiscountProduct = productRepository.findProductByAndDiscountOrderByDiscountAsc();

        List<ProductDto> collect = new ArrayList<>();
        for (Product product : getDiscountProduct) {
            if (collect.size() < 10) {
                ProductDto productDto = mapProductToProductDto(product);
                collect.add(productDto);
            } else {
                break;
            }
        }
        return collect;
    }

    /**
     * Fetch product names for autocomplete function
     *
     * @param name The name to look for in the product names
     * @return A List containing product names
     */
    public Set<String> getProductNames(String name) {
        return productRepository.findProductNames(name);
    }

    /**
     * Fetch products from the database by the given input, it will return the product
     * if the product's name or description containing that piece of string.
     *
     * @param query The given input, the return products containing this piece of string
     * @param page  The index of the page
     * @param size  The size of the page
     * @return A PageProductList DTO that contains a list of ProductDTO,
     * also the total number of elements found in the database
     */
    public PagedProductList getProductsByQuery(String query, String page, String size) {
        Pageable pageable = PageRequest.of(Integer.parseInt(page), Integer.parseInt(size));
        Page<Product> products = productRepository.findProductsBySearchInput(query, pageable);
        return createPagedProductListResponse("Keresési eredmény", products);
    }

    /**
     * Fetch products from the database by the given input, it will return the product
     * if the product's name or description containing that piece of string, also accepts a
     * ProductFilterOptions DTO that can hold extra details to filter the products.
     *
     * @param query         The given input, the return products containing this piece of string
     * @param page          The required page index
     * @param size          The number of products returned on the given page
     * @param filterOptions ProductFilterOption DTO, products filter by this criteria
     * @return A PageProductList DTO that contains a list of ProductDTO,
     * also the total number of elements found in the database
     */
    public PagedProductList getFilteredProductsByQuery(
            String query, String page, String size, ProductFilterOptions filterOptions
    ) {
        Pageable pageable = PageRequest.of(Integer.parseInt(page), Integer.parseInt(size));
        Page<Product> products;
        products = productRepository.findAll(
                buildFilterSpecificationForSearch(query, filterOptions), pageable);
        return createPagedProductListResponse("Keresési eredmény", products);
    }

    /**
     * Builds a specification for searching in products name's, description's,
     * also contains other filtering criteria
     *
     * @param searchInput          The given input that must be in the returned products name's, descriptions.
     * @param productFilterOptions ProductFilterOptions that holds filtering criteria
     * @return A specification that holds the criteria
     */
    Specification<Product> buildFilterSpecificationForSearch(
            String searchInput, ProductFilterOptions productFilterOptions) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = getPredicates(productFilterOptions, root, criteriaBuilder);
            predicates.add(
                    criteriaBuilder.or(
                            criteriaBuilder.like(root.get("name"), "%" + searchInput + "%"),
                            criteriaBuilder.like(root.get("description"), "%" + searchInput + "%"))
            );
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    private List<Predicate> getPredicates(ProductFilterOptions productFilterOptions,
                                          javax.persistence.criteria.Root<Product> root,
                                          javax.persistence.criteria.CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.isTrue(root.get("active")));

        Boolean discounted = productFilterOptions.getDiscounted();
        Boolean nonDiscounted = productFilterOptions.getNonDiscounted();
        if (discounted != null && discounted && nonDiscounted != null && nonDiscounted) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("discount"), 0));
        } else if (discounted != null && discounted) {
            predicates.add(criteriaBuilder.greaterThan(root.get("discount"), 0));
        } else if (nonDiscounted != null && nonDiscounted) {
            predicates.add(criteriaBuilder.lessThan(root.get("discount"), 1));
        }
        if (productFilterOptions.getNotInStock() != null && !productFilterOptions.getNotInStock()) {
            predicates.add(
                    criteriaBuilder.greaterThanOrEqualTo(root.get("inventory").get("quantityAvailable"), 1)
            );
        } else if (productFilterOptions.getNotInStock() != null && productFilterOptions.getNotInStock()) {
            predicates.add(criteriaBuilder.lessThan(root.get("inventory").get("quantityAvailable"), 1));
        }
        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("averageRating"),
                productFilterOptions.getLowestRating())
        );
        predicates.add(criteriaBuilder.between(
                root.get("price"), productFilterOptions.getMinPrice(), productFilterOptions.getMaxPrice())
        );
        return predicates;
    }

    /**
     * Fetch all active products by the given subcategory
     *
     * @param category The given subcategory
     * @param page     The index of the requested page
     * @param size     The size of the requested page
     * @return A PageProductList DTO, that contains a List of ProductDto
     */
    public PagedProductList getActiveProductsByCategory(String category, Integer page, Integer size) {
        LookupEntity categoryByKey = lookupService.getCategoryByKey(category);
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productsBySubCategory =
                productRepository.findProductsBySubCategory(
                        categoryByKey, pageable
                );
        return createPagedProductListResponse(categoryByKey.getDisplayName(), productsBySubCategory);
    }

    /**
     * Fetch all products by the given filter options
     *
     * @param category      The given subcategory
     * @param pageIndex     The index of the requested page
     * @param pageSize      The size of the requested page
     * @param filterOptions A DTO containing the filter options
     * @return A PagedProductList DTO, that contains a List of ProductDto
     */
    public PagedProductList getFilteredProductsByCategory(
            String category, String pageIndex, String pageSize, ProductFilterOptions filterOptions
    ) {
        LookupEntity categoryByKey = lookupService.getCategoryByKey(category);
        Pageable pageable = PageRequest.of(Integer.parseInt(pageIndex), Integer.parseInt(pageSize));
        Page<Product> products = productRepository.findAll(
                buildFilterSpecificationForCategory(categoryByKey, filterOptions), pageable);
        return createPagedProductListResponse(categoryByKey.getDisplayName(), products);
    }

    /**
     * Builds a specification for the filter query
     *
     * @param category             The subcategory where the products belongs to
     * @param productFilterOptions A DTO containing the required filters
     * @return A Specification
     */
    Specification<Product> buildFilterSpecificationForCategory(
            LookupEntity category, ProductFilterOptions productFilterOptions) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = getPredicates(productFilterOptions, root, criteriaBuilder);
            if (category != null) {
                predicates.add(criteriaBuilder.equal(root.get("subCategory"), category));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    /**
     * Builds/wraps a page of products to a wrapper DTO, also sets some extra details
     *
     * @param categoryDisplayName The category display name or other string to show
     * @param products            A list of ProductDTO
     * @return A PagedProductList DTO that containing all the required details
     */
    private PagedProductList createPagedProductListResponse(String categoryDisplayName, Page<Product> products) {
        PagedProductList productList = new PagedProductList();
        productList.setProducts(products
                .stream()
                .map(this::mapProductToProductDto)
                .collect(Collectors.toList())
        );
        productList.setCategoryDisplayName(categoryDisplayName);
        productList.setTotalElements(products.getTotalElements());
        productList.setTotalPages(products.getTotalPages());
        productList.setHighestPrice(productRepository.getHighestPrice().intValue());
        return productList;
    }

    /**
     * Fetch all discounted products
     *
     * @param page The requested page index
     * @param size The size of the requested page
     * @return A PagedProductList DTO containing ProductDTOs and extra details
     */
    public PagedProductList getDiscountedProducts(String page, String size) {
        Pageable pageable = PageRequest.of(Integer.parseInt(page), Integer.parseInt(size));
        Page<Product> discountedProducts = productRepository.findDiscountedProducts(pageable);
        return createPagedProductListResponse("Leárazott termékek", discountedProducts);
    }

    /**
     * Fetch all filtered and discounted products
     *
     * @param page          The requested page
     * @param size          The size of the requested page
     * @param filterOptions The filter specifications
     * @return A PagedProductList DTO containing ProductDtos and extra details
     */
    public PagedProductList getFilteredDiscountedProducts(
            String page, String size, ProductFilterOptions filterOptions) {
        Pageable pageable = PageRequest.of(Integer.parseInt(page), Integer.parseInt(size));
        Page<Product> filteredDiscountedProducts =
                productRepository.findAll(buildFilterSpecificationForDiscountedProducts(filterOptions), pageable);
        return createPagedProductListResponse("Leárazott termékek", filteredDiscountedProducts);
    }

    /**
     * Builds a specification query for the discounted products
     *
     * @param productFilterOptions DTO that contains the filtering criteria
     * @return A Specification that contains the filtering criteria
     */
    Specification<Product> buildFilterSpecificationForDiscountedProducts(ProductFilterOptions productFilterOptions) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = getPredicates(productFilterOptions, root, criteriaBuilder);
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
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
     * @param sellerName
     * @return A List of ProductDto
     */
    public PagedSellerProductList getAllProductsAddedByUser(String sellerName, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productsAddedByUser = productRepository.findProductsByAddedBy(sellerName, pageable);

        PagedSellerProductList sellerProducts = new PagedSellerProductList();
        sellerProducts.setProducts(
                productsAddedByUser.stream().map(this::mapProductToProductTableDto)
                        .collect(Collectors.toList()));
        sellerProducts.setTotalElements(productsAddedByUser.getTotalElements());
        return sellerProducts;
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
    private ProductTableDto mapProductToProductTableDto(Product product){
        ProductTableDto productTableDto = new ProductTableDto();
        productTableDto.setId(product.getId());
        productTableDto.setName(product.getName());
        productTableDto.setCategoryDisplayName(product.getSubCategory().getDisplayName());
        productTableDto.setPrice(product.getPrice());
        productTableDto.setActive(product.isActive());
        productTableDto.setPictureUrl(product.getPictureUrl());
        productTableDto.setSlug(product.getSlug());
        return productTableDto;

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
        product.setMainCategory(getCategoryByKey(productDto.getMainCategory().getKey()));
        product.setSubCategory(getCategoryByKey(productDto.getSubCategory().getKey()));
        product.setFeatures(productDto.getFeatures());
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
            } else {
                LOGGER.warn("Unauthorized product delete request, id: {}, username: {}",
                        id, SecurityContextHolder.getContext().getAuthentication().getName());
                return false;
            }
        }
        LOGGER.warn("Product delete request, but product not found! id: {}", id);
        return false;
    }

    private boolean isAuthorized(String productAddedBy) {
        boolean isAdmin = false;
        String authenticatedUsername = "unknown";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            for (GrantedAuthority authority : authentication.getAuthorities()) {
                if (authority.getAuthority().equals(Role.ROLE_ADMIN.toString())) {
                    isAdmin = true;
                }
            }
            authenticatedUsername = authentication.getName();
        }
        return isAdmin || productAddedBy.equalsIgnoreCase(authenticatedUsername);
    }

    public List<ProductTableDto> getAllProductsToTable() {
        return productRepository.findAllToTable();
    }
}
