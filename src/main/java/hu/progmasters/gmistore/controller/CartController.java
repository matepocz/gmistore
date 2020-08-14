package hu.progmasters.gmistore.controller;

import hu.progmasters.gmistore.dto.CartDto;
import hu.progmasters.gmistore.dto.ShippingMethodItem;
import hu.progmasters.gmistore.service.CartService;
import hu.progmasters.gmistore.service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;
    private final ShippingService shippingService;

    @Autowired
    public CartController(CartService cartService, ShippingService shippingService) {
        this.cartService = cartService;
        this.shippingService = shippingService;
    }

    @GetMapping
    public ResponseEntity<CartDto> getCart(HttpSession session) {
        CartDto cart = cartService.getCart(session);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PutMapping("/add-item")
    public ResponseEntity<Void> addItem(
            @RequestParam Long id, @RequestParam int count, HttpSession session) {
        boolean result = cartService.addProduct(id, count, session);
        return result ?
                new ResponseEntity<>(HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/refresh-product-count")
    public ResponseEntity<Void> refreshProductCount(
            @RequestParam Long id, @RequestParam int count, HttpSession session) {
        cartService.updateProductCount(id, count, session);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/remove-product")
    public ResponseEntity<Void> removeProduct(@RequestParam Long id, HttpSession session) {
        cartService.removeCartItem(id, session);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/shipping-data")
    public ResponseEntity<List<ShippingMethodItem>> getShippingData() {
        List<ShippingMethodItem> shippingData = shippingService.getShippingData();
        return new ResponseEntity<>(shippingData, HttpStatus.OK);
    }

    @PutMapping("/update-shipping-method")
    public ResponseEntity<Void> updateShippingMethod(@RequestParam String method, HttpSession session) {
        cartService.updateShippingMethod(method, session);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
