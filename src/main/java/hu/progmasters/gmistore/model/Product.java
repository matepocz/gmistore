package hu.progmasters.gmistore.model;


import hu.progmasters.gmistore.enums.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    @Basic(optional = false)
    private String name;

    private String description;

    @NonNull
    @Enumerated(EnumType.STRING)
    private Category category;

    private String pictureUrl;

    @NonNull
    private double price;

    private int discount;

    private int warrantyMonths;

    @NonNull
    private int quantityAvailable;

    @ElementCollection(targetClass = Integer.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "ratings")
    @Column(name = "product_ratings")
    private List<Integer> ratings = new ArrayList<>();

    private double averageRating;
}
