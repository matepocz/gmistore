package hu.progmasters.gmistore.service;

import hu.progmasters.gmistore.dto.product.ProductCategoryDetails;
import hu.progmasters.gmistore.dto.product.ProductDto;
import hu.progmasters.gmistore.enums.DomainType;
import hu.progmasters.gmistore.model.Inventory;
import hu.progmasters.gmistore.model.LookupEntity;
import hu.progmasters.gmistore.model.Product;
import hu.progmasters.gmistore.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
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

    @BeforeEach
    public void setup() {
        productService = new ProductService(
                productRepositoryMock, inventoryServiceMock, lookupServiceMock);
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

        boolean actualResult = productService.updateProduct("test-product-gmi123", productDto);

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

        boolean actualResult = productService.updateProduct("test-product-gmi123", productDto);

        assertFalse(actualResult);
        verify(productRepositoryMock, times(1)).findProductBySlug("test-product-gmi123");
    }
}
