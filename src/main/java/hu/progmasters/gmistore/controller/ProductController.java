package hu.progmasters.gmistore.controller;

import hu.progmasters.gmistore.dto.ProductDto;
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

    @PostMapping("/add")
    public ResponseEntity<ProductDto> addProduct(@Valid @RequestBody ProductDto productDto) {
        productService.addProduct(productDto);
        return new ResponseEntity<>(productDto, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> products = productService.getAllActiveProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/all-inactive")
    public ResponseEntity<List<ProductDto>> getAllInactiveProducts() {
        List<ProductDto> products = productService.getAllInActiveProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id) {
        ProductDto product = productService.getProductById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
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

    @PutMapping("/delete/{id}")
    public ResponseEntity deleteProduct(@PathVariable Long id) {
        boolean isSetToInactive = productService.deleteProduct(id);
        return isSetToInactive ?
                new ResponseEntity<>(HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDto productDto) {
        ProductDto updatedProductDto = productService.updateProduct(id, productDto);
        return updatedProductDto != null ?
                new ResponseEntity<>(updatedProductDto, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
