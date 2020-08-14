package hu.progmasters.gmistore.dto;

import hu.progmasters.gmistore.model.ShippingMethod;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ShippingMethodItem {
    private int id;
    private String method;
    private double cost;
    private int days;

    public ShippingMethodItem(ShippingMethod shippingMethod) {
        if (shippingMethod != null) {
            this.id = shippingMethod.getId();
            this.method = shippingMethod.getMethod();
            this.cost = shippingMethod.getCost();
            this.days = shippingMethod.getDays();
        }
    }
}
