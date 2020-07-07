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
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

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

    public User() {
        roles.add(Role.ROLE_USER);
    }
}
