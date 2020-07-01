package hu.progmasters.gmistore.service;

import hu.progmasters.gmistore.dto.ProductDto;
import hu.progmasters.gmistore.model.Product;
import hu.progmasters.gmistore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    /**
     * Get all products from the database
     *
     * @return A ProductDto List
     */
    public List<ProductDto> getAllProducts() {
        List<Product> allProduct = productRepository.findAll();
        return allProduct.stream().map(this::mapProductToProductDto).collect(Collectors.toList());
    }

    private ProductDto mapProductToProductDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setCategory(product.getCategory());
        productDto.setPictureUrl(product.getPictureUrl());
        productDto.setPrice(product.getPrice());
        productDto.setDiscount(product.getDiscount());
        productDto.setWarrantyMonths(product.getWarrantyMonths());
        productDto.setQuantityAvailable(product.getQuantityAvailable());
        productDto.setAverageRating(product.getAverageRating());
        return productDto;
    }
}
