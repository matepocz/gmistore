package hu.progmasters.gmistore.dto;

import hu.progmasters.gmistore.enums.Role;
import hu.progmasters.gmistore.model.Address;
import hu.progmasters.gmistore.model.Order;
import hu.progmasters.gmistore.model.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserEditableDetailsDto {
    private Long id;
    private String username;
    private String lastName;
    private String firstName;
    private Address shippingAddress;
    private Address billingAddress;
    private String phoneNumber;
    private List<Role> roles;

    public UserEditableDetailsDto(User user) {
        this.username = user.getUsername();
        this.lastName = user.getLastName();
        this.firstName = user.getFirstName();
        this.shippingAddress = user.getShippingAddress();
        this.billingAddress = user.getBillingAddress();
        this.phoneNumber = user.getPhoneNumber();
        this.roles = user.getRoles();
    }
}
