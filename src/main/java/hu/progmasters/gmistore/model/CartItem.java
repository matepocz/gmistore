package hu.progmasters.gmistore.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name = "cart_item")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(targetEntity = Product.class)
    @NotNull
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @Column(name = "count")
    @NotNull
    @Min(value = 1, message = "Minimum 1 pc of this product!")
    private Integer count;
}
