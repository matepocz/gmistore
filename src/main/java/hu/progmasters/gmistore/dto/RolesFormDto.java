package hu.progmasters.gmistore.dto;

import hu.progmasters.gmistore.enums.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RolesFormDto {
    private String name;
    private String displayName;

    public RolesFormDto(Role value) {
        this.name = value.toString();
        this.displayName = value.getDisplayName();
    }
}
