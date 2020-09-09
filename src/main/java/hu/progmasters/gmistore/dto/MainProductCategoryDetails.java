package hu.progmasters.gmistore.dto;

import hu.progmasters.gmistore.model.LookupEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MainProductCategoryDetails {

    private Long id;
    private String key;
    private String displayName;

    public MainProductCategoryDetails(LookupEntity lookupEntity) {
        this.id = lookupEntity.getId();
        this.key = lookupEntity.getLookupKey();
        this.displayName = lookupEntity.getDisplayName();
    }
}
