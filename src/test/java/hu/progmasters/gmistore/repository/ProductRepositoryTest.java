package hu.progmasters.gmistore.repository;

import hu.progmasters.gmistore.enums.DomainType;
import hu.progmasters.gmistore.model.Inventory;
import hu.progmasters.gmistore.model.LookupEntity;
import hu.progmasters.gmistore.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private LookupRepository lookupRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    private final Supplier<Product> productSupplier = () -> {

        Optional<LookupEntity> mainCategoryByKey =
                lookupRepository.findByDomainTypeAndLookupKey(
                        DomainType.PRODUCT_CATEGORY, "main_category");
        Optional<LookupEntity> subCategoryByKey =
                lookupRepository.findByDomainTypeAndLookupKey(
                        DomainType.PRODUCT_CATEGORY, "sub_category");
        Product product = new Product();

        Inventory inventory = new Inventory();
        inventory.setQuantityAvailable(1);
        inventory.setProduct(product);
        inventory.setQuantitySold(1);
        inventory.setUpdated(LocalDateTime.now());

        product.setName("test product");
        product.setProductCode("GMI123");
        product.setSlug("test-product-gmi123");
        product.setDescription("test description here 1");
        product.setMainCategory(mainCategoryByKey.get());
        product.setSubCategory(subCategoryByKey.get());
        product.setActive(true);
        product.setPrice(100.0);
        product.setDiscount(0);
        product.setWarrantyMonths(12);
        product.setAddedBy("unknown");
        product.setInventory(inventory);
        return product;
    };

    @BeforeEach
    public void init() {
        LookupEntity mainCategory = new LookupEntity();
        mainCategory.setDisplayFlag(true);
        mainCategory.setLookupKey("main_category");
        mainCategory.setDisplayName("Main Category");
        mainCategory.setDomainType(DomainType.PRODUCT_CATEGORY);
        lookupRepository.save(mainCategory);

        LookupEntity subCategory = new LookupEntity();
        subCategory.setDisplayFlag(true);
        subCategory.setLookupKey("sub_category");
        subCategory.setDisplayName("Sub Category");
        subCategory.setDomainType(DomainType.PRODUCT_CATEGORY);
        subCategory.setParent(mainCategory);
        lookupRepository.save(subCategory);

        LookupEntity subCategory2 = new LookupEntity();
        subCategory2.setDisplayFlag(true);
        subCategory2.setLookupKey("sub_category2");
        subCategory2.setDisplayName("Sub Category2");
        subCategory2.setDomainType(DomainType.PRODUCT_CATEGORY);
        subCategory2.setParent(mainCategory);
        lookupRepository.save(subCategory2);
    }

    @Test
    public void testSaveProduct() {
        Product product = productSupplier.get();
        Product savedProduct = productRepository.save(product);
        assertNotNull(savedProduct);
        assertEquals("GMI123", savedProduct.getProductCode());
    }

    @Test
    public void testFindProductBySlugProductExists() {
        Product product = productSupplier.get();
        productRepository.save(product);
        inventoryRepository.save(product.getInventory());
        Optional<Product> productBySlug = productRepository.findProductBySlug("test-product-gmi123");
        Product actualProduct = productBySlug.get();
        assertEquals("GMI123", actualProduct.getProductCode());
        assertEquals("test product", actualProduct.getName());
    }

    @Test
    public void testFindProductBySlugProductNotExists() {
        Optional<Product> productBySlug = productRepository.findProductBySlug("test-product-gmi123");
        assertTrue(productBySlug.isEmpty());
    }

    @Test
    public void testGetHighestPriceOneHundred() {
        Product product = productSupplier.get();
        product.setPrice(50.0);
        product.setProductCode("GMI50");
        productRepository.save(product);
        inventoryRepository.save(product.getInventory());

        Product product2 = productSupplier.get();
        product2.setPrice(100.0);
        product2.setMainCategory(product.getMainCategory());
        product2.setSubCategory(product.getMainCategory());
        product2.setProductCode("GMI100");
        product2.setSlug("test-product-gmi100");
        productRepository.save(product2);
        inventoryRepository.save(product2.getInventory());

        Double highestPrice = productRepository.getHighestPrice();
        assertEquals(100.0, highestPrice);
    }

    @Test
    public void testFindProductBySubCategoryReturnsTwo() {
        Product product = productSupplier.get();
        product.setProductCode("GMI50");
        productRepository.save(product);
        inventoryRepository.save(product.getInventory());

        Product product2 = productSupplier.get();
        product2.setProductCode("GMI100");
        product2.setSlug("test-product-gmi100");
        productRepository.save(product2);
        inventoryRepository.save(product2.getInventory());

        Optional<LookupEntity> subCategoryByKey =
                lookupRepository.findByDomainTypeAndLookupKey(
                        DomainType.PRODUCT_CATEGORY, "sub_category");
        Pageable pageable = PageRequest.of(0, 10);

        Page<Product> productsBySubCategory =
                productRepository.findProductsBySubCategory(subCategoryByKey.get(), pageable);
        assertEquals(2, productsBySubCategory.getTotalElements());
    }

    @Test
    public void testFindProductBySubCategoryReturnsOne() {
        Product product = productSupplier.get();
        product.setProductCode("GMI50");
        productRepository.save(product);
        inventoryRepository.save(product.getInventory());

        Optional<LookupEntity> subCategoryByKey2 =
                lookupRepository.findByDomainTypeAndLookupKey(
                        DomainType.PRODUCT_CATEGORY, "sub_category2");

        Product product2 = productSupplier.get();
        product2.setMainCategory(product.getMainCategory());
        product2.setSubCategory(subCategoryByKey2.get());
        product2.setProductCode("GMI100");
        product2.setSlug("test-product-gmi100");
        productRepository.save(product2);
        inventoryRepository.save(product2.getInventory());

        Optional<LookupEntity> subCategoryByKey =
                lookupRepository.findByDomainTypeAndLookupKey(
                        DomainType.PRODUCT_CATEGORY, "sub_category");
        Pageable pageable = PageRequest.of(0, 10);

        Page<Product> productsBySubCategory =
                productRepository.findProductsBySubCategory(subCategoryByKey.get(), pageable);
        assertEquals(1, productsBySubCategory.getTotalElements());
    }

    @Test
    public void testFindProductBySearchInputProductNameContainsInput() {
        Product product = productSupplier.get();
        productRepository.save(product);
        inventoryRepository.save(product.getInventory());
        Pageable pageable = PageRequest.of(0, 10);

        Page<Product> products = productRepository.findProductsBySearchInput("test", pageable);
        assertEquals(1, products.getTotalElements());
    }

    @Test
    public void testFindProductBySearchInput() {
        Product product = productSupplier.get();
        productRepository.save(product);
        inventoryRepository.save(product.getInventory());
        Pageable pageable = PageRequest.of(0, 10);

        Page<Product> products = productRepository.findProductsBySearchInput("description", pageable);
        assertEquals(1, products.getTotalElements());
    }

    @Test
    public void testFindProductByProductCode() {
        Product product = productSupplier.get();
        productRepository.save(product);
        inventoryRepository.save(product.getInventory());

        Optional<Product> productByProductCode = productRepository.findProductByProductCode("GMI123");
        Product foundProduct = productByProductCode.get();
        assertEquals("GMI123", foundProduct.getProductCode());
    }
}
