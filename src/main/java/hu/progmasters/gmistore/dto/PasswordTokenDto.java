package hu.progmasters.gmistore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordTokenDto {
    private String token;
    private String password;
    private String confirmPassword;
}
