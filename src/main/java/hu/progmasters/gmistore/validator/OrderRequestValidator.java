package hu.progmasters.gmistore.validator;

import hu.progmasters.gmistore.dto.OrderRequest;
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

        validateEmailAddress(errors, orderRequest);
        validateFirstAndLastName(errors, orderRequest);
        validateShippingAddress(errors, orderRequest);
        validateBillingAddress(errors, orderRequest);
    }

    private void validateBillingAddress(Errors errors, OrderRequest orderRequest) {
        if (orderRequest.getBillingAddress() == null || orderRequest.getBillingAddress().getCity() == null ||
                orderRequest.getBillingAddress().getCity().length() < 3) {
            errors.rejectValue(BILLING_ADDRESS, BILLING_ADDRESS_INVALID);
        }

        if (orderRequest.getBillingAddress() == null || orderRequest.getBillingAddress().getStreet() == null ||
                orderRequest.getBillingAddress().getStreet().length() < 2) {
            errors.rejectValue(BILLING_ADDRESS, BILLING_ADDRESS_INVALID);
        }

        if (orderRequest.getBillingAddress() == null || orderRequest.getBillingAddress().getNumber() == null ||
                orderRequest.getBillingAddress().getNumber() < 1) {
            errors.rejectValue(BILLING_ADDRESS, BILLING_ADDRESS_INVALID);
        }
    }

    private void validateShippingAddress(Errors errors, OrderRequest orderRequest) {
        if (orderRequest.getShippingAddress() == null || orderRequest.getShippingAddress().getCity() == null ||
                orderRequest.getShippingAddress().getCity().length() < 3) {
            errors.rejectValue(SHIPPING_ADDRESS, SHIPPING_ADDRESS_INVALID);
        }

        if (orderRequest.getShippingAddress() == null || orderRequest.getShippingAddress().getStreet() == null ||
                orderRequest.getShippingAddress().getStreet().length() < 2) {
            errors.rejectValue(SHIPPING_ADDRESS, SHIPPING_ADDRESS_INVALID);
        }

        if (orderRequest.getShippingAddress() == null || orderRequest.getShippingAddress().getNumber() == null ||
                orderRequest.getShippingAddress().getNumber() < 1) {
            errors.rejectValue(SHIPPING_ADDRESS, SHIPPING_ADDRESS_INVALID);
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
