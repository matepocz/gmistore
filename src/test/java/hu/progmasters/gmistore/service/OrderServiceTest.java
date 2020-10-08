package hu.progmasters.gmistore.service;

import hu.progmasters.gmistore.dto.CustomerDetails;
import hu.progmasters.gmistore.model.Address;
import hu.progmasters.gmistore.model.User;
import hu.progmasters.gmistore.repository.OrderRepository;
import hu.progmasters.gmistore.repository.OrderStatusHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Principal;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    private OrderService orderService;

    @Mock
    private OrderRepository orderRepositoryMock;

    @Mock
    private UserService userServiceMock;

    @Mock
    private CartService cartServiceMock;

    @Mock
    private LookupService lookupServiceMock;

    @Mock
    private InventoryService inventoryServiceMock;

    @Mock
    private DateService dateServiceMock;

    @Mock
    private EmailSenderService emailSenderServiceMock;

    @Mock
    private OrderStatusHistoryRepository orderStatusHistoryRepositoryMock;

    @Mock
    private Principal principalMock;

    private final Supplier<Address> addressSupplier = () -> {
        Address address = new Address();
        address.setId(1L);
        address.setCity("Budapest");
        address.setStreet("Kossuth Lajos");
        address.setNumber(1);
        address.setPostcode("1000");
        address.setCountry("Hungary");
        return address;
    };

    private final Supplier<User> userSupplier = () -> {
        User user = new User();
        user.setId(1L);
        user.setUsername("username");
        user.setShippingAddress(addressSupplier.get());
        user.setBillingAddress(addressSupplier.get());
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setActive(true);
        user.setPhoneNumber("+36201234567");
        user.setEmail("john.doe@gmail.com");
        return user;
    };

    @BeforeEach
    public void init() {
        orderService = new OrderService(
                orderRepositoryMock, userServiceMock, cartServiceMock, lookupServiceMock,
                inventoryServiceMock, dateServiceMock, emailSenderServiceMock, orderStatusHistoryRepositoryMock);
    }

    @Test
    public void testGetCustomerDetailsPrincipalNullsShouldReturnNull() {
        CustomerDetails customerDetails = orderService.getCustomerDetails(null);
        assertNull(customerDetails);
        verify(principalMock, times(0)).getName();
        verify(userServiceMock, times(0)).getUserByUsername(any());
    }

    @Test
    public void testGetCustomerDetailsUserNotFoundShouldReturnNull() {
        when(principalMock.getName()).thenReturn("username");
        CustomerDetails customerDetails = orderService.getCustomerDetails(principalMock);
        assertNull(customerDetails);
        verify(principalMock, times(1)).getName();
        verify(userServiceMock, times(1)).getUserByUsername(any());
    }

    @Test
    public void testGetCustomerDetailsSuccessfully() {
        User user = userSupplier.get();
        when(principalMock.getName()).thenReturn("username");
        when(userServiceMock.getUserByUsername("username")).thenReturn(user);

        CustomerDetails customerDetails = orderService.getCustomerDetails(principalMock);

        assertNotNull(customerDetails);
        assertEquals("john.doe@gmail.com", user.getEmail());
        verify(principalMock, times(1)).getName();
        verify(userServiceMock, times(1)).getUserByUsername("username");
    }
}
