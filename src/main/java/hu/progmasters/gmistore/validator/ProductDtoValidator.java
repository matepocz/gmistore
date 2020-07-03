package hu.progmasters.gmistore.validator;


import hu.progmasters.gmistore.dto.ProductDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ProductDtoValidator implements Validator {

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
    }
}
