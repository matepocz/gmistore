package hu.progmasters.gmistore.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductDto {

    private long id;
    private String name;
    private String description;
    private String category;
    private String pictureUrl;
    private double price;
    private int discount;
    private int warrantyMonths;
    private int quantityAvailable;
    private List<Integer> ratings;
    private double averageRating;
    private boolean active;
}
