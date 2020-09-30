package hu.progmasters.gmistore.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UsernameDto {

    private String username;

    public UsernameDto(String username) {
        this.username = username;
    }
}
