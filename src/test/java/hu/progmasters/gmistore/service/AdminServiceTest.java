package hu.progmasters.gmistore.service;

import hu.progmasters.gmistore.dto.product.ProductDto;
import hu.progmasters.gmistore.dto.user.UserDto;
import hu.progmasters.gmistore.dto.user.UserRegistrationDTO;
import hu.progmasters.gmistore.dto.user.UserRegistrationDateDto;
import hu.progmasters.gmistore.enums.Role;
import hu.progmasters.gmistore.model.Address;
import hu.progmasters.gmistore.model.Cart;
import hu.progmasters.gmistore.model.Product;
import hu.progmasters.gmistore.model.User;
import hu.progmasters.gmistore.repository.ProductRepository;
import hu.progmasters.gmistore.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.core.session.SessionRegistry;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class AdminServiceTest {
    private AdminService adminService;
    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private SessionRegistry sessionRegistry;

    @BeforeEach
    public void setup() {
        adminService = new AdminService(sessionRegistry, userRepositoryMock,  userService);
    }

    private final Supplier<User> userSupplier = () -> {

        User user = new User();
        user.setId(1L);
        user.setActive(true);
        user.setEmail("GMI@123");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setPhoneNumber("234562433");
        user.setUsername("username");
        user.setRoles(Arrays.asList(Role.ROLE_USER,Role.ROLE_ADMIN));
        user.setId(1L);
        user.setRegistered(LocalDateTime.now());;
        user.setShippingAddress(new Address());
        user.setBillingAddress(new Address());
        user.setPassword("anyad");
        user.setFavoriteProducts(new HashSet<>());
        user.setOrderList(new ArrayList<>());
        user.setCart(new Cart());
        return user;
    };

    @Test
    public void testInterval() {
        User user = userSupplier.get();
        when(userRepositoryMock.save(any(User.class))).thenAnswer(returnsFirstArg());

        List<UserRegistrationDTO> userRegistrationsByDateInterval = adminService.getUserRegistrationsByDateInterval(
                "{\"start\":\"2020-08-31T22:00:00.000Z\",\"end\":\"2020-10-29T22:00:00.000Z\"}");

        assertEquals(1, userRegistrationsByDateInterval.size());
        assertEquals("", user.getUsername());

    }
}
