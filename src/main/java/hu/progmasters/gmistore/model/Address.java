package hu.progmasters.gmistore.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(mappedBy = "address")
    private User user;

    @NotNull
    private String city;

    @NotNull
    private String street;

    @NotNull
    private int number;

    @NotNull
    private String postcode;
}
