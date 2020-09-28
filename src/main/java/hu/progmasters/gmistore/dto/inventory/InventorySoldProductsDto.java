package hu.progmasters.gmistore.dto.inventory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InventorySoldProductsDto {
    private Double income;
    private Double spent;
}
