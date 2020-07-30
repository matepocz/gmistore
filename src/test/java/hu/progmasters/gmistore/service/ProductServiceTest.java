package hu.progmasters.gmistore.service;

import hu.progmasters.gmistore.dto.ProductDto;
import hu.progmasters.gmistore.enums.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(properties = "spring.profiles.active=test")
@TestPropertySource(properties = {"spring.config.location=classpath:application-test.yaml"})
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @BeforeEach
    public void setup() {
    }

    @Test
    public void testSaveProduct() {
        ProductDto productDto = new ProductDto();
        productDto.setName("test 1");
        productDto.setProductCode("GMI123");
        productDto.setDescription("test 1");
        productDto.setPrice(1.0);
        productDto.setCategory(Category.COMPUTER.toString());
        productDto.setActive(true);
        productDto.setQuantityAvailable(1);
        productDto.setAddedBy("matep");
        productService.addProduct(productDto);
        Assertions.assertNotNull(productService.getProductBySlug("test-1-gmi123"));
        Assertions.assertEquals("matep", productService.getProductBySlug("test-1-gmi123").getAddedBy());
    }

    @Test
    public void testUpdateProduct() {
        ProductDto productBySlug = productService.getProductBySlug("test-1-gmi123");
        ProductDto productDto = new ProductDto();
        productDto.setId(productBySlug.getId());
        productDto.setName("test 1");
        productDto.setProductCode("GMI123");
        productDto.setSlug("test-1-gmi123");
        productDto.setDescription("test 1");
        productDto.setPrice(10.0);
        productDto.setCategory(Category.PHONE.toString());
        productDto.setActive(true);
        productDto.setQuantityAvailable(1);
        boolean actualResult = productService.updateProduct("test-1-gmi123", productDto);
        ProductDto productAfterUpdate = productService.getProductBySlug("test-1-gmi123");
        Assertions.assertTrue(actualResult);
        Assertions.assertEquals(10.0, productAfterUpdate.getPrice());
    }
}
