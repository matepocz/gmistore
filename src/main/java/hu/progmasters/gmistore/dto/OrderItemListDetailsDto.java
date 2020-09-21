package hu.progmasters.gmistore.dto;

import hu.progmasters.gmistore.dto.order.OrderProductDetailsDto;
import hu.progmasters.gmistore.model.OrderItem;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class OrderItemListDetailsDto {
    private Long id;
    private OrderProductDetailsDto product;
    private Integer quantity;
    private Double price;

    public OrderItemListDetailsDto(OrderItem orderItem) {
        this.id = orderItem.getId();
        this.product = new OrderProductDetailsDto(orderItem.getProduct());
        this.quantity = orderItem.getQuantity();
        this.price = orderItem.getPrice();
    }
}
