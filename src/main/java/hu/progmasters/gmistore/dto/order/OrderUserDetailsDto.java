package hu.progmasters.gmistore.dto.order;

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
}
