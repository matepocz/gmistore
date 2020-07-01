package hu.progmasters.gmistore.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
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
}
