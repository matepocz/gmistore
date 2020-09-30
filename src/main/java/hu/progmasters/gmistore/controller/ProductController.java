package hu.progmasters.gmistore.controller;

import hu.progmasters.gmistore.dto.inventory.InventorySoldProductsDto;
import hu.progmasters.gmistore.dto.product.PagedProductList;
import hu.progmasters.gmistore.dto.product.PagedSellerProductList;
import hu.progmasters.gmistore.dto.product.ProductDto;
import hu.progmasters.gmistore.dto.product.ProductFilterOptions;
import hu.progmasters.gmistore.service.InventoryService;
import hu.progmasters.gmistore.service.ProductService;
import hu.progmasters.gmistore.validator.ProductDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final InventoryService inventoryService;
    private final ProductDtoValidator productDtoValidator;

    @Autowired
    public ProductController(ProductService productService,InventoryService inventoryService,
                             ProductDtoValidator productDtoValidator) {
        this.productService = productService;
        this.productDtoValidator = productDtoValidator;
        this.inventoryService = inventoryService;
    }

    @InitBinder("productDto")
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(productDtoValidator);
    }

    @PostMapping
    public ResponseEntity<ProductDto> addProduct(@Valid @RequestBody ProductDto productDto) {
        productService.addProduct(productDto);
        return new ResponseEntity<>(productDto, HttpStatus.CREATED);
    }

    @PostMapping("/search")
    public ResponseEntity<PagedProductList> getProductsByQuery(
            @RequestParam(value = "filter", defaultValue = "false") Boolean filter,
            @RequestParam(value = "size", defaultValue = "10") String size,
            @RequestParam(value = "page", defaultValue = "0") String page,
            @RequestParam(value = "query") String query,
            @RequestBody(required = false) ProductFilterOptions filterOptions
    ) {
        PagedProductList products;
        if (filter){
            products = productService.getFilteredProductsByQuery(query, page, size, filterOptions);
        } else {
            products = productService.getProductsByQuery(query, page, size);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PostMapping("/discounted-products")
    public ResponseEntity<PagedProductList> getDiscountedProducts(
            @RequestParam(value = "filter", defaultValue = "false") Boolean filter,
            @RequestParam(value = "size", defaultValue = "10") String size,
            @RequestParam(value = "page", defaultValue = "0") String page,
            @RequestBody(required = false) ProductFilterOptions filterOptions) {
        PagedProductList products;
        if (filter) {
            products = productService.getFilteredDiscountedProducts(page, size, filterOptions);
        } else {
            products = productService.getDiscountedProducts(page, size);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PostMapping("/by-category/{category}")
    public ResponseEntity<PagedProductList> getProductsByCategory(
            @PathVariable("category") String category,
            @RequestParam(value = "size") String size,
            @RequestParam(name = "page", defaultValue = "0", required = false) String page,
            @RequestParam(value = "filter", defaultValue = "false") Boolean filter,
            @RequestBody(required = false) ProductFilterOptions filterOptions
    ) {
        PagedProductList products;
        if (filter) {
            products = productService.getFilteredProductsByCategory(
                    category, page, size, filterOptions);
        } else {
            products = productService.getActiveProductsByCategory(
                    category, Integer.parseInt(page), Integer.parseInt(size));
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/inactive")
    public ResponseEntity<List<ProductDto>> getAllInactiveProducts() {
        List<ProductDto> products = productService.getAllInActiveProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable String slug) {
        ProductDto product = productService.getProductBySlug(slug);
        return product != null ?
                new ResponseEntity<>(product, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

//    @GetMapping("/added-by-user/{username}")
//    public ResponseEntity<List<ProductDto>> getProductsAddedByUser(@PathVariable String username) {
//        String authenticatedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
//        if (authenticatedUsername.equalsIgnoreCase(username)) {
//            List<ProductDto> products = productService.getAllProductsAddedByUser(username);
//            return new ResponseEntity<>(products, HttpStatus.OK);
//        }
//        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteProduct(@PathVariable Long id) {
        boolean result = productService.deleteProduct(id);
        return result ?
                new ResponseEntity<>(true, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{slug}")
    public ResponseEntity<Void> updateProduct(@PathVariable String slug, @Valid @RequestBody ProductDto productDto) {
        boolean result = productService.updateProduct(slug, productDto);
        return result ?
                new ResponseEntity<>(HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/product-names/{name}")
    public ResponseEntity<Set<String>> getProductNames(@PathVariable("name") String name) {
        Set<String> productNames = productService.getProductNames(name);
        return new ResponseEntity<>(productNames, HttpStatus.OK);
    }

    @GetMapping("/get-discount-product")
    public ResponseEntity<List<ProductDto>> getDiscountProducts() {
        List<ProductDto> pictureOfProductsInOffer = productService.getProductInOffer();
        return new ResponseEntity<>(pictureOfProductsInOffer, HttpStatus.OK);
    }

    @GetMapping("/added-by-user")
    public ResponseEntity<PagedSellerProductList> getUserOwnProducts(
            @RequestParam(value = "size", defaultValue = "10") String size,
            @RequestParam(value = "page", defaultValue = "0") String page,
            Principal principal){
        PagedSellerProductList products = productService.getAllProductsAddedByUser(principal.getName(),Integer.parseInt(page), Integer.parseInt(size));
        return new ResponseEntity<>(products,HttpStatus.OK);
    }

    @GetMapping("/income-spent")
    public ResponseEntity<InventorySoldProductsDto> getIncomeSpent() {
        InventorySoldProductsDto inventorySoldProductsDto = inventoryService.getIncomeSpentByInventory();
        return new ResponseEntity<>(inventorySoldProductsDto, HttpStatus.OK);
    }
}
