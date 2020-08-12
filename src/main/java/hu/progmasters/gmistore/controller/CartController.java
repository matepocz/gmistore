package hu.progmasters.gmistore.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import hu.progmasters.gmistore.dto.CartDto;
import hu.progmasters.gmistore.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<CartDto> getCart(HttpServletRequest request) throws JsonProcessingException {
        CartDto cart = cartService.getCart(request);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PutMapping("/add-item")
    public ResponseEntity<Void> addItem(
            @RequestParam Long id, @RequestParam int count, HttpServletRequest request) {
        boolean result = cartService.addProduct(id, count, request);
        return result ?
                new ResponseEntity<>(HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/refresh-product-count")
    public ResponseEntity<Void> refreshProductCount(
            @RequestParam Long id, @RequestParam int count, HttpServletRequest request) {
        cartService.refreshProductCount(id, count, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/remove-product")
    public ResponseEntity<Void> removeProduct(@RequestParam Long id, HttpServletRequest request) {
        cartService.removeCartItem(id, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}