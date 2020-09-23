package hu.progmasters.gmistore.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hu.progmasters.gmistore.enums.Role;
import hu.progmasters.gmistore.model.Address;
import hu.progmasters.gmistore.model.Cart;
import hu.progmasters.gmistore.model.Order;
import hu.progmasters.gmistore.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class UserListDetailDto {
    private long id;
    private String username;
    private String email;
    private List<String> roles;
    private boolean active;

    public UserListDetailDto(User u) {
        this.id = u.getId();
        this.username = u.getUsername();
        this.email = u.getEmail();
        this.roles = u.getRoles().stream().map(Role::getDisplayName).collect(Collectors.toList());
        this.active = u.isActive();
    }
}
