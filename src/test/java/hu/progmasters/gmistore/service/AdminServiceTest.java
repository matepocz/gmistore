package hu.progmasters.gmistore.service;

import hu.progmasters.gmistore.dto.user.UserRegistrationDTO;
import hu.progmasters.gmistore.enums.Role;
import hu.progmasters.gmistore.model.*;
import hu.progmasters.gmistore.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.session.SessionRegistry;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {
    private AdminService adminService;

    @Mock
    private UserService userService;

    @Mock
    private ProductService productService;

    @Mock
    private DateService dateService;

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private SessionRegistry sessionRegistry;

    @Spy
    private List<UserRegistrationDTO> userList;

    private DateTimeFormatter formatter;

    @BeforeEach
    public void setup() {
        adminService = new AdminService(sessionRegistry, dateService, userRepositoryMock, userService, productService);
        userList = new ArrayList<>();
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a")
                .withLocale(Locale.FRENCH);
    }

    private final Supplier<User> userSupplier = () -> {

        User user = new User();
        user.setId(1L);
        user.setActive(true);
        user.setEmail("GMI@123");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setPhoneNumber("234562433");
        user.setUsername("aron");
        user.setRoles(Arrays.asList(Role.ROLE_USER, Role.ROLE_ADMIN));
        user.setId(1L);
        user.setRegistered(LocalDateTime.parse("2020-10-08 10:15:30 AM", formatter));
        user.setShippingAddress(new Address());
        user.setBillingAddress(new Address());
        user.setPassword("gaborAronRezagyuja");
        user.setFavoriteProducts(new HashSet<>());
        user.setOrderList(new ArrayList<>());
        user.setCart(new Cart());
        return user;
    };

    @Test
    public void testInterval() {

        User user = userSupplier.get();
        UserRegistrationDTO user1 = new UserRegistrationDTO(user.getId(), user.getRegistered());
        UserRegistrationDTO user2 = new UserRegistrationDTO(user.getId(), user.getRegistered());
        userList.add(user1);
        userList.add(user2);

        DateService.CreateDates dates = new DateService.CreateDates(
                LocalDateTime.parse("2020-10-07 10:15:30 AM", formatter),
                LocalDateTime.parse("2020-10-11 10:15:30 AM", formatter));

        when(userRepositoryMock.findUserRegistrationsByDateInterval(
                dates.getStart(),
                dates.getEnd()))
                .thenReturn(userList);

        when(dateService.stringToDate(
                "{\"start\":\"2020-08-31T22:00:00.00\",\"end\":\"2020-10-29T22:00:00.000Z\"}"))
                .thenReturn(dates);

        List<UserRegistrationDTO> userRegistrationsByDateInterval = adminService.getUserRegistrationsByDateInterval(
                "{\"start\":\"2020-08-31T22:00:00.00\",\"end\":\"2020-10-29T22:00:00.000Z\"}");

        assertEquals(2, userRegistrationsByDateInterval.size());
    }
}
