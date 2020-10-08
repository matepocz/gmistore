package hu.progmasters.gmistore.service;

import hu.progmasters.gmistore.dto.AddressDetails;
import hu.progmasters.gmistore.dto.CustomerDetails;
import hu.progmasters.gmistore.dto.order.OrderRequest;
import hu.progmasters.gmistore.enums.DomainType;
import hu.progmasters.gmistore.model.*;
import hu.progmasters.gmistore.repository.OrderRepository;
import hu.progmasters.gmistore.repository.OrderStatusHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashSet;
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

    @Mock
    private HttpSession session;

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

    private final Supplier<Cart> cartSupplier = () -> {
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setItems(new HashSet<>());
        cart.setUser(userSupplier.get());
        cart.setItemsTotalPrice(1000.0);
        cart.setTotalPrice(1100.0);
        return cart;
    };

    private final Supplier<Product> productSupplier = () -> {
        LookupEntity mainCategory = new LookupEntity();
        mainCategory.setId(1L);
        mainCategory.setDisplayFlag(true);
        mainCategory.setLookupKey("main_category");
        mainCategory.setDisplayName("Main Category");
        mainCategory.setDomainType(DomainType.PRODUCT_CATEGORY);

        LookupEntity subCategory = new LookupEntity();
        subCategory.setId(2L);
        subCategory.setDisplayFlag(true);
        subCategory.setLookupKey("sub_category");
        subCategory.setDisplayName("Sub Category");
        subCategory.setDomainType(DomainType.PRODUCT_CATEGORY);
        subCategory.setParent(mainCategory);

        Product product = new Product();

        Inventory inventory = new Inventory();
        inventory.setQuantityAvailable(1);
        inventory.setId(1L);
        inventory.setProduct(product);

        product.setId(1L);
        product.setName("test product");
        product.setProductCode("GMI123");
        product.setSlug("test-product-gmi123");
        product.setDescription("test 1");
        product.setMainCategory(mainCategory);
        product.setSubCategory(subCategory);
        product.setActive(true);
        product.setPrice(100.0);
        product.setDiscount(0);
        product.setWarrantyMonths(12);
        product.setAddedBy("unknown");
        product.setInventory(inventory);
        return product;
    };

    private final Supplier<CartItem> cartItemSupplier = () -> {
        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setCount(1);
        cartItem.setProduct(productSupplier.get());
        return cartItem;
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

    @Test
    public void testCreateOrderCartIsEmptyShouldReturnFalse() {
        Cart cart = cartSupplier.get();
        OrderRequest orderRequest = new OrderRequest();

        when(cartServiceMock.getActualCart(any())).thenReturn(cart);

        boolean result = orderService.createOrder(orderRequest, session, principalMock);

        assertFalse(result);
    }

    @Test
    public void testCreateOrderSuccessfully() {
        Address address = addressSupplier.get();
        AddressDetails shippingAddress = new AddressDetails(address);

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setEmail("john.doe@gmail.com");
        orderRequest.setFirstName("John");
        orderRequest.setLastName("Doe");
        orderRequest.setPhoneNumber("+36201234567");
        orderRequest.setPaymentMethod("test");
        orderRequest.setShippingAddress(shippingAddress);
        orderRequest.setBillingAddress(shippingAddress);

        Cart cart = cartSupplier.get();
        cart.getItems().add(cartItemSupplier.get());
        cart.setExpectedDeliveryDate(LocalDateTime.now());

        ShippingMethod shippingMethod = new ShippingMethod();
        shippingMethod.setId(1);
        shippingMethod.setCost(100);
        shippingMethod.setMethod("test");
        shippingMethod.setDays(2);

        cart.setShippingMethod(shippingMethod);

        User user = userSupplier.get();

        LookupEntity paymentMethod = new LookupEntity();
        paymentMethod.setId(1L);
        paymentMethod.setLookupKey("test");
        paymentMethod.setDomainType(DomainType.PAYMENT_METHOD);
        paymentMethod.setDisplayFlag(true);
        paymentMethod.setDisplayName("test");

        when(cartServiceMock.getActualCart(session)).thenReturn(cart);
        when(principalMock.getName()).thenReturn("john");
        when(userServiceMock.getUserByUsername("john")).thenReturn(user);
        when(lookupServiceMock.getPaymentMethodByKey("test")).thenReturn(paymentMethod);

        boolean result = orderService.createOrder(orderRequest, session, principalMock);

        assertTrue(result);
    }
}
