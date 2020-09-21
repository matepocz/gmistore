package hu.progmasters.gmistore.dto.order;

import hu.progmasters.gmistore.dto.CartItemDto;
import hu.progmasters.gmistore.enums.OrderStatus;
import hu.progmasters.gmistore.model.Address;
import hu.progmasters.gmistore.model.Product;
import hu.progmasters.gmistore.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrderDto {
    private Long generatedUniqueId;
    private String status;
    private List<CartItemDto> items;
    private String shippingMethod;
    private String deliveryMode;
    private String paymentMethod;
    private Address deliveryAddress;
    private Address  invoiceAddress;
    private Double deliveryCost;
    private Double totalPrice;
    private LocalDateTime orderedAt;
    private LocalDateTime expectedDeliveryDate;
    private LocalDateTime deliveredAt;
    private OrderUserDetailsDto user;
}
