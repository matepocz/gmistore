package hu.progmasters.gmistore.dto.product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductInCartDetails {
    private Long id;
    private String name;
    private String productCode;
    private String slug;
    private String pictureUrl;
    private Double price;
    private Integer discount;
    private Integer warrantyMonths;
    private Integer quantityAvailable;
}
