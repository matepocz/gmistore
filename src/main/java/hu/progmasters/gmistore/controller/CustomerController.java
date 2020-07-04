package hu.progmasters.gmistore.controller;

import hu.progmasters.gmistore.dto.CustomerDto;
import hu.progmasters.gmistore.model.Customer;
import hu.progmasters.gmistore.service.CustomerService;
import hu.progmasters.gmistore.validator.CustomerDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerDtoValidator customerDtoValidator;

    @Autowired
    public CustomerController(CustomerService customerService, CustomerDtoValidator customerDtoValidator) {
        this.customerService = customerService;
        this.customerDtoValidator = customerDtoValidator;
    }

    @InitBinder("customerDto")
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(customerDtoValidator);
    }
    @PostMapping("/register")
    public ResponseEntity registerCustomer(@Valid @RequestBody CustomerDto customerDto) {
        Customer customer = customerService.registerCustomer(customerDto);
        return customer != null ?
                new ResponseEntity<>(HttpStatus.CREATED) :
                new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
