package hu.progmasters.gmistore.validator;

import hu.progmasters.gmistore.dto.UserEditableDetailsDto;
import hu.progmasters.gmistore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserEditValidator implements Validator {
    private final UserRepository userRepository;

    @Autowired
    public UserEditValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserEditableDetailsDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserEditableDetailsDto userDetails = (UserEditableDetailsDto) target;
        if (userDetails.getFirstName() == null || userDetails.getUsername().length() <= 1) {
            errors.rejectValue("firstName", "user.firstName.invalid");
        }
        if (userDetails.getLastName() == null || userDetails.getUsername().length() <= 1) {
            errors.rejectValue("lastName", "user.lastName.invalid");
        }
        if (userDetails.getPhoneNumber() == null
                || userDetails.getPhoneNumber().length() <= 6
                || !userDetails.getPhoneNumber().matches("[0-9]+")) {
            errors.rejectValue("phoneNumber", "user.phoneNumber.invalid");
        }

        //ShippingAddress
        if (userDetails.getShippingAddress().getPostcode() == null
                || userDetails.getShippingAddress().getPostcode().length() <= 1
                || !userDetails.getShippingAddress().getPostcode().matches("[0-9]+")) {
            errors.rejectValue("shippingAddress.postcode", "address.postcode.invalid");
        }

        if (userDetails.getShippingAddress().getNumber() == null
                || userDetails.getShippingAddress().getNumber() < 1
        ) {
            errors.rejectValue("shippingAddress.number", "address.number.invalid");
        }

        if (userDetails.getShippingAddress().getCity() == null
                || userDetails.getShippingAddress().getCity().length() <= 1
        ) {
            errors.rejectValue("shippingAddress.city", "address.city.empty");
        }

        if (userDetails.getShippingAddress().getCountry() == null
                || userDetails.getShippingAddress().getCountry().length() <= 1
        ) {
            errors.rejectValue("shippingAddress.country", "address.country.empty");
        }

        if (userDetails.getShippingAddress().getStreet() == null
                || userDetails.getShippingAddress().getStreet().length() <= 1
        ) {
            errors.rejectValue("shippingAddress.street", "address.street.empty");
        }

//        //BillingAddress
//        if (userDetails.getBillingAddress().getPostcode() == null
//                || userDetails.getBillingAddress().getPostcode().length() <= 1
//                || !userDetails.getBillingAddress().getPostcode().matches("[0-9]+")) {
//            errors.rejectValue("billingAddress.postcode", "address.postcode.invalid");
//        }
//
//        if (userDetails.getBillingAddress().getNumber() == null
//                || userDetails.getBillingAddress().getNumber() < 1
//        ) {
//            errors.rejectValue("billingAddress.number", "address.number.invalid");
//        }
//
//        if (userDetails.getBillingAddress().getCity() == null
//                || userDetails.getBillingAddress().getCity().length() <= 1
//        ) {
//            errors.rejectValue("billingAddress.city", "address.city.empty");
//        }
//
//        if (userDetails.getBillingAddress().getCountry() == null
//                || userDetails.getBillingAddress().getCountry().length() <= 1
//        ) {
//            errors.rejectValue("billingAddress.country", "address.country.empty");
//        }
//
//        if (userDetails.getBillingAddress().getStreet() == null
//                || userDetails.getBillingAddress().getStreet().length() <= 1
//        ) {
//            errors.rejectValue("billingAddress.street", "address.street.empty");
//        }
    }
}
