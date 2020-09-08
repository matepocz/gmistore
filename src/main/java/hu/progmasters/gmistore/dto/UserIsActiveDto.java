package hu.progmasters.gmistore.dto;

import hu.progmasters.gmistore.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserIsActiveDto {
    private Long id;
    private boolean active;

    public UserIsActiveDto(User user) {
        this.id = user.getId();
        this.active = user.isActive();
    }
}
