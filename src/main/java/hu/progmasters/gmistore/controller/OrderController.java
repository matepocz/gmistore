package hu.progmasters.gmistore.controller;

import hu.progmasters.gmistore.dto.CustomerDetails;
import hu.progmasters.gmistore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/customer-details")
    public ResponseEntity<CustomerDetails> getCustomerDetails() {
        CustomerDetails customerDetails = orderService.getCustomerDetails();
        return customerDetails != null ?
                new ResponseEntity<>(customerDetails, HttpStatus.OK):
                new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
