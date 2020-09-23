package hu.progmasters.gmistore.dto;

import hu.progmasters.gmistore.dto.product.ProductInCartDetails;
import hu.progmasters.gmistore.model.CartItem;
import hu.progmasters.gmistore.model.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CartItemDto {
    private Long id;
    private ProductInCartDetails product;
    private Integer count;

    public CartItemDto(CartItem cartItem) {
        this.id = cartItem.getId();
        this.product = mapProductToDetails(cartItem.getProduct());
        this.count = cartItem.getCount();
    }

    private ProductInCartDetails mapProductToDetails(Product product) {
        ProductInCartDetails productDto = new ProductInCartDetails();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setProductCode(product.getProductCode());
        productDto.setSlug(product.getSlug());
        productDto.setPictureUrl(product.getPictureUrl());
        productDto.setPrice(product.getPrice());
        productDto.setDiscount(product.getDiscount());
        productDto.setWarrantyMonths(product.getWarrantyMonths());
        productDto.setQuantityAvailable(product.getInventory().getQuantityAvailable());
        return productDto;
    }
}
