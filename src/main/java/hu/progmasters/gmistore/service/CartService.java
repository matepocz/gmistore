package hu.progmasters.gmistore.service;

import hu.progmasters.gmistore.dto.CartDto;
import hu.progmasters.gmistore.dto.CartItemDto;
import hu.progmasters.gmistore.dto.ShippingMethodItem;
import hu.progmasters.gmistore.model.*;
import hu.progmasters.gmistore.repository.CartRepository;
import hu.progmasters.gmistore.repository.ProductRepository;
import hu.progmasters.gmistore.repository.UserRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class CartService {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(CartService.class);

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ShippingService shippingService;

    @Autowired
    public CartService(CartRepository cartRepository, ProductRepository productRepository,
                       UserRepository userRepository, ShippingService shippingService) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.shippingService = shippingService;
    }

    public CartDto getCart(HttpSession session) {
        Cart actualCart = getActualCart(session);
        CartDto cartDto = new CartDto();
        cartDto.setId(actualCart.getId());
        cartDto.setCartItems(actualCart.getItems().stream()
                .map(CartItemDto::new)
                .collect(java.util.stream.Collectors.toSet()));
        cartDto.setShippingMethod(new ShippingMethodItem(actualCart.getShippingMethod()));
        cartDto.setItemsTotalPrice(actualCart.getItemsTotalPrice());
        cartDto.setTotalPrice(actualCart.getTotalPrice());
        cartDto.setExpectedDeliveryDate(actualCart.getExpectedDeliveryDate());
        return cartDto;
    }

    public boolean addProduct(Long id, int count, HttpSession session) {
        Cart actualCart = getActualCart(session);
        Optional<Product> productById = productRepository.findProductById(id);
        if (productById.isPresent()) {
            Set<CartItem> items = actualCart.getItems();
            Product actualProduct = productById.get();
            for (CartItem item : items) {
                if (item.getProduct().equals(actualProduct) &&
                        actualProduct.getInventory().getQuantityAvailable() >= count) {
                    item.setCount(item.getCount() + count);
                    calculateAndSetItemsTotalPrice(actualCart);
                    setCartsTotalPrice(actualCart);
                    LOGGER.debug("Product count incremented!");
                    return true;
                }
            }
            if (actualProduct.getInventory().getQuantityAvailable() >= count) {
                CartItem cartItem = new CartItem();
                cartItem.setProduct(actualProduct);
                cartItem.setCount(count);
                items.add(cartItem);
                setInitialShippingMethod(actualCart);
                calculateAndSetItemsTotalPrice(actualCart);
                setCartsTotalPrice(actualCart);
                cartRepository.save(actualCart);
                LOGGER.debug("Product added to cart!");
                return true;
            }
        }
        LOGGER.debug("Product not found! id: {}", id);
        return false;
    }

    private void calculateAndSetItemsTotalPrice(Cart cart) {
        cart.setItemsTotalPrice(cart.getItems().stream().mapToDouble(
                item -> ((item.getProduct().getPrice() / 100)
                        * (100 - item.getProduct().getDiscount()))
                        * item.getCount())
                .sum());
    }

    private void setCartsTotalPrice(Cart cart) {
        cart.setTotalPrice(cart.getItemsTotalPrice() + cart.getShippingMethod().getCost());
    }

    private Cart getActualCart(HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Optional<User> userByUsername = userRepository.findUserByUsername(authentication.getName());
            if (userByUsername.isPresent()) {
                User user = userByUsername.get();
                Optional<Cart> cartByUser = cartRepository.findByUser(user);
                if (cartByUser.isPresent()) {
                    Cart actualCart = cartByUser.get();
                    calculateAndSetItemsTotalPrice(actualCart);
                    setCartsTotalPrice(actualCart);
                    session.setAttribute("cart", actualCart.getId());
                    return actualCart;
                }
                Cart cart = createCart(user);
                session.setAttribute("cart", cart.getId());
                return cart;
            }
        }
        if (session.getAttribute("cart") != null) {
            long cartId = (Long) session.getAttribute("cart");
            Optional<Cart> cartById = cartRepository.findById(cartId);
            if (cartById.isPresent()) {
                Cart cart = cartById.get();
                calculateAndSetItemsTotalPrice(cart);
                setCartsTotalPrice(cart);
                return cart;
            }
        }
        Cart actualCart = createCart(null);
        session.setAttribute("cart", actualCart.getId());
        return actualCart;
    }

    private Cart createCart(User user) {
        Cart cart = new Cart();
        cart.setItems(new HashSet<>());
        cart.setUser(user);
        setInitialShippingMethod(cart);
        cart.setItemsTotalPrice(0.0);
        cart.setTotalPrice(0.0);
        cart = cartRepository.save(cart);
        LOGGER.debug("Cart created!");
        return cart;
    }

    private void setInitialShippingMethod(Cart actualCart) {
        ShippingMethod shippingMethod = shippingService.getInitialShippingMethod();
        actualCart.setShippingMethod(shippingMethod);
    }

    public Cart updateProductCount(Long id, int count, HttpSession session) {
        Cart actualCart = getActualCart(session);
        Set<CartItem> items = actualCart.getItems();
        if (count == 0) {
            actualCart.getItems().removeIf((cartItem -> cartItem.getId().equals(id)));
            return actualCart;
        }
        items.stream().filter(cartItem -> cartItem.getProduct().getId().equals(id) &&
                cartItem.getProduct().getInventory().getQuantityAvailable() >= count)
                .forEach(cartItem -> cartItem.setCount(count));

        calculateAndSetItemsTotalPrice(actualCart);
        setCartsTotalPrice(actualCart);
        cartRepository.save(actualCart);
        LOGGER.debug("Product refreshed in cart, id: {}", actualCart.getId());
        return actualCart;
    }

    public void removeCartItem(Long id, HttpSession session) {
        Cart actualCart = getActualCart(session);
        actualCart.getItems().removeIf((cartItem -> cartItem.getId().equals(id)));
        calculateAndSetItemsTotalPrice(actualCart);
        setCartsTotalPrice(actualCart);
        LOGGER.debug("Cart item removed from cart, id: {}", actualCart.getId());
    }

    public void deleteCart(Long id) {
        cartRepository.findById(id).ifPresent(cartRepository::delete);
    }

    public void updateShippingMethod(String method, HttpSession session) {
        Cart actualCart = getActualCart(session);
        ShippingMethod shippingMethod = shippingService.fetchShippingMethod(method);
        if (shippingMethod != null) {
            actualCart.setShippingMethod(shippingMethod);
            setCartsTotalPrice(actualCart);
            actualCart.setExpectedDeliveryDate(shippingService.calculateExpectedShippingDate(shippingMethod));
        }
    }
}
