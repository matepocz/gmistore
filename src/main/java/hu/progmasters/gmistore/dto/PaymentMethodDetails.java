package hu.progmasters.gmistore.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaymentMethodDetails {
    private Long id;
    private String paymentMethod;
    private String displayName;

    public PaymentMethodDetails(Long id, String paymentMethod, String displayName) {
        this.id = id;
        this.paymentMethod = paymentMethod;
        this.displayName = displayName;
    }
}
