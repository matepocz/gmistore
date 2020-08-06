package hu.progmasters.gmistore.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class CartDto {

    private Long id;
    private Set<CartItemDto> cartItems;
    private Double totalPrice;
}
