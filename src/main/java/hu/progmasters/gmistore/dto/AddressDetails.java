package hu.progmasters.gmistore.dto;

import hu.progmasters.gmistore.model.Address;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddressDetails {
    private Long id;
    private String city;
    private String street;
    private Integer number;
    private Integer floor;
    private Integer door;
    private String country;
    private String postcode;

    public AddressDetails(Address address) {
        this.id = address.getId();
        this.city = address.getCity();
        this.street = address.getStreet();
        this.number = address.getNumber();
        this.floor = address.getFloor();
        this.door = address.getDoor();
        this.country = address.getCountry();
        this.postcode = address.getPostcode();
    }
}
