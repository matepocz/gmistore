package hu.progmasters.gmistore.dto;

import hu.progmasters.gmistore.model.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
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

    public ProductDto() {
    }

    public ProductDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        if (product.getCategory() != null) {
            this.category = product.getCategory().getDisplayName();
        }
        if (product.getPictureUrl() != null) {
            this.pictureUrl = product.getPictureUrl();
        }
        this.price = product.getPrice();
        this.discount = product.getDiscount();
        this.warrantyMonths = product.getWarrantyMonths();
        this.quantityAvailable = product.getQuantityAvailable();
        if(product.getRatings() != null) {
            this.ratings = product.getRatings();
        }
        this.averageRating = product.getAverageRating();
        this.active = product.isActive();
    }
}
