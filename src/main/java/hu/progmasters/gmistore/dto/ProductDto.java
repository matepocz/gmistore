package hu.progmasters.gmistore.dto;

import hu.progmasters.gmistore.model.Rating;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ProductDto {

    private long id;
    private String name;
    private String productCode;
    private String slug;
    private String description;
    private String category;
    private String pictureUrl;
    private Set<String> pictures;
    private Double price;
    private int discount;
    private int warrantyMonths;
    private int quantityAvailable;
    private int quantitySold;
    private List<Rating> ratings;
    private double averageRating;
    private boolean active;
    private String addedBy;
}
