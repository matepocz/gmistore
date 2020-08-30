package hu.progmasters.gmistore.service;

import hu.progmasters.gmistore.dto.AddressDetails;
import hu.progmasters.gmistore.dto.CustomerDetails;
import hu.progmasters.gmistore.model.User;
import hu.progmasters.gmistore.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;

    @Autowired
    public OrderService(OrderRepository orderRepository, UserService userService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
    }

    public CustomerDetails getCustomerDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByUsername(authentication.getName());
        if (user != null) {
            return setValuesForCustomerDetails(user);
        }
        return null;
    }

    private CustomerDetails setValuesForCustomerDetails(User user) {
        CustomerDetails customerDetails = new CustomerDetails();
        if (user.getShippingAddress() != null) {
            AddressDetails shippingAddress = new AddressDetails(user.getShippingAddress());
            customerDetails.setShippingAddress(shippingAddress);
        } else {
            customerDetails.setShippingAddress(null);
        }

        if (user.getBillingAddress() != null) {
            AddressDetails billingAddress = new AddressDetails(user.getShippingAddress());
            customerDetails.setBillingAddress(billingAddress);
        } else {
            customerDetails.setBillingAddress(null);
        }

        if (user.getPhoneNumber() != null) {
            customerDetails.setPhoneNumber(user.getPhoneNumber());
        } else {
            customerDetails.setPhoneNumber(null);
        }

        customerDetails.setFirstName(user.getFirstName());
        customerDetails.setLastName(user.getLastName());
        customerDetails.setEmail(user.getEmail());
        return customerDetails;
    }
}
