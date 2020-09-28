package hu.progmasters.gmistore.validator;

import hu.progmasters.gmistore.dto.messages.ReplyEmailDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ReplyEmailValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return ReplyEmailDto.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ReplyEmailDto replyEmailDto = (ReplyEmailDto) target;
        String message = replyEmailDto.getMessage();


        if (message == null || message.isEmpty()) {
            errors.rejectValue("message", "reply-email.empty");
        } else if (message.length() > 2000) {
            errors.rejectValue("message", "reply-email.tooLong");
        }
    }
}
