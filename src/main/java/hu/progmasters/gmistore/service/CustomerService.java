package hu.progmasters.gmistore.service;

import hu.progmasters.gmistore.dto.CustomerDto;
import hu.progmasters.gmistore.model.Address;
import hu.progmasters.gmistore.model.Customer;
import hu.progmasters.gmistore.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer registerCustomer(CustomerDto customerDto) {
        Customer customer = new Customer();
        Address address = mapAddressDetailsToAddress(customerDto, customer);
        mapCustomerDetailsToCustomer(customerDto, customer, address);
        return customerRepository.save(customer);
    }

    private void mapCustomerDetailsToCustomer(CustomerDto customerDto, Customer customer, Address address) {
        customer.setUsername(customerDto.getUsername());
        customer.setLastName(customerDto.getLastName());
        customer.setFirstName(customerDto.getFirstName());
        customer.setAddress(address);
        customer.setPassword(customerDto.getPassword());
        customer.setEmail(customerDto.getEmail());
        customer.setPhoneNumber(customerDto.getPhoneNumber());
        customer.setRegistered(LocalDateTime.now());
        customer.setActive(false);
    }

    private Address mapAddressDetailsToAddress(CustomerDto customerDto, Customer customer) {
        Address address = new Address();
        address.setCustomer(customer);
        address.setCity(customerDto.getCity());
        address.setStreet(customerDto.getStreet());
        address.setNumber(customerDto.getNumber());
        address.setPostcode(customerDto.getPostcode());
        return address;
    }
}
