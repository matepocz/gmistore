package hu.progmasters.gmistore.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import hu.progmasters.gmistore.enums.OrderStatus;
import hu.progmasters.gmistore.enums.Role;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    @NotNull
    @Column(name = "unique_id", unique = true)
    private String uniqueId;

    @ManyToOne
    @JoinColumn(name = "order_status", referencedColumnName = "id")
    @NotNull
    private LookupEntity status;

    @ManyToMany
    private List<OrderStatusHistory> orderStatusList = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_item", referencedColumnName = "id")
    private Set<OrderItem> items = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "shipping_method", referencedColumnName = "id")
    private ShippingMethod shippingMethod;

    @ManyToOne
    @JoinColumn(name = "payment_method", referencedColumnName = "id")
    @NotNull
    private LookupEntity paymentMethod;

    @ManyToOne(cascade = CascadeType.ALL, targetEntity = Address.class)
    private Address deliveryAddress;

    @ManyToOne(cascade = CascadeType.ALL, targetEntity = Address.class)
    private Address invoiceAddress;

    @Column(name = "delivery_cost")
    @NotNull
    @Min(value = 0, message = "Delivery cost cannot be negative number")
    private Double deliveryCost;

    @Column(name = "total_price")
    @NotNull
    @Min(value = 0, message = "Total price cannot be negative number")
    private Double totalPrice;

    @NotNull
    @Column(name = "ordered_at")
    private LocalDateTime orderedAt;

    @NotNull
    @Column(name = "expected_delivery_date")
    private LocalDateTime expectedDeliveryDate;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonBackReference
    private User user;
}
