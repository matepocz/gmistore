package hu.progmasters.gmistore.validator;

import hu.progmasters.gmistore.dto.CustomerDto;
import hu.progmasters.gmistore.model.Customer;
import hu.progmasters.gmistore.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class CustomerDtoValidator implements Validator {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerDtoValidator(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return CustomerDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CustomerDto customerDto = (CustomerDto) target;
        if (customerDto.getUsername() == null || customerDto.getUsername().length() <= 1) {
            errors.rejectValue("username", "customer.username.empty");
        }
        if (customerDto.getEmail() == null || customerDto.getEmail().length() <= 1) {
            errors.rejectValue("email", "customer.email.empty");
        }
        if (!customerDto.getEmail().matches("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$")) {
            errors.rejectValue("email", "customer.email.invalid");
        }
        if (customerDto.getPassword() == null || customerDto.getPassword().length() <= 1) {
            errors.rejectValue("password", "customer.password.empty");
        }
        if (!customerDto.getPassword().matches("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,}")) {
            errors.rejectValue("password", "customer.password.invalid");
        }
        if (customerDto.getCity() == null || customerDto.getCity().length() <= 1) {
            errors.rejectValue("city", "customer.city.empty");
        }
        if (customerDto.getStreet() == null || customerDto.getStreet().length() <= 1) {
            errors.rejectValue("street", "customer.street.empty");
        }
        if (customerDto.getNumber() == null || customerDto.getNumber() <= 0) {
            errors.rejectValue("houseNumber", "customer.houseNumber.empty");
        }
        if (customerDto.getPostcode() == null) {
            errors.rejectValue("postcode", "customer.postcode.empty");
        }
        Optional<Customer> customer = customerRepository.findCustomerByEmail(customerDto.getEmail());
        if (customer.isPresent()){
            errors.rejectValue("email", "customer.email.alreadyRegistered");
        }
    }
}
