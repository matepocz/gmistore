package hu.progmasters.gmistore.service;

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
import org.springframework.mock.web.MockHttpSession;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    private MockHttpSession session;

    @BeforeEach
    public void setup() {
        cartService = new CartService(
                cartRepositoryMock, productRepositoryMock, userRepositoryMock, shippingServiceMock);
        session = new MockHttpSession();
    }

    private final Supplier<User> userSupplier = () -> {
        User user = new User();
        user.setId(1L);
        return user;
    };

    private final Supplier<Cart> cartSupplier = () -> {
        Cart cart = new Cart();
        cart.setId(1L);
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

    @Test
    public void testCreateCartItem() {
        CartItem newCartItem = cartService.createNewCartItem(2, productSupplier.get());
        assertEquals(2, newCartItem.getCount());
        assertEquals(1L, newCartItem.getProduct().getId());
        assertEquals("GMI123", newCartItem.getProduct().getProductCode());
    }

}
