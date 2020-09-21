package hu.progmasters.gmistore.dto.order;

import hu.progmasters.gmistore.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderUserDetailsDto {
    private Long id;
    private String lastName;
    private String firstName;
    private String email;
    private String phoneNumber;

    public OrderUserDetailsDto(User user) {
        this.id = user.getId();
        this.lastName = user.getLastName();
        this.firstName = user.getFirstName();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
    }
}
