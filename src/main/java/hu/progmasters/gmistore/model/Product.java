package hu.progmasters.gmistore.model;


import hu.progmasters.gmistore.enums.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;

import javax.persistence.*;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    @Basic(optional = false)
    private String name;

    private String description;

    @NonNull
    private Category category;

    private String pictureUrl;

    @NonNull
    private double price;

    private int discount;

    private int warrantyMonths;

    @NonNull
    private int quantityAvailable;

    private double averageRating;
}
