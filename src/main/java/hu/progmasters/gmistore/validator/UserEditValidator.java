package hu.progmasters.gmistore.validator;

import hu.progmasters.gmistore.dto.user.UserEditableDetailsDto;
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

        if (userDetails.getPhoneNumber() != null) {
            if (userDetails.getPhoneNumber().length() <= 6
                    || !userDetails.getPhoneNumber().matches("[0-9]+")) {
                errors.rejectValue("phoneNumber", "user.phoneNumber.invalid");
            }
        }

        //ShippingAddress
        if (userDetails.getShippingAddress().getPostcode() != null) {
            if (userDetails.getShippingAddress().getPostcode().length() <= 3
                    || !userDetails.getShippingAddress().getPostcode().matches("[0-9]+")) {
                errors.rejectValue("shippingAddress.postcode", "address.postcode.invalid");
            }
        }

        if (userDetails.getShippingAddress().getNumber() != null) {
            if (userDetails.getShippingAddress().getNumber() < 1
                    || userDetails.getShippingAddress().getNumber() > 99999999
            ) {
                errors.rejectValue("shippingAddress.number", "address.number.invalid");
            }
        }

        if (userDetails.getShippingAddress().getDoor() != null) {
            if (userDetails.getShippingAddress().getDoor() < 1
                    || userDetails.getShippingAddress().getNumber() > 99999999
            ) {
                errors.rejectValue("shippingAddress.number", "address.number.invalid");
            }
        }

        if (userDetails.getShippingAddress().getFloor() != null) {
            if (userDetails.getShippingAddress().getFloor() < 1
                    || userDetails.getShippingAddress().getNumber() > 99999999
            ) {
                errors.rejectValue("shippingAddress.number", "address.number.invalid");
            }
        }

        if (userDetails.getShippingAddress().getCity() != null) {
            if (userDetails.getShippingAddress().getCity().matches("[0-9]+")
            ) {
                errors.rejectValue("shippingAddress.city", "address.city.invalid");
            }
        }

        //Billing address
        if (userDetails.getBillingAddress().getPostcode() != null) {
            if (userDetails.getBillingAddress().getPostcode().length() <= 3
                    || !userDetails.getBillingAddress().getPostcode().matches("[0-9]+")) {
                errors.rejectValue("billingAddress.postcode", "address.postcode.invalid");
            }
        }

        if (userDetails.getBillingAddress().getNumber() != null) {
            if (userDetails.getBillingAddress().getNumber() < 1
                    || userDetails.getBillingAddress().getNumber() > 99999999
            ) {
                errors.rejectValue("billingAddress.number", "address.number.invalid");
            }
        }

        if (userDetails.getBillingAddress().getDoor() != null) {
            if (userDetails.getBillingAddress().getDoor() < 1
                    || userDetails.getBillingAddress().getNumber() > 99999999
            ) {
                errors.rejectValue("billingAddress.number", "address.number.invalid");
            }
        }

        if (userDetails.getBillingAddress().getFloor() != null) {
            if (userDetails.getBillingAddress().getFloor() < 1
                    || userDetails.getBillingAddress().getNumber() > 99999999
            ) {
                errors.rejectValue("billingAddress.number", "address.number.invalid");
            }
        }

        if (userDetails.getBillingAddress().getCity() != null) {
            if (userDetails.getBillingAddress().getCity().matches("[0-9]+")
            ) {
                errors.rejectValue("billingAddress.city", "address.city.invalid");
            }
        }
    }
}
