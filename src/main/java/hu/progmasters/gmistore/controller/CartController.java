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

    @GetMapping("/items-in-cart")
    public ResponseEntity<Integer> getNumberOfItemsInCart(HttpSession session) {
        int items = cartService.getNumberOfItemsInCart(session);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<CartDto> getCart(HttpSession session) {
        CartDto cart = cartService.getCart(session);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PutMapping("/add-item")
    public ResponseEntity<Boolean> addItem(
            @RequestParam Long id, @RequestParam int count, HttpSession session) {
        boolean result = cartService.addProduct(id, count, session);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/refresh-product-count")
    public ResponseEntity<Boolean> refreshProductCount(
            @RequestParam Long id, @RequestParam int count, HttpSession session) {
        boolean result = cartService.updateProductCount(id, count, session);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/remove-product")
    public ResponseEntity<Boolean> removeProduct(@RequestParam Long id, HttpSession session) {
        boolean result = cartService.removeCartItem(id, session);
        return new ResponseEntity<>(result, HttpStatus.OK);
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

    @GetMapping("/can-checkout")
    public ResponseEntity<Boolean> canCheckout(HttpSession session) {
        boolean result = cartService.canCheckout(session);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
