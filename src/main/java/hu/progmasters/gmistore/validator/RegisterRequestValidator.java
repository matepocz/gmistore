package hu.progmasters.gmistore.validator;

import hu.progmasters.gmistore.dto.RegisterRequest;
import hu.progmasters.gmistore.model.User;
import hu.progmasters.gmistore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class RegisterRequestValidator implements Validator {

    private final UserRepository userRepository;

    @Autowired
    public RegisterRequestValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return RegisterRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RegisterRequest registerRequest = (RegisterRequest) target;
        if (registerRequest.getUsername() == null || registerRequest.getUsername().length() <= 1) {
            errors.rejectValue("username", "user.username.empty");
        }
        if (registerRequest.getEmail() == null || registerRequest.getEmail().length() <= 1) {
            errors.rejectValue("email", "user.email.empty");
        }
        if (!registerRequest.getEmail().matches("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$")) {
            errors.rejectValue("email", "user.email.invalid");
        }
        if (registerRequest.getPassword() == null || registerRequest.getPassword().length() <= 1) {
            errors.rejectValue("password", "user.password.empty");
        }
        if (!registerRequest.getPassword().matches("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,}")) {
            errors.rejectValue("password", "user.password.invalid");
        }
        if (registerRequest.getCity() == null || registerRequest.getCity().length() <= 1) {
            errors.rejectValue("city", "user.city.empty");
        }
        if (registerRequest.getStreet() == null || registerRequest.getStreet().length() <= 1) {
            errors.rejectValue("street", "user.street.empty");
        }
        if (registerRequest.getNumber() == null || registerRequest.getNumber() <= 0) {
            errors.rejectValue("houseNumber", "user.houseNumber.empty");
        }
        if (registerRequest.getPostcode() == null) {
            errors.rejectValue("postcode", "user.postcode.empty");
        }
        Optional<User> userWithEmail = userRepository.findUserByEmail(registerRequest.getEmail());
        if (userWithEmail.isPresent()){
            errors.rejectValue("email", "user.email.alreadyRegistered");
        }
        Optional<User> userWithUsername = userRepository.findUserByUsername(registerRequest.getUsername());
        if (userWithUsername.isPresent()){
            errors.rejectValue("username", "user.username.alreadyTaken");
        }
    }
}
