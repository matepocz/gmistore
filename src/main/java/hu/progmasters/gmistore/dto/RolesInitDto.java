package hu.progmasters.gmistore.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class RolesInitDto {
    List<RolesFormDto> roles;
}
