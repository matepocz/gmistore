package hu.progmasters.gmistore.validator;

import hu.progmasters.gmistore.dto.EmailCreatingDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class EmailValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return EmailCreatingDto.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        EmailCreatingDto emailCreatingDto = (EmailCreatingDto) target;


        String email = emailCreatingDto.getEmail();
        String subject = emailCreatingDto.getSubject();
        String message = emailCreatingDto.getMessage();

        if (email == null || email.isEmpty()) {
            errors.rejectValue("email", "email.empty");
        } else if (!email.matches("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$")) {
            errors.rejectValue("email", "email.invalid");
        }

        if (subject == null || subject.isEmpty()) {
            errors.rejectValue("subject", "subject.is-empty");
        } else if (subject.length() < 3) {
            errors.rejectValue("subject", "subject.short");
        } else if (subject.length() > 30) {
            errors.rejectValue("subject", "subject.tooLong");
        }


        if (message == null || message.isEmpty()) {
            errors.rejectValue("message", "message.is-empty");
        } else if (message.length() < 3) {
            errors.rejectValue("message", "message.short");
        } else if (message.length() > 1000) {
            errors.rejectValue("message", "message.tooLong");
        }
    }
}
