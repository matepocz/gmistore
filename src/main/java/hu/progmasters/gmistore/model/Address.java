package hu.progmasters.gmistore.model;

import org.springframework.lang.NonNull;

import javax.persistence.*;

@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne(mappedBy = "address")
    private Customer customer;

    @NonNull
    private String city;

    @NonNull
    private String street;

    @NonNull
    private int number;

    @NonNull
    private String postcode;

    public Address() {
    }

    public Address(Customer customer, String city, String street, int number, String postcode) {
        this.customer = customer;
        this.city = city;
        this.street = street;
        this.number = number;
        this.postcode = postcode;
    }

    public long getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    @NonNull
    public String getCity() {
        return city;
    }

    @NonNull
    public String getStreet() {
        return street;
    }

    public int getNumber() {
        return number;
    }

    @NonNull
    public String getPostcode() {
        return postcode;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setCity(@NonNull String city) {
        this.city = city;
    }

    public void setStreet(@NonNull String street) {
        this.street = street;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setPostcode(@NonNull String postcode) {
        this.postcode = postcode;
    }
}
