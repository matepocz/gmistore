package hu.progmasters.gmistore.controller;

import hu.progmasters.gmistore.dto.CustomerDetails;
import hu.progmasters.gmistore.dto.OrderRequest;
import hu.progmasters.gmistore.service.OrderService;
import hu.progmasters.gmistore.validator.OrderRequestValidator;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;
    private final OrderRequestValidator orderRequestValidator;

    @Autowired
    public OrderController(OrderService orderService, OrderRequestValidator orderRequestValidator) {
        this.orderService = orderService;
        this.orderRequestValidator = orderRequestValidator;
    }

    @InitBinder("orderRequest")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(orderRequestValidator);
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
    public ResponseEntity<Void> createOrder(@Valid @RequestBody OrderRequest orderRequest, HttpSession session) {
        orderService.createOrder(orderRequest, session);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
