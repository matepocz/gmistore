package hu.progmasters.gmistore.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class CartDto {

    private Long id;
    private Set<CartItemDto> cartItems;
    private ShippingMethodItem shippingMethod;
    private Double itemsTotalPrice;
    private Double totalPrice;
    private LocalDateTime expectedDeliveryDate;

}
