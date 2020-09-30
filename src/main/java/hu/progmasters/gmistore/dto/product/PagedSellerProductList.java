package hu.progmasters.gmistore.dto.product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PagedSellerProductList {
    private List<ProductTableDto> products;
    private Long totalElements;
}
