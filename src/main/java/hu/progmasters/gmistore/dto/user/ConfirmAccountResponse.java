package hu.progmasters.gmistore.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfirmAccountResponse {

    private String result;
    private String email;
}
