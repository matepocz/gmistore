package hu.progmasters.gmistore.model;

import hu.progmasters.gmistore.enums.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @NotNull
    @Basic(optional = false)
    @Column(name = "name")
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(name = "picture_url")
    private String pictureUrl;

    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "pictures")
    @Column(name = "product_pictures")
    private Set<String> pictures = new HashSet<>();

    @NotNull
    @Column(name = "price")
    private double price;

    @Column(name = "discount", columnDefinition = "int default 0")
    private int discount;

    @Column(name = "warranty_months", columnDefinition = "int default 0")
    private int warrantyMonths;

    @Column(name = "quantity_available", columnDefinition = "int default 0")
    private int quantityAvailable;

    @ElementCollection(targetClass = Integer.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "ratings")
    @Column(name = "product_ratings")
    private List<Integer> ratings = new ArrayList<>();

    @Column(name = "average_rating", columnDefinition = "double default 0.0")
    private double averageRating;

    @Column(name = "active", columnDefinition = "boolean default true")
    private boolean active;

    @ManyToOne
    @JoinColumn(name = "product_order_id", referencedColumnName = "id")
    private Order productOrder;
}
