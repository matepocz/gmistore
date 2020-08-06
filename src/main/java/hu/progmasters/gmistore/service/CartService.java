package hu.progmasters.gmistore.service;

import hu.progmasters.gmistore.dto.CartDto;
import hu.progmasters.gmistore.dto.CartItemDto;
import hu.progmasters.gmistore.dto.ProductDto;
import hu.progmasters.gmistore.model.Cart;
import hu.progmasters.gmistore.model.CartItem;
import hu.progmasters.gmistore.model.Product;
import hu.progmasters.gmistore.repository.CartRepository;
import hu.progmasters.gmistore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CartService(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    public CartDto getCart(HttpServletRequest request) {
        Cart actualCart = getActualCart(request);
        CartDto cartDto = new CartDto();
        cartDto.setId(actualCart.getId());
        cartDto.setCartItems(actualCart.getItems().stream()
                .map(item -> new CartItemDto(item))
                .collect(java.util.stream.Collectors.toSet()));
        cartDto.setTotalPrice(actualCart.getTotalPrice());
        return cartDto;
    }

    public boolean addProduct(Long id, int count, HttpServletRequest request) {
        Cart actualCart = getActualCart(request);
        Set<CartItem> items = actualCart.getItems();
        Optional<Product> productById = productRepository.findProductById(id);
        if (productById.isPresent()) {
            Product actualProduct = productById.get();
            for (CartItem item : items) {
                if (item.getProduct().equals(actualProduct)) {
                    item.setCount(item.getCount() + count);
                    actualCart.setTotalPrice(calculateCartTotalPrice(actualCart));
                    return true;
                }
            }
            CartItem cartItem = new CartItem();
            cartItem.setProduct(actualProduct);
            cartItem.setCount(count);
            items.add(cartItem);
            actualCart.setTotalPrice(calculateCartTotalPrice(actualCart));
            return true;
        }
        return false;
    }

    private double calculateCartTotalPrice(Cart cart) {
        return cart.getItems().stream()
                .mapToDouble(item -> (item.getProduct().getPrice() * item.getCount()))
                .sum();
    }

    private Cart getActualCart(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Cart actualCart = new Cart();
        if (session.getAttribute("cart") == null) {
            Cart cart = new Cart();
            cart.setItems(new HashSet<>());
            cart.setTotalPrice(0.0);
            actualCart = cartRepository.save(cart);
            session.setAttribute("cart", actualCart.getId());
        } else {
            long cartId = (Long) session.getAttribute("cart");
            Optional<Cart> cartById = cartRepository.findById(cartId);
            if (cartById.isPresent()) {
                actualCart = cartById.get();
            }
        }
        return actualCart;
    }
}
