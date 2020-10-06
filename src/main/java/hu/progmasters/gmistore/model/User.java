package hu.progmasters.gmistore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import hu.progmasters.gmistore.enums.Role;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @NotNull
    @Column(name = "username", columnDefinition = "varchar(100)", unique = true)
    private String username;

    @Column(name = "last_name", columnDefinition = "varchar(100)")
    private String lastName;

    @Column(name = "first_name", columnDefinition = "varchar(100)")
    private String firstName;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "shipping_address_id", referencedColumnName = "id")
    private Address shippingAddress;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "billing_address_id", referencedColumnName = "id")
    private Address billingAddress;

    @NotNull
    @Column(name = "password")
    private String password;

    @NotNull
    @Email
    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phone_number", columnDefinition = "varchar(20)")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "roles")
    @Column(name = "roles")
    private List<Role> roles = new ArrayList<>();

    @Column(name = "registered")
    private LocalDateTime registered;

    @Column(name = "active", columnDefinition = "boolean default false")
    private boolean active;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private Cart cart;

    @ManyToMany(targetEntity = Product.class)
    private Set<Product> favoriteProducts;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<Order> orderList;

    @OneToMany(mappedBy = "sender")
    private List<Message> outgoingMessages;

    @OneToMany(mappedBy = "receiver")
    private List<Message> incomingMessages;
}
