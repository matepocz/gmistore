package hu.progmasters.gmistore.model;

import org.springframework.lang.NonNull;

import javax.persistence.*;

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NonNull
    private String username;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @NonNull
    private String password;

    private String email;

    public Customer() {
    }

    public Customer(String username, Address address, String password, String email) {
        this.username = username;
        this.address = address;
        this.password = password;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    public Address getAddress() {
        return address;
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
