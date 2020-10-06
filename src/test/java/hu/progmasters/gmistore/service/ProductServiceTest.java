package hu.progmasters.gmistore.service;

import hu.progmasters.gmistore.dto.product.ProductCategoryDetails;
import hu.progmasters.gmistore.dto.product.ProductDto;
import hu.progmasters.gmistore.enums.DomainType;
import hu.progmasters.gmistore.enums.Role;
import hu.progmasters.gmistore.exception.ProductNotFoundException;
import hu.progmasters.gmistore.model.Inventory;
import hu.progmasters.gmistore.model.LookupEntity;
import hu.progmasters.gmistore.model.Product;
import hu.progmasters.gmistore.model.User;
import hu.progmasters.gmistore.repository.ProductRepository;
import hu.progmasters.gmistore.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Principal;
import java.util.*;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    private ProductService productService;

    @Mock
    private ProductRepository productRepositoryMock;

    @Mock
    private InventoryService inventoryServiceMock;

    @Mock
    private LookupService lookupServiceMock;

    @Mock
    private UserRepository userRepositoryMock;

    @Spy
    private List<Product> productListMock = new ArrayList<>();

    @Mock
    private Principal principal;

    @BeforeEach
    public void setup() {
        productService = new ProductService(
                productRepositoryMock, inventoryServiceMock, lookupServiceMock, userRepositoryMock);
        productListMock = new ArrayList<>();
    }

    private final Supplier<ProductDto> productDtoSupplier = () -> {
        ProductCategoryDetails mainCategoryDto = new ProductCategoryDetails();
        mainCategoryDto.setId(1L);
        mainCategoryDto.setKey("main_category");
        mainCategoryDto.setDisplayName("Main Category");

        ProductCategoryDetails subCategoryDto = new ProductCategoryDetails();
        mainCategoryDto.setId(2L);
        mainCategoryDto.setKey("sub_category");
        mainCategoryDto.setDisplayName("Sub Category");


        ProductDto productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setName("test 1");
        productDto.setProductCode("GMI123");
        productDto.setDescription("test 1");
        productDto.setMainCategory(mainCategoryDto);
        productDto.setSubCategory(subCategoryDto);
        productDto.setActive(true);
        productDto.setQuantityAvailable(1);
        productDto.setPrice(100.0);
        productDto.setDiscount(0);
        productDto.setWarrantyMonths(12);
        productDto.setAddedBy("unknown");
        return productDto;
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
    public void testSaveProduct() {
        ProductDto productDto = productDtoSupplier.get();
        when(productRepositoryMock.save(any(Product.class))).thenAnswer(returnsFirstArg());

        Product savedProduct = productService.addProduct(productDto);

        assertTrue(savedProduct.isActive());
        assertEquals("test 1", savedProduct.getName());
        assertEquals("test-1-gmi123", savedProduct.getSlug());
        assertEquals(100.0, savedProduct.getPrice());
    }

    @Test
    public void testUpdateProduct() {
        Product product = productSupplier.get();
        ProductDto productDto = productDtoSupplier.get();

        when(productRepositoryMock.findProductBySlug("test-product-gmi123")).thenReturn(Optional.of(product));
        when(productRepositoryMock.save(any(Product.class))).thenAnswer(returnsFirstArg());
        when(principal.getName()).thenReturn("unknown");

        boolean actualResult = productService.updateProduct("test-product-gmi123", productDto, principal);

        Optional<Product> productBySlug = productRepositoryMock.findProductBySlug("test-product-gmi123");
        if (productBySlug.isPresent()) {
            Product updatedProduct = productBySlug.get();
            Assertions.assertEquals(1L, updatedProduct.getId());
            Assertions.assertTrue(actualResult);
            Assertions.assertEquals(100.0, updatedProduct.getPrice());

            verify(productRepositoryMock, times(2)).findProductBySlug("test-product-gmi123");
            verify(productRepositoryMock, times(1)).save(any(Product.class));
        }
    }

    @Test
    public void testUpdateProductProductNotExists() {
        ProductDto productDto = productDtoSupplier.get();

        when(productRepositoryMock.findProductBySlug("test-product-gmi123")).thenReturn(Optional.empty());

        boolean actualResult = productService.updateProduct("test-product-gmi123", productDto, principal);

        assertFalse(actualResult);
        verify(productRepositoryMock, times(1)).findProductBySlug("test-product-gmi123");
    }

    @Test
    public void testGetProductBySlugSuccessfully() {
        Product product = productSupplier.get();

        when(productRepositoryMock.findProductBySlug("test-product-gmi123")).thenReturn(Optional.of(product));

        ProductDto productBySlug = productService.getProductBySlug("test-product-gmi123");
        assertNotNull(productBySlug);
        assertEquals(ProductDto.class, productBySlug.getClass());
        verify(productRepositoryMock, times(1)).findProductBySlug("test-product-gmi123");
    }

    @Test
    public void testGetProductBySlugShouldThrowProductNotFoundException() {
        when(productRepositoryMock.findProductBySlug("test-product")).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getProductBySlug("test-product"));
        verify(productRepositoryMock, times(1)).findProductBySlug("test-product");
    }

    @Test
    public void testGetProductInOffer() {
        Product product1 = productSupplier.get();
        product1.setDiscount(10);

        Product product2 = productSupplier.get();
        product1.setId(2L);
        product2.setDiscount(20);

        Product product3 = productSupplier.get();
        product1.setId(3L);
        product3.setDiscount(30);

        productListMock.add(product1);
        productListMock.add(product2);
        productListMock.add(product3);

        when(productRepositoryMock.findProductByAndDiscountOrderByDiscountAsc()).thenReturn(productListMock);

        List<ProductDto> productInOffer = productService.getProductInOffer();

        assertNotNull(productInOffer);
        assertEquals(3, productInOffer.size());
        verify(productRepositoryMock, times(1)).findProductByAndDiscountOrderByDiscountAsc();
    }

    @Test
    public void testGetProductNamesReturnsThree() {
        Set<String> names = new HashSet<>();
        Product product1 = productSupplier.get();
        product1.setName("Test 1");

        Product product2 = productSupplier.get();
        product2.setName("Test 2");

        Product product3 = productSupplier.get();
        product3.setName("Test 3");

        names.add(product1.getName());
        names.add(product2.getName());
        names.add(product3.getName());

        when(productRepositoryMock.findProductNames("Test")).thenReturn(names);

        Set<String> foundNames = productRepositoryMock.findProductNames("Test");

        assertEquals(3, foundNames.size());
        verify(productRepositoryMock, times(1)).findProductNames("Test");
    }

    @Test
    public void testDeleteProductUnsuccessful() {
        Product product = productSupplier.get();
        product.setAddedBy("anonymous");

        when(productRepositoryMock.findById(1L)).thenReturn(Optional.of(product));
        when(principal.getName()).thenReturn("unknown");

        boolean result = productService.deleteProduct(1L, principal);

        assertFalse(result);
        assertTrue(product.isActive());
        verify(productRepositoryMock, times(1)).findById(1L);
        verify(principal, times(3)).getName();
    }

    @Test
    public void testDeleteProductProductNotExists() {
        when(productRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        boolean result = productService.deleteProduct(1L, principal);

        assertFalse(result);
        verify(productRepositoryMock, times(1)).findById(1L);
    }

    @Test
    public void testDeleteProductAsNotAuthenticatedUserUnsuccessful() {
        Product product = productSupplier.get();

        boolean result = productService.deleteProduct(1L, null);

        assertFalse(result);
        assertTrue(product.isActive());
    }

    @Test
    public void testDeleteProductAsSellerShouldSuccessfullyDelete() {
        Product product = productSupplier.get();

        when(productRepositoryMock.findById(1L)).thenReturn(Optional.of(product));
        when(principal.getName()).thenReturn("unknown");

        boolean result = productService.deleteProduct(1L, principal);

        assertTrue(result);
        assertFalse(product.isActive());
        verify(productRepositoryMock, times(1)).findById(1L);
        verify(principal, times(2)).getName();
    }

    @Test
    public void testDeleteProductAsAdminShouldSuccessfullyDelete() {
        Product product = productSupplier.get();

        User admin = new User();
        admin.setUsername("admin");
        admin.getRoles().add(Role.ROLE_ADMIN);

        when(productRepositoryMock.findById(1L)).thenReturn(Optional.of(product));
        when(principal.getName()).thenReturn("admin");
        when(userRepositoryMock.findUserByUsername("admin")).thenReturn(Optional.of(admin));

        boolean result = productService.deleteProduct(1L, principal);

        assertTrue(result);
        assertFalse(product.isActive());
        verify(productRepositoryMock, times(1)).findById(1L);
        verify(principal, times(2)).getName();
        verify(userRepositoryMock, times(1)).findUserByUsername("admin");
    }

    @Test
    public void testCountAllByActiveShouldReturnTwo() {
        Product product1 = productSupplier.get();
        Product product2 = productSupplier.get();
        Product product3 = productSupplier.get();
        product3.setActive(false);

        productListMock.add(product1);
        productListMock.add(product2);
        productListMock.add(product3);

        when(productRepositoryMock.countAllByActive(true)).thenReturn(2);

        Integer countAllByActive = productService.countAllByActive();

        assertEquals(2, countAllByActive);
        verify(productRepositoryMock, times(1)).countAllByActive(true);
    }

    @Test
    public void testCountAllByInActiveShouldReturnTwo() {
        Product product1 = productSupplier.get();
        Product product2 = productSupplier.get();
        product2.setActive(false);
        Product product3 = productSupplier.get();
        product3.setActive(false);

        productListMock.add(product1);
        productListMock.add(product2);
        productListMock.add(product3);

        when(productRepositoryMock.countAllByActive(false)).thenReturn(2);

        Integer countAllByInActive = productService.countAllByInActive();

        assertEquals(2, countAllByInActive);
        verify(productRepositoryMock, times(1)).countAllByActive(false);
    }
}
