package hu.progmasters.gmistore.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import hu.progmasters.gmistore.enums.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "product")
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "Product must have a name.")
    @Basic(optional = false)
    @Column(name = "name")
    private String name;

    @Column(name = "product_code", unique = true)
    private String productCode;

    @Column(name = "slug", unique = true)
    private String slug;

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

    @OneToOne(mappedBy = "product", fetch = FetchType.LAZY)
    private Inventory inventory;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Rating> ratings;

    @Column(name = "average_rating", columnDefinition = "double default 0.0")
    private double averageRating;

    @Column(name = "active", columnDefinition = "boolean default true")
    private boolean active;

    @NotNull(message = "Username must not be empty.")
    @Column(name = "added_by", columnDefinition = "varchar(50)")
    private String addedBy;
}
