package hu.progmasters.gmistore.dto.order;

import hu.progmasters.gmistore.dto.OrderItemListDetailsDto;
import hu.progmasters.gmistore.model.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class OrderDto {
    private String generatedUniqueId;
    private Set<OrderStatusDetailsDto> status;
    private Set<OrderItemListDetailsDto> items;
    private ShippingMethod shippingMethod;
    private String paymentMethod;
    private Address deliveryAddress;
    private Address invoiceAddress;
    private Double deliveryCost;
    private Double totalPrice;
    private LocalDateTime orderedAt;
    private LocalDateTime expectedDeliveryDate;
    private LocalDateTime deliveredAt;
    private OrderUserDetailsDto user;

    public OrderDto(Order order) {
        this.generatedUniqueId = order.getUniqueId();
        this.status = getAllStatuses(order);
        this.items = order.getItems().stream().map(OrderItemListDetailsDto::new).collect(Collectors.toSet());
        this.shippingMethod = order.getShippingMethod();
        this.paymentMethod = order.getPaymentMethod().getDisplayName();
        this.deliveryAddress = order.getDeliveryAddress();
        this.invoiceAddress = order.getInvoiceAddress();
        this.deliveryCost = order.getDeliveryCost();
        this.totalPrice = order.getTotalPrice();
        this.orderedAt = order.getOrderedAt();
        this.expectedDeliveryDate = order.getExpectedDeliveryDate();
        this.deliveredAt = order.getDeliveredAt();
        this.user = new OrderUserDetailsDto(order.getUser());
    }

    private Set<OrderStatusDetailsDto> getAllStatuses(Order order) {
        return order.getOrderStatusList().stream()
                .map(a -> new OrderStatusDetailsDto(a.getStatus().toString(),a.getStatus().getDisplayName(),a.getDate()))
                .collect(Collectors.toSet());
    }
}
