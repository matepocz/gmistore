package hu.progmasters.gmistore.controller;

import hu.progmasters.gmistore.dto.CustomerDetails;
import hu.progmasters.gmistore.service.OrderService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/customer-details")
    public ResponseEntity<CustomerDetails> getCustomerDetails() {
        LOGGER.debug("Customer details requested!");
        CustomerDetails customerDetails = orderService.getCustomerDetails();
        return customerDetails != null ?
                new ResponseEntity<>(customerDetails, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping
    public ResponseEntity<Void> createOrder(@RequestBody CustomerDetails customerDetails, HttpSession session) {
        orderService.createOrder(customerDetails, session);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
