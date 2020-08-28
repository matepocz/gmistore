package hu.progmasters.gmistore.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    private String username;
    private String lastName;
    private String firstName;
    private String password;
    private String confirmPassword;
    private String email;
    private Boolean seller;
    private boolean active;
}
