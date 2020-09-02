package hu.progmasters.gmistore.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "unique_id")
    private String uniqueId;

    @ManyToOne
    @JoinColumn(name = "order_status", referencedColumnName = "id")
    private LookupEntity status;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_item", referencedColumnName = "id")
    private Set<OrderItem> items;

    @ManyToOne
    @JoinColumn(name = "delivery_mode", referencedColumnName = "id")
    private LookupEntity deliveryMode;

    @ManyToOne
    @JoinColumn(name = "payment_method", referencedColumnName = "id")
    private LookupEntity paymentMethod;

    @Column(name = "delivery_cost")
    private Double deliveryCost;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "ordered_at")
    private LocalDateTime orderedAt;

    @Column(name = "expected_delivery_date")
    private LocalDateTime expectedDeliveryDate;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
