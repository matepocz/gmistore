package hu.progmasters.gmistore.validator;

import hu.progmasters.gmistore.dto.AddressDetails;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class AddressValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return AddressDetails.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AddressDetails addressDetails = (AddressDetails) target;
        if (addressDetails.getPostcode() != null) {
            if (addressDetails.getPostcode().length() <= 3
                    || addressDetails.getPostcode().matches("[0-9]+")) {
                errors.rejectValue("shippingAddress.postcode", "address.postcode.invalid");
            }
        }

        if (addressDetails.getNumber() != null) {
            if (addressDetails.getNumber() < 1
                    || addressDetails.getNumber() > 99999999
            ) {
                errors.rejectValue("shippingAddress.number", "address.number.invalid");
            }
        }

        if (addressDetails.getDoor() != null) {
            if (addressDetails.getDoor() < 1
                    || addressDetails.getNumber() > 99999999
            ) {
                errors.rejectValue("shippingAddress.number", "address.number.invalid");
            }
        }

        if (addressDetails.getFloor() != null) {
            if (addressDetails.getFloor() < 1
                    || addressDetails.getNumber() > 99999999
            ) {
                errors.rejectValue("shippingAddress.number", "address.number.invalid");
            }
        }

        if (addressDetails.getCity() != null) {
            if (addressDetails.getCity().matches("[0-9]+")
            ) {
                errors.rejectValue("shippingAddress.city", "address.city.invalid");
            }
        }
    }
}
