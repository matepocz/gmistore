package hu.progmasters.gmistore.controller;

import hu.progmasters.gmistore.dto.ProductDto;
import hu.progmasters.gmistore.dto.product.PagedProductList;
import hu.progmasters.gmistore.dto.product.ProductFilterOptions;
import hu.progmasters.gmistore.service.ProductService;
import hu.progmasters.gmistore.validator.ProductDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final ProductDtoValidator productDtoValidator;

    @Autowired
    public ProductController(ProductService productService, ProductDtoValidator productDtoValidator) {
        this.productService = productService;
        this.productDtoValidator = productDtoValidator;
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

    @GetMapping
    public ResponseEntity<List<ProductDto>> getActiveProducts() {
        List<ProductDto> products = productService.getAllActiveProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PostMapping("/by-category/{category}")
    public ResponseEntity<PagedProductList> getProductsByCategory(
            @PathVariable("category") String category, @RequestParam(value = "size") String size,
            @RequestParam(name = "page", defaultValue = "0", required = false) String page,
            @RequestParam(value = "filter", defaultValue = "false") Boolean filter,
            @RequestBody(required = false) ProductFilterOptions filterOptions
    ) {
        PagedProductList products;
        if (filter){
            products = productService.getFilteredProducts(
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

    @GetMapping("/added-by-user/{username}")
    public ResponseEntity<List<ProductDto>> getProductsAddedByUser(@PathVariable String username) {
        String authenticatedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if (authenticatedUsername.equalsIgnoreCase(username)) {
            List<ProductDto> products = productService.getAllProductsAddedByUser(username);
            return new ResponseEntity<>(products, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        boolean result = productService.deleteProduct(id);
        return result ?
                new ResponseEntity<>(HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{slug}")
    public ResponseEntity<Void> updateProduct(@PathVariable String slug, @Valid @RequestBody ProductDto productDto) {
        boolean result = productService.updateProduct(slug, productDto);
        return result ?
                new ResponseEntity<>(HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/get-discount-pictures")
    public ResponseEntity<List<String>> getDiscountProductsPictureURL() {
        List<String> pictureOfProductsInOffer = productService.getPictureOfProductsInOffer();
        return new ResponseEntity<>(pictureOfProductsInOffer, HttpStatus.OK);
    }
}
