package hu.progmasters.gmistore.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
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

    @Column(name = "method")
    private String method;

    @Column(name = "cost")
    private double cost;

    @Column(name = "days")
    private int days;
}
