package hu.progmasters.gmistore.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NewMainCategoryRequest {

    private String key;
    private String displayName;
    private Boolean isActive;
}
