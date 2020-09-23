package hu.progmasters.gmistore.validator;

import hu.progmasters.gmistore.dto.EmailCreating;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class EmailValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return EmailCreating.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        EmailCreating emailCreating = (EmailCreating) target;


        String email = emailCreating.getEmail();
        if (email == null || email.isEmpty()) {
            errors.rejectValue("email", "email.empty");
        } else if (!email.matches("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$")) {
            errors.rejectValue("email", "email.invalid");
        }

        String subject = emailCreating.getSubject();
        if (subject == null || subject.isEmpty()) {
            errors.rejectValue("subject", "subject.is-empty");
        }
    }
}
