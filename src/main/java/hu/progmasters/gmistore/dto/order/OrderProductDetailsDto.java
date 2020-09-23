package hu.progmasters.gmistore.dto.order;

import hu.progmasters.gmistore.model.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderProductDetailsDto {
    private Long id;
    private String name;
    private String productCode;
    private String pictureUrl;
    private Double price;
    private Integer discount;

    public OrderProductDetailsDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.productCode = product.getProductCode();
        this.pictureUrl = product.getPictureUrl();
        this.price = product.getPrice();
        this.discount = product.getDiscount();
    }
}
