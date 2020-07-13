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
    private Long id;

    @NotNull(message = "Product must have a name.")
    @Basic(optional = false)
    @Column(name = "name")
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "Product must have a category.")
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(name = "picture_url")
    private String pictureUrl;

    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "pictures")
    @Column(name = "product_pictures")
    private Set<String> pictures = new HashSet<>();

    @NotNull(message = "Product must have a price.")
    @Column(name = "price")
    private Double price;

    @Column(name = "discount", columnDefinition = "int default 0")
    private int discount;

    @Column(name = "warranty_months", columnDefinition = "int default 0")
    private int warrantyMonths;

    @OneToOne(mappedBy = "product", fetch = FetchType.EAGER)
    private Inventory inventory;

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

    @NotNull(message = "Username must not be empty.")
    @Column(name = "added_by", columnDefinition = "varchar(50)")
    private String addedBy;
}
