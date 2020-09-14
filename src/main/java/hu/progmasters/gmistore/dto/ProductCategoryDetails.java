package hu.progmasters.gmistore.dto;

import hu.progmasters.gmistore.model.LookupEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductCategoryDetails {

    private Long id;
    private String key;
    private String displayName;

    public ProductCategoryDetails(LookupEntity lookupEntity) {
        this.id = lookupEntity.getId();
        this.key = lookupEntity.getLookupKey();
        this.displayName = lookupEntity.getDisplayName();
    }
}
