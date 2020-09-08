package hu.progmasters.gmistore.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDetails {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private AddressDetails shippingAddress;
    private AddressDetails billingAddress;
}
