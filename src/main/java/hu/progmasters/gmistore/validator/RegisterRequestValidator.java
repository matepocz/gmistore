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

        validateFirstName(errors, registerRequest);
        validateLastName(errors, registerRequest);
        validateUsername(errors, registerRequest);
        validateEmailAddress(errors, registerRequest);
        validatePassword(errors, registerRequest);

        if (registerRequest.getUsername() != null) {
            Optional<User> userWithUsername = userRepository.findUserByUsername(registerRequest.getUsername());
            if (userWithUsername.isPresent()) {
                errors.rejectValue("username", "user.username.alreadyTaken");
            }
        }

        if (registerRequest.getEmail() != null) {
            Optional<User> userWithEmail = userRepository.findUserByEmail(registerRequest.getEmail());
            if (userWithEmail.isPresent()) {
                errors.rejectValue("email", "user.email.alreadyRegistered");
            }
        }

    }

    private void validatePassword(Errors errors, RegisterRequest registerRequest) {
        if (registerRequest.getPassword() == null || registerRequest.getPassword().length() <= 1) {
            errors.rejectValue("password", "user.password.empty");
        }
        if (!registerRequest.getPassword().matches("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,}")) {
            errors.rejectValue("password", "user.password.invalid");
        }
    }

    private void validateEmailAddress(Errors errors, RegisterRequest registerRequest) {
        if (registerRequest.getEmail() == null || registerRequest.getEmail().length() <= 1) {
            errors.rejectValue("email", "user.email.empty");
        }
        if (registerRequest.getEmail() == null ||
                !registerRequest.getEmail().matches("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$")) {
            errors.rejectValue("email", "user.email.invalid");
        }
    }

    private void validateUsername(Errors errors, RegisterRequest registerRequest) {
        if (registerRequest.getUsername() == null) {
            errors.rejectValue("username", "user.username.empty");
        } else if (registerRequest.getUsername().length() < 5) {
            errors.rejectValue("username", "user.username.tooShort");
        } else if (!registerRequest.getUsername().matches("^[a-z0-9_-]{5,30}$")) {
            errors.rejectValue("username", "user.username.invalid");
        }
    }

    private void validateLastName(Errors errors, RegisterRequest registerRequest) {
        if (registerRequest.getLastName() == null) {
            errors.rejectValue("lastName", "user.lastName.empty");
        } else if (!registerRequest.getLastName().matches("^[-'a-zA-ZÀ-ÖØ-öø-ſ]+$")) {
            errors.rejectValue("lastName", "user.lastName.invalid");
        } else if (registerRequest.getLastName().length() < 3) {
            errors.rejectValue("lastName", "user.lastName.tooShort");
        }
    }

    private void validateFirstName(Errors errors, RegisterRequest registerRequest) {
        if (registerRequest.getFirstName() == null) {
            errors.rejectValue("firstName", "user.firstName.empty");
        } else if (!registerRequest.getFirstName().matches("^[-'a-zA-ZÀ-ÖØ-öø-ſ]+$")) {
            errors.rejectValue("firstName", "user.firstName.invalid");
        } else if (registerRequest.getFirstName().length() < 3) {
            errors.rejectValue("firstName", "user.firstName.tooShort");
        }
    }
}
