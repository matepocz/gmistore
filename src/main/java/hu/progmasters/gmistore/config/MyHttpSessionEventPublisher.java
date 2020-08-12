package hu.progmasters.gmistore.config;

import hu.progmasters.gmistore.model.Cart;
import hu.progmasters.gmistore.repository.CartRepository;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSessionEvent;
import java.util.Optional;

@Component
public class MyHttpSessionEventPublisher extends HttpSessionEventPublisher {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MyHttpSessionEventPublisher.class);

    private final CartRepository cartRepository;

    public MyHttpSessionEventPublisher(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        LOGGER.debug("Session created id: {}", event.getSession().getId());
        super.sessionCreated(event);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        Long cartId = (Long) event.getSession().getAttribute("cart");
        if (cartId != null) {
            Optional<Cart> cartById = cartRepository.findById(cartId);
            if (cartById.isPresent() && cartById.get().getUser() == null) {
                LOGGER.debug("Cart deleted! id: {} , Session id: {}", cartId, event.getSession().getId());
                cartRepository.deleteById(cartId);
            }
        }
        super.sessionDestroyed(event);
    }
}
