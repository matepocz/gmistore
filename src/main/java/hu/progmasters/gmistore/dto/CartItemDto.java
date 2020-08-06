package hu.progmasters.gmistore.dto;

import hu.progmasters.gmistore.model.Product;
import hu.progmasters.gmistore.model.CartItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CartItemDto {
    private Long id;
    private ProductDto productDto;
    private Integer count;

    public CartItemDto(CartItem cartItem) {
        this.id = cartItem.getId();
        this.productDto = mapProductToProductDto(cartItem.getProduct());
        this.count = cartItem.getCount();
    }

    private ProductDto mapProductToProductDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setProductCode(product.getProductCode());
        productDto.setSlug(product.getSlug());
        productDto.setDescription(product.getDescription());
        productDto.setCategory(product.getCategory().getDisplayName());
        productDto.setPictureUrl(product.getPictureUrl());
        productDto.setPictures(product.getPictures());
        productDto.setPrice(product.getPrice());
        productDto.setDiscount(product.getDiscount());
        productDto.setWarrantyMonths(product.getWarrantyMonths());
        productDto.setQuantityAvailable(product.getInventory().getQuantityAvailable());
        productDto.setQuantitySold(product.getInventory().getQuantitySold());
        productDto.setRatings(product.getRatings());
        productDto.setAverageRating(product.getAverageRating());
        productDto.setActive(product.isActive());
        productDto.setAddedBy(product.getAddedBy());
        return productDto;
    }
}
