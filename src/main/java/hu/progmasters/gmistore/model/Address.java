package hu.progmasters.gmistore.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "city")
    private String city;

    @Column(name = "street")
    private String street;

    @Column(name = "number")
    private Integer number;

    @Column(name = "floor")
    private Integer floor;

    @Column(name = "door")
    private Integer door;

    @Column(name = "country")
    private String country;

    @Column(name = "postcode")
    private String postcode;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(city).append(", ").append(street).append(" út/utca ").append(number).append("\n");
        if (floor != null && floor != 0) {
            sb.append(floor).append(" em. ");
        }
        if (door != null && door != 0) {
            sb.append(door).append(" ajtó. \n");
        }
        sb.append(country).append("\n");
        sb.append(postcode);

        return sb.toString();
    }
}
