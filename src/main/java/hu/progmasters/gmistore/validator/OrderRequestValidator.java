package hu.progmasters.gmistore.validator;

import hu.progmasters.gmistore.dto.AddressDetails;
import hu.progmasters.gmistore.dto.order.OrderRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class OrderRequestValidator implements Validator {

    private static final String SHIPPING_ADDRESS = "shippingAddress";
    private static final String SHIPPING_ADDRESS_INVALID = "order.shippingAddress.invalid";

    private static final String BILLING_ADDRESS = "billingAddress";
    private static final String BILLING_ADDRESS_INVALID = "order.billingAddress.invalid";

    @Override
    public boolean supports(Class<?> clazz) {
        return OrderRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        OrderRequest orderRequest = (OrderRequest) target;

        validatePhoneNumber(errors, orderRequest);
        validateEmailAddress(errors, orderRequest);
        validateFirstAndLastName(errors, orderRequest);
        validateShippingAddress(errors, orderRequest);
        validateBillingAddress(errors, orderRequest);
    }

    private void validatePhoneNumber(Errors errors, OrderRequest orderRequest) {
        if (orderRequest.getPhoneNumber() == null) {
            errors.rejectValue("phoneNumber", "order.phoneNumber.empty");
        } else if (orderRequest.getPhoneNumber().length() < 10) {
            errors.rejectValue("phoneNumber", "order.phoneNumber.tooShort");
        } else if (orderRequest.getPhoneNumber().length() > 12) {
            errors.rejectValue("phoneNumber", "order.phoneNumber.tooLong");
        } else if (!orderRequest.getPhoneNumber().matches("^\\+[0-9]{11}$")) {
            errors.rejectValue("phoneNumber", "order.phoneNumber.invalid");
        }
    }

    private void validateBillingAddress(Errors errors, OrderRequest orderRequest) {
        AddressDetails billingAddress = orderRequest.getBillingAddress();
        if (billingAddress == null || billingAddress.getCity() == null ||
                billingAddress.getCity().length() < 3) {
            errors.rejectValue(BILLING_ADDRESS, BILLING_ADDRESS_INVALID);
        }

        if (billingAddress == null || billingAddress.getStreet() == null ||
                billingAddress.getStreet().length() < 2) {
            errors.rejectValue(BILLING_ADDRESS, BILLING_ADDRESS_INVALID);
        }

        assert billingAddress != null;
        if (billingAddress.getFloor() != null && billingAddress.getFloor() < 0) {
            errors.rejectValue("billingAddress.floor", "address.floor.invalid");
        }

        if (billingAddress.getDoor() != null && billingAddress.getDoor() < 0) {
            errors.rejectValue("billingAddress.door", "address.door.invalid");
        }

        if (billingAddress.getNumber() == null || billingAddress.getNumber() < 1) {
            errors.rejectValue(BILLING_ADDRESS, BILLING_ADDRESS_INVALID);
        }
        if (billingAddress.getPostcode().length() < 4 || !billingAddress.getPostcode().matches("^[0-9]{4}$")) {
            errors.rejectValue(BILLING_ADDRESS + ".postcode", "address.postcode.invalid");
        }
    }

    private void validateShippingAddress(Errors errors, OrderRequest orderRequest) {
        AddressDetails shippingAddress = orderRequest.getShippingAddress();
        if (shippingAddress == null || shippingAddress.getCity() == null ||
                shippingAddress.getCity().length() < 3) {
            errors.rejectValue(SHIPPING_ADDRESS, SHIPPING_ADDRESS_INVALID);
        }

        if (shippingAddress == null || shippingAddress.getStreet() == null ||
                shippingAddress.getStreet().length() < 2) {
            errors.rejectValue(SHIPPING_ADDRESS, SHIPPING_ADDRESS_INVALID);
        }

        assert shippingAddress != null;
        if (shippingAddress.getFloor() != null && shippingAddress.getFloor() < 0) {
            errors.rejectValue("shippingAddress.floor", "address.floor.invalid");
        }

        if (shippingAddress.getDoor() != null && shippingAddress.getDoor() < 0) {
            errors.rejectValue("shippingAddress.door", "address.door.invalid");
        }

        if (shippingAddress.getNumber() == null || shippingAddress.getNumber() < 1) {
            errors.rejectValue(SHIPPING_ADDRESS, SHIPPING_ADDRESS_INVALID);
        }
        if (shippingAddress.getPostcode().length() < 4) {
            errors.rejectValue("shippingAddress.postcode", "address.postcode.invalid");
        } else if (!shippingAddress.getPostcode().matches("^[0-9]{4}$")) {
            errors.rejectValue(SHIPPING_ADDRESS + ".postcode", "address.postcode.invalid");
        }
    }

    private void validateFirstAndLastName(Errors errors, OrderRequest orderRequest) {
        if (orderRequest.getFirstName() == null || orderRequest.getFirstName().length() < 3) {
            errors.rejectValue("firstName", "order.firstName.invalid");
        }

        if (orderRequest.getLastName() == null || orderRequest.getLastName().length() < 3) {
            errors.rejectValue("lastName", "order.lastName.invalid");
        }
    }

    private void validateEmailAddress(Errors errors, OrderRequest orderRequest) {
        if (orderRequest.getEmail() == null) {
            errors.rejectValue("email", "order.email.empty");
        } else if (!orderRequest.getEmail().matches("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$")) {
            errors.rejectValue("email", "order.email.invalid");
        }
    }
}
