package hu.progmasters.gmistore.service;

import hu.progmasters.gmistore.dto.CartDto;
import hu.progmasters.gmistore.dto.CartItemDto;
import hu.progmasters.gmistore.model.Cart;
import hu.progmasters.gmistore.model.CartItem;
import hu.progmasters.gmistore.model.Product;
import hu.progmasters.gmistore.model.User;
import hu.progmasters.gmistore.repository.CartRepository;
import hu.progmasters.gmistore.repository.ProductRepository;
import hu.progmasters.gmistore.repository.UserRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class CartService {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(CartService.class);

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Autowired
    public CartService(CartRepository cartRepository, ProductRepository productRepository,
                       UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public CartDto getCart(HttpServletRequest request) {
        Cart actualCart = getActualCart(request);
        CartDto cartDto = new CartDto();
        cartDto.setId(actualCart.getId());
        cartDto.setCartItems(actualCart.getItems().stream()
                .map(CartItemDto::new)
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
                    LOGGER.debug("Product count incremented!");
                    return true;
                }
            }
            CartItem cartItem = new CartItem();
            cartItem.setProduct(actualProduct);
            cartItem.setCount(count);
            items.add(cartItem);
            actualCart.setTotalPrice(calculateCartTotalPrice(actualCart));
            cartRepository.save(actualCart);
            LOGGER.debug("Product added to cart");
            return true;
        }
        LOGGER.debug("Product not found!");
        return false;
    }

    private double calculateCartTotalPrice(Cart cart) {
        return cart.getItems().stream()
                .mapToDouble(item -> (item.getProduct().getPrice() * item.getCount()))
                .sum();
    }

    private Cart getActualCart(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Optional<User> userByUsername = userRepository.findUserByUsername(authentication.getName());
            if (userByUsername.isPresent()) {
                User user = userByUsername.get();
                Optional<Cart> cartByUser = cartRepository.findByUser(user);
                if (cartByUser.isPresent()) {
                    session.setAttribute("cart", cartByUser.get().getId());
                    return cartByUser.get();
                } else {
                    Cart cart = createCart(user);
                    session.setAttribute("cart", cart.getId());
                    return cart;
                }
            }
        }


        Cart actualCart = new Cart();
        if (session.getAttribute("cart") == null) {
            Cart cart = new Cart();
            cart.setItems(new HashSet<>());
            if (authentication != null) {
                Optional<User> userByUsername = userRepository.findUserByUsername(authentication.getName());
                userByUsername.ifPresent(cart::setUser);
            }
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

    private Cart createCart(User user) {
        Cart cart = new Cart();
        cart.setItems(new HashSet<>());
        cart.setUser(user);
        cart.setTotalPrice(0.0);
        cart = cartRepository.save(cart);
        return cart;
    }

    public void refreshProductCount(Long id, int count, HttpServletRequest request) {
        Cart actualCart = getActualCart(request);
        Set<CartItem> items = actualCart.getItems();
        Iterator<CartItem> iterator = items.stream().iterator();
        while (iterator.hasNext()) {
            CartItem nextItem = iterator.next();
            if (nextItem.getProduct().getId().equals(id)) {
                nextItem.setCount(count);
            }
            if (nextItem.getCount() <= 0) {
                iterator.remove();
            }
        }
        actualCart.setTotalPrice(calculateCartTotalPrice(actualCart));
        cartRepository.save(actualCart);
    }

    public void removeCartItem(Long id, HttpServletRequest request) {
        Cart actualCart = getActualCart(request);
        actualCart.getItems().removeIf((cartItem -> cartItem.getId().equals(id)));
        actualCart.setTotalPrice(calculateCartTotalPrice(actualCart));
    }
}
