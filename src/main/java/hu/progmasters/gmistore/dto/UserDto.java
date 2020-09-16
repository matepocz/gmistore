package hu.progmasters.gmistore.dto;

import hu.progmasters.gmistore.enums.Role;
import hu.progmasters.gmistore.model.Address;
import hu.progmasters.gmistore.model.Order;
import hu.progmasters.gmistore.model.OrderItem;
import hu.progmasters.gmistore.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private long id;
    private String username;
    private String lastName;
    private String firstName;
    private Address shippingAddress;
    private Address billingAddress;
    private String email;
    private String phoneNumber;
    private List<Role> roles = new ArrayList<>();
    private LocalDateTime registered;
    private boolean active;
    private List<Order> orderList;

    public UserDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.lastName = user.getLastName();
        this.firstName = user.getFirstName();
        this.shippingAddress = user.getShippingAddress();
        this.billingAddress = user.getBillingAddress();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.roles = user.getRoles();
        this.registered = user.getRegistered();
        this.active = user.isActive();
        this.orderList = user.getOrderList();
    }
}
