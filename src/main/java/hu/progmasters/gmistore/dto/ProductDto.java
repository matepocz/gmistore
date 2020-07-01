package hu.progmasters.gmistore.dto;

import hu.progmasters.gmistore.enums.Category;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto {

    private long id;
    private String name;
    private String description;
    private Category category;
    private String pictureUrl;
    private double price;
    private int discount;
    private int warrantyMonths;
    private int quantityAvailable;
    private double averageRating;
}
