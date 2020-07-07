package hu.progmasters.gmistore.model;

import hu.progmasters.gmistore.enums.Role;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column(columnDefinition = "varchar(100)", unique = true)
    private String username;

    @Column(columnDefinition = "varchar(100)")
    private String lastName;

    @Column(columnDefinition = "varchar(100)")
    private String firstName;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @NotNull
    private String password;

    @NotNull
    @Email
    @Column(unique = true)
    private String email;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "roles")
    @Column(name = "roles")
    private List<Role> roles = new ArrayList<>();

    private LocalDateTime registered;

    @Column(columnDefinition = "boolean default false")
    private boolean active;

    @OneToOne(mappedBy = "user")
    private Order order;

    public User() {
        roles.add(Role.ROLE_USER);
    }
}
