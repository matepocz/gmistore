package hu.progmasters.gmistore.dto;

import hu.progmasters.gmistore.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class UserRegistrationDateDto {
    Set<String> dates;
    Collection<Integer> size;


    public UserRegistrationDateDto(User u) {
    }

    public UserRegistrationDateDto(Map<String, Integer> sortedUserRegistrationByDate) {
        this.dates = sortedUserRegistrationByDate.keySet();
        this.size = sortedUserRegistrationByDate.values();
    }
}
