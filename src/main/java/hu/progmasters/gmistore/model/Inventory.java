package hu.progmasters.gmistore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "inventory")
public class Inventory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(targetEntity = Product.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", unique = true, nullable = false)
    @JsonIgnore
    private Product product;

    @Column(name = "quantity_available", nullable = false)
    private int quantityAvailable;

    @Column(name = "quantity_sold", columnDefinition = "int default 0")
    private int quantitySold;

    @Column(name = "updated")
    private LocalDateTime updated;

    public Inventory(Product product, int quantityAvailable) {
        this.product = product;
        this.quantityAvailable = quantityAvailable;
        this.updated = LocalDateTime.now();
    }

}
