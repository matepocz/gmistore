package hu.progmasters.gmistore.dto.product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductFilterOptions {
    private Boolean notInStock;
    private Boolean nonDiscounted;
    private Boolean discounted;
    private Integer minPrice;
    private Integer maxPrice;
    private Integer lowestRating;
}
