package hu.progmasters.gmistore.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "shipping_method")
@Getter
@Setter
public class ShippingMethod implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotNull
    @Column(name = "method")
    private String method;

    @NotNull
    @Column(name = "cost")
    private double cost;

    @NotNull
    @Column(name = "days")
    private int days;
}
