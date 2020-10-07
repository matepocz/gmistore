package hu.progmasters.gmistore.service;

import hu.progmasters.gmistore.dto.inventory.InventorySoldProductsDto;
import hu.progmasters.gmistore.enums.DomainType;
import hu.progmasters.gmistore.model.CartItem;
import hu.progmasters.gmistore.model.Inventory;
import hu.progmasters.gmistore.model.LookupEntity;
import hu.progmasters.gmistore.model.Product;
import hu.progmasters.gmistore.repository.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceTest {

    private InventoryService inventoryService;

    @Mock
    private InventoryRepository inventoryRepositoryMock;

    @BeforeEach
    public void init() {
        inventoryService = new InventoryService(inventoryRepositoryMock);
    }

    private final Supplier<Inventory> inventorySupplier = () -> {
        Inventory inventory = new Inventory();
        inventory.setId(1L);
        inventory.setProduct(new Product());
        inventory.setUpdated(LocalDateTime.now());
        inventory.setQuantitySold(1);
        inventory.setQuantityAvailable(20);
        return inventory;
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

        Inventory inventory = inventorySupplier.get();
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
    public void testSaveInventorySuccessfully() {
        Product product = productSupplier.get();
        Inventory inventory = inventorySupplier.get();
        inventory.setProduct(product);

        when(inventoryRepositoryMock.save(any(Inventory.class))).thenAnswer(returnsFirstArg());

        Inventory savedInventory = inventoryService.saveInventory(product, 10);

        assertNotNull(savedInventory);
        assertEquals(10, savedInventory.getQuantityAvailable());
        verify(inventoryRepositoryMock, times(1)).save(any());
    }

    @Test
    public void testFindInventoryByProduct() {
        Product product = productSupplier.get();
        Inventory inventory = inventorySupplier.get();
        inventory.setProduct(product);

        when(inventoryRepositoryMock.findByProduct(any(Product.class))).thenReturn(Optional.of(inventory));

        Inventory inventoryByProduct = inventoryService.findInventoryByProduct(product);

        assertNotNull(inventoryByProduct);
        assertEquals(20, inventoryByProduct.getQuantityAvailable());
    }

    @Test
    public void testUpdateAvailableAndSoldQuantities() {
        Set<CartItem> cartItems = new HashSet<>();

        Product product1 = productSupplier.get();
        Inventory inventory1 = inventorySupplier.get();
        inventory1.setProduct(product1);
        inventory1.setQuantityAvailable(20);
        inventory1.setQuantitySold(1);
        product1.setInventory(inventory1);

        Product product2 = productSupplier.get();
        product2.setId(2L);
        Inventory inventory2 = inventorySupplier.get();
        inventory2.setProduct(product2);
        inventory2.setQuantityAvailable(30);
        inventory2.setQuantitySold(10);
        product2.setInventory(inventory2);

        CartItem cartItem1 = new CartItem();
        cartItem1.setId(1L);
        cartItem1.setProduct(product1);
        cartItem1.setCount(5);
        cartItems.add(cartItem1);

        CartItem cartItem2 = new CartItem();
        cartItem2.setId(2L);
        cartItem2.setProduct(product2);
        cartItem2.setCount(10);
        cartItems.add(cartItem2);

        when(inventoryRepositoryMock.findByProduct(product1)).thenReturn(Optional.of(product1.getInventory()));
        when(inventoryRepositoryMock.findByProduct(product2)).thenReturn(Optional.of(product2.getInventory()));

        inventoryService.updateAvailableAndSoldQuantities(cartItems);

        assertEquals(15, product1.getInventory().getQuantityAvailable());
        assertEquals(6, product1.getInventory().getQuantitySold());
        assertEquals(20, inventory2.getQuantityAvailable());
        assertEquals(20, inventory2.getQuantitySold());
    }

    @Test
    public void testGetIncomeSpentByInventory() {
        Inventory inventory1 = inventorySupplier.get();
        inventory1.setQuantitySold(10);
        Product product1 = productSupplier.get();
        product1.setDiscount(5);
        product1.setPrice(120.0);
        product1.setPriceGross(100.0);
        inventory1.setProduct(product1);

        Inventory inventory2 = inventorySupplier.get();
        inventory2.setId(2L);
        inventory2.setQuantitySold(10);
        Product product2 = productSupplier.get();
        product2.setId(2L);
        product2.setPrice(240.0);
        product2.setPriceGross(200.0);
        product2.setDiscount(10);
        inventory2.setProduct(product2);

        Inventory inventory3 = inventorySupplier.get();
        inventory3.setId(3L);
        inventory3.setQuantitySold(10);
        Product product3 = productSupplier.get();
        product3.setId(3L);
        product3.setPrice(120.0);
        product3.setPriceGross(100.0);
        product3.setDiscount(10);
        inventory3.setProduct(product3);

        List<Inventory> inventories = new ArrayList<>();
        inventories.add(inventory1);
        inventories.add(inventory2);
        inventories.add(inventory3);

        when(inventoryRepositoryMock.findAll()).thenReturn(inventories);

        InventorySoldProductsDto incomeSpentByInventory = inventoryService.getIncomeSpentByInventory();

        assertNotNull(incomeSpentByInventory);
        assertEquals(43.8, incomeSpentByInventory.getDiscount());
        assertEquals(4380.0, incomeSpentByInventory.getIncome());
        assertEquals(12000, incomeSpentByInventory.getSpent());
    }
}
