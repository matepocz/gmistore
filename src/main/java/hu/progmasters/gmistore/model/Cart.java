package hu.progmasters.gmistore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "cart")
public class Cart implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    private User user;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cart_items", referencedColumnName = "id")
    private Set<CartItem> items;

    @ManyToOne(targetEntity = ShippingMethod.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "shipping_method", referencedColumnName = "id")
    private ShippingMethod shippingMethod;

    @NotNull
    @Column(name = "items_total_price", columnDefinition = "double default 0.0")
    private Double itemsTotalPrice;

    @NotNull
    @Column(name = "total_price", columnDefinition = "double default 0.0")
    private Double totalPrice;

    @Column(name = "expected_delivery")
    private LocalDateTime expectedDeliveryDate;
}
