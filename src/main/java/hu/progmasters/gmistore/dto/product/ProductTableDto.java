package hu.progmasters.gmistore.dto.product;

import hu.progmasters.gmistore.model.LookupEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductTableDto {
    private Long id;
    private String name;
    private String categoryDisplayName;
    private double price;
    private boolean active;
    private String pictureUrl;
    private String slug;

    public ProductTableDto(Long id,
                           String name,
                           LookupEntity categoryDisplayName,
                           double price,
                           boolean active,
                           String pictureUrl,
                           String slug) {
        this.id = id;
        this.name = name;
        this.categoryDisplayName = categoryDisplayName.getDisplayName();
        this.price = price;
        this.active = active;
        this.pictureUrl = pictureUrl;
        this.slug = slug;
    }
}
