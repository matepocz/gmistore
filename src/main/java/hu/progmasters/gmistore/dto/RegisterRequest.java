package hu.progmasters.gmistore.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RegisterRequest {

    private String username;
    private String lastName;
    private String firstName;
    private String city;
    private String street;
    private Integer number;
    private String postcode;
    private String password;
    private String email;
    private String phoneNumber;
    private LocalDateTime registered;
    private boolean active;
}
