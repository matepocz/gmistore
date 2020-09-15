package hu.progmasters.gmistore.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NewCategoryRequest {

    private String key;
    private String displayName;
    private String icon;
    private Boolean isActive;
    private String mainCategoryKey;
    private Boolean isSubCategory;
}
