package hu.progmasters.gmistore.dto.product;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;
@Getter
@Setter
public class ProductListDetailDto {
    private Long id;
    private String name;
    private String productCode;
    private Set<String> features;
    private String pictureUrl;
    private Set<String> pictures;
    private Double price;
    private int discount;
    private double averageRating;
    private String orderItemId;
}
