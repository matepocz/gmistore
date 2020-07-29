package hu.progmasters.gmistore.service;

import hu.progmasters.gmistore.dto.ProductDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    public void testAddProduct() {
        ProductDto productDto = new ProductDto();

        Assertions.assertNotNull(productDto);
    }
}
