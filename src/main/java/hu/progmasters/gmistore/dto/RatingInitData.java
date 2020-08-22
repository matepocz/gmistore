package hu.progmasters.gmistore.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RatingInitData {

    private String name;

    public RatingInitData(String name) {
        this.name = name;
    }
}
