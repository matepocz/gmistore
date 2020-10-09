package hu.progmasters.gmistore.service;

import hu.progmasters.gmistore.dto.CartDto;
import hu.progmasters.gmistore.enums.DomainType;
import hu.progmasters.gmistore.model.*;
import hu.progmasters.gmistore.repository.CartRepository;
import hu.progmasters.gmistore.repository.ProductRepository;
import hu.progmasters.gmistore.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    private CartService cartService;

    @Mock
    private CartRepository cartRepositoryMock;

    @Mock
    private ProductRepository productRepositoryMock;

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private ShippingService shippingServiceMock;

    @Mock
    private HttpSession sessionMock;

    @BeforeEach
    public void setup() {
        cartService = new CartService(
                cartRepositoryMock, productRepositoryMock, userRepositoryMock, shippingServiceMock);
    }

    private final Supplier<ShippingMethod> shippingMethodSupplier = () -> {
        ShippingMethod shippingMethod = new ShippingMethod();
        shippingMethod.setId(1);
        shippingMethod.setMethod("Next day");
        shippingMethod.setCost(1000.0);
        return shippingMethod;
    };

    private final Supplier<User> userSupplier = () -> {
        User user = new User();
        user.setId(1L);
        return user;
    };

    private final Supplier<Cart> cartSupplier = () -> {
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setItems(new HashSet<>());
        cart.setUser(userSupplier.get());
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
        inventory.setQuantityAvailable(15);
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
        cartItem.setCount(2);
        cartItem.setProduct(productSupplier.get());
        return cartItem;
    };

    @Test
    public void testCreateCartItem() {
        CartItem newCartItem = cartService.createNewCartItem(2, productSupplier.get());
        assertEquals(2, newCartItem.getCount());
        assertEquals(1L, newCartItem.getProduct().getId());
        assertEquals("GMI123", newCartItem.getProduct().getProductCode());
    }

    @Test
    public void testSetItemsTotalPrice() {
        Cart cart = cartSupplier.get();
        Set<CartItem> items = cart.getItems();

        CartItem cartItem1 = cartItemSupplier.get();
        items.add(cartItem1);

        Product product2 = productSupplier.get();
        product2.setId(2L);

        CartItem cartItem2 = cartItemSupplier.get();
        cartItem2.setId(2L);
        cartItem2.setProduct(product2);
        items.add(cartItem2);

        cartService.setItemsTotalPrice(cart);
        assertEquals(400.0, cart.getItemsTotalPrice());
    }

    @Test
    public void testSetCartsTotalPrice() {
        Cart cart = cartSupplier.get();
        cart.setItemsTotalPrice(300.0);
        cart.setShippingMethod(shippingMethodSupplier.get());

        cartService.setCartsTotalPrice(cart);

        assertEquals(1300.0, cart.getTotalPrice());
    }

    @Test
    public void testGetNumberOfItemsInCart() {
        Cart cart = cartSupplier.get();
        cart.setItemsTotalPrice(300.0);
        cart.setShippingMethod(shippingMethodSupplier.get());
        Set<CartItem> items = cart.getItems();

        CartItem cartItem1 = cartItemSupplier.get();
        items.add(cartItem1);

        Product product2 = productSupplier.get();
        product2.setId(2L);

        CartItem cartItem2 = cartItemSupplier.get();
        cartItem2.setId(2L);
        cartItem2.setProduct(product2);
        items.add(cartItem2);

        when(sessionMock.getAttribute("cart")).thenReturn(1L);
        when(cartRepositoryMock.findById(1L)).thenReturn(Optional.of(cart));

        cartService.setItemsTotalPrice(cart);
        cartService.setCartsTotalPrice(cart);

        int numberOfItemsInCart = cartService.getNumberOfItemsInCart(sessionMock);
        assertEquals(4, numberOfItemsInCart);
        verify(sessionMock, times(2)).getAttribute("cart");
        verify(cartRepositoryMock, times(1)).findById(1L);
    }

    @Test
    public void testSetInitialShippingMethod() {
        Cart cart = cartSupplier.get();
        ShippingMethod shippingMethod = shippingMethodSupplier.get();

        when(shippingServiceMock.getInitialShippingMethod()).thenReturn(shippingMethod);

        cartService.setInitialShippingMethod(cart);
        assertNotNull(cart.getShippingMethod());
        assertEquals(shippingMethod, cart.getShippingMethod());
        verify(shippingServiceMock, times(1)).getInitialShippingMethod();
    }

    @Test
    public void testUpdateShippingMethod() {
        Cart cart = cartSupplier.get();
        cart.setItemsTotalPrice(300.0);
        cart.setShippingMethod(shippingMethodSupplier.get());

        ShippingMethod shippingMethod2 = shippingMethodSupplier.get();
        shippingMethod2.setId(2);

        when(sessionMock.getAttribute("cart")).thenReturn(1L);
        when(cartRepositoryMock.findById(1L)).thenReturn(Optional.of(cart));
        when(shippingServiceMock.fetchShippingMethod("Test")).thenReturn(shippingMethod2);

        cartService.updateShippingMethod("Test", sessionMock);

        assertNotNull(cart.getShippingMethod());
        assertEquals(2, cart.getShippingMethod().getId());
        assertEquals(shippingMethod2, cart.getShippingMethod());
        verify(sessionMock, times(2)).getAttribute("cart");
        verify(cartRepositoryMock, times(1)).findById(1L);
        verify(shippingServiceMock, times(1)).fetchShippingMethod("Test");
    }

    @Test
    public void testUpdateProductCountWhereCountEqualsZero() {
        Cart cart = cartSupplier.get();
        cart.setItemsTotalPrice(300.0);
        cart.setShippingMethod(shippingMethodSupplier.get());
        Set<CartItem> items = cart.getItems();

        CartItem cartItem1 = cartItemSupplier.get();
        items.add(cartItem1);

        Product product2 = productSupplier.get();
        product2.setId(2L);

        CartItem cartItem2 = cartItemSupplier.get();
        cartItem2.setId(2L);
        cartItem2.setProduct(product2);
        items.add(cartItem2);

        when(sessionMock.getAttribute("cart")).thenReturn(1L);
        when(cartRepositoryMock.findById(1L)).thenReturn(Optional.of(cart));

        boolean result = cartService.updateProductCount(1L, 0, sessionMock);

        assertFalse(result);
        assertFalse(cart.getItems().contains(cartItem1));
        verify(sessionMock, times(2)).getAttribute("cart");
        verify(cartRepositoryMock, times(1)).findById(1L);
    }

    @Test
    public void testUpdateProductCountWhereCountEqualsTwentyShouldReturnFalse() {
        Cart cart = cartSupplier.get();
        cart.setItemsTotalPrice(300.0);
        cart.setShippingMethod(shippingMethodSupplier.get());
        Set<CartItem> items = cart.getItems();

        CartItem cartItem1 = cartItemSupplier.get();
        items.add(cartItem1);

        Product product2 = productSupplier.get();
        product2.setId(2L);

        CartItem cartItem2 = cartItemSupplier.get();
        cartItem2.setId(2L);
        cartItem2.setProduct(product2);
        items.add(cartItem2);

        when(sessionMock.getAttribute("cart")).thenReturn(1L);
        when(cartRepositoryMock.findById(1L)).thenReturn(Optional.of(cart));

        cartService.updateProductCount(1L, 20, sessionMock);

        assertTrue(cart.getItems().contains(cartItem1));
        assertEquals(2, cartItem1.getCount());
        verify(sessionMock, times(2)).getAttribute("cart");
        verify(cartRepositoryMock, times(1)).findById(1L);
    }

    @Test
    public void testUpdateProductCountWhereCountEqualsTenShouldReturnTrue() {
        Cart cart = cartSupplier.get();
        cart.setItemsTotalPrice(300.0);
        cart.setShippingMethod(shippingMethodSupplier.get());
        Set<CartItem> items = cart.getItems();

        CartItem cartItem1 = cartItemSupplier.get();
        items.add(cartItem1);

        Product product2 = productSupplier.get();
        product2.setId(2L);

        CartItem cartItem2 = cartItemSupplier.get();
        cartItem2.setId(2L);
        cartItem2.setProduct(product2);
        items.add(cartItem2);

        when(sessionMock.getAttribute("cart")).thenReturn(1L);
        when(cartRepositoryMock.findById(1L)).thenReturn(Optional.of(cart));

        boolean result = cartService.updateProductCount(1L, 10, sessionMock);

        assertTrue(result);
        assertTrue(cart.getItems().contains(cartItem1));
        assertEquals(10, cartItem1.getCount());
        verify(sessionMock, times(2)).getAttribute("cart");
        verify(cartRepositoryMock, times(1)).findById(1L);
    }

    @Test
    public void testRemoveCartItem() {
        Cart cart = cartSupplier.get();
        cart.setItemsTotalPrice(300.0);
        cart.setShippingMethod(shippingMethodSupplier.get());
        Set<CartItem> items = cart.getItems();

        CartItem cartItem1 = cartItemSupplier.get();
        items.add(cartItem1);

        when(sessionMock.getAttribute("cart")).thenReturn(1L);
        when(cartRepositoryMock.findById(1L)).thenReturn(Optional.of(cart));

        boolean result = cartService.removeCartItem(1L, sessionMock);

        assertTrue(result);
        assertFalse(cart.getItems().contains(cartItem1));
        verify(sessionMock, times(2)).getAttribute("cart");
        verify(cartRepositoryMock, times(1)).findById(1L);
    }

    @Test
    public void testCanCheckoutShouldReturnFalse() {
        Cart cart = cartSupplier.get();
        cart.setItemsTotalPrice(300.0);
        cart.setShippingMethod(shippingMethodSupplier.get());

        when(sessionMock.getAttribute("cart")).thenReturn(1L);
        when(cartRepositoryMock.findById(1L)).thenReturn(Optional.of(cart));

        boolean result = cartService.canCheckout(sessionMock);

        assertFalse(result);
        verify(sessionMock, times(2)).getAttribute("cart");
        verify(cartRepositoryMock, times(1)).findById(1L);
    }

    @Test
    public void testCanCheckoutProductInactiveShouldReturnFalse() {
        Cart cart = cartSupplier.get();
        cart.setItemsTotalPrice(300.0);
        cart.setShippingMethod(shippingMethodSupplier.get());
        Set<CartItem> items = cart.getItems();

        CartItem cartItem1 = cartItemSupplier.get();
        items.add(cartItem1);

        Product product2 = productSupplier.get();
        product2.setId(2L);
        product2.setActive(false);

        CartItem cartItem2 = cartItemSupplier.get();
        cartItem2.setId(2L);
        cartItem2.setProduct(product2);
        items.add(cartItem2);

        when(sessionMock.getAttribute("cart")).thenReturn(1L);
        when(cartRepositoryMock.findById(1L)).thenReturn(Optional.of(cart));

        boolean result = cartService.canCheckout(sessionMock);

        assertFalse(result);
        verify(sessionMock, times(2)).getAttribute("cart");
        verify(cartRepositoryMock, times(1)).findById(1L);
    }

    @Test
    public void testCanCheckoutShouldReturnTrue() {
        Cart cart = cartSupplier.get();
        cart.setItemsTotalPrice(300.0);
        cart.setShippingMethod(shippingMethodSupplier.get());
        Set<CartItem> items = cart.getItems();

        CartItem cartItem1 = cartItemSupplier.get();
        items.add(cartItem1);

        Product product2 = productSupplier.get();
        product2.setId(2L);

        CartItem cartItem2 = cartItemSupplier.get();
        cartItem2.setId(2L);
        cartItem2.setProduct(product2);
        items.add(cartItem2);

        when(sessionMock.getAttribute("cart")).thenReturn(1L);
        when(cartRepositoryMock.findById(1L)).thenReturn(Optional.of(cart));

        boolean result = cartService.canCheckout(sessionMock);

        assertTrue(result);
        verify(sessionMock, times(2)).getAttribute("cart");
        verify(cartRepositoryMock, times(1)).findById(1L);
    }

    @Test
    public void testCanCheckoutProductQuantityEqualsZeroShouldReturnFalse() {
        Cart cart = cartSupplier.get();
        cart.setItemsTotalPrice(300.0);
        cart.setShippingMethod(shippingMethodSupplier.get());
        Set<CartItem> items = cart.getItems();

        CartItem cartItem1 = cartItemSupplier.get();
        items.add(cartItem1);

        Product product2 = productSupplier.get();
        product2.setId(2L);
        product2.getInventory().setQuantityAvailable(0);

        CartItem cartItem2 = cartItemSupplier.get();
        cartItem2.setId(2L);
        cartItem2.setProduct(product2);
        items.add(cartItem2);

        when(sessionMock.getAttribute("cart")).thenReturn(1L);
        when(cartRepositoryMock.findById(1L)).thenReturn(Optional.of(cart));

        boolean result = cartService.canCheckout(sessionMock);

        assertFalse(result);
        verify(sessionMock, times(2)).getAttribute("cart");
        verify(cartRepositoryMock, times(1)).findById(1L);
    }

    @Test
    public void testGetCartDto() {
        Cart cart = cartSupplier.get();
        cart.setItemsTotalPrice(300.0);
        cart.setShippingMethod(shippingMethodSupplier.get());
        Set<CartItem> items = cart.getItems();

        CartItem cartItem1 = cartItemSupplier.get();
        items.add(cartItem1);

        when(sessionMock.getAttribute("cart")).thenReturn(1L);
        when(cartRepositoryMock.findById(1L)).thenReturn(Optional.of(cart));

        CartDto cartDto = cartService.getCart(sessionMock);

        assertNotNull(cartDto);
        verify(sessionMock, times(2)).getAttribute("cart");
        verify(cartRepositoryMock, times(1)).findById(1L);
    }

}
