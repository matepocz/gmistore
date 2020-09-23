package hu.progmasters.gmistore.dto;

import hu.progmasters.gmistore.dto.product.ProductCategoryDetails;
import hu.progmasters.gmistore.model.LookupEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MainCategoryDetails {

    private Long id;
    private String key;
    private String displayName;
    private String icon;
    private List<ProductCategoryDetails> subCategories;

    public MainCategoryDetails(LookupEntity category, List<ProductCategoryDetails> subCategories) {
        this.id = category.getId();
        this.key = category.getLookupKey();
        this.displayName = category.getDisplayName();
        this.icon = category.getCategoryIcon();
        this.subCategories = subCategories;
    }
}
