package hu.progmasters.gmistore.validator;

import hu.progmasters.gmistore.model.PasswordResetToken;
import hu.progmasters.gmistore.repository.PasswordTokenRepository;
import hu.progmasters.gmistore.dto.PasswordTokenDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class ResetPasswordValidator implements Validator {
    private final PasswordTokenRepository passwordResetTokenRepository;

    @Autowired
    public ResetPasswordValidator(PasswordTokenRepository passwordResetTokenRepository) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return PasswordTokenDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PasswordTokenDto passwordTokenDto = (PasswordTokenDto) target;

        if (passwordTokenDto.getPassword() == null || passwordTokenDto.getPassword().length() <= 1) {
            errors.rejectValue("password", "user.password.empty");
        }
        if (!passwordTokenDto.getPassword().matches("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,}")) {
            errors.rejectValue("password", "user.password.invalid");
        }
        PasswordResetToken token = passwordResetTokenRepository.findByToken(passwordTokenDto.getToken());
        System.out.println(token);
        if (token == null) {
            errors.rejectValue("token", "user.token.invalidToken");
        }
    }
}


