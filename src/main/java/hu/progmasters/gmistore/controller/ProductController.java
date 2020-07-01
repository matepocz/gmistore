package hu.progmasters.gmistore.controller;

import hu.progmasters.gmistore.model.Product;
import hu.progmasters.gmistore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/all")
    public ResponseEntity<List<Product>> products() {
        List<Product> products = new ArrayList<>();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
