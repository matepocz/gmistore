package hu.progmasters.gmistore.validator;


import hu.progmasters.gmistore.dto.ProductDto;
import hu.progmasters.gmistore.model.Product;
import hu.progmasters.gmistore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class ProductDtoValidator implements Validator {

    private final ProductRepository productRepository;

    @Autowired
    public ProductDtoValidator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return ProductDto.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ProductDto productDto = (ProductDto) o;
        if (productDto.getName() == null) {
            errors.rejectValue("name", "product.name.empty");
        }
        if (productDto.getCategory() == null) {
            errors.rejectValue("category", "product.category.empty");
        }
        if (productDto.getPrice() == null || productDto.getPrice() <= 0.0) {
            errors.rejectValue("price", "product.price.negative");
        }

        if (productDto.getId() == null) {
            Optional<Product> productByProductCode =
                    productRepository.findProductByProductCode(productDto.getProductCode());
            if (productDto.getProductCode() == null ||
                    (productByProductCode.isPresent() && !productDto.getId().equals(productByProductCode.get().getId()))) {
                errors.rejectValue("productCode", "product.productCode.alreadyExists");
            }
        }
    }
}
