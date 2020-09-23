package hu.progmasters.gmistore.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import hu.progmasters.gmistore.enums.OrderStatus;
import hu.progmasters.gmistore.model.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class OrderListDto {
    private String generatedUniqueId;
    private String username;
    @JsonFormat(pattern = "YYYY.MMM dd - HH:mm")
    private LocalDateTime date;
    private String status;
    private Double totalPrice;

    public OrderListDto(Order order) {
        this.generatedUniqueId = order.getUniqueId();
        this.username = order.getUser().getUsername();
        this.date = order.getOrderedAt();
        this.status = order.getStatus().toString();
        this.totalPrice = order.getTotalPrice();
    }

    public OrderListDto(String generatedUniqueId, String username, LocalDateTime date, OrderStatus status, Double totalPrice) {
        this.generatedUniqueId = generatedUniqueId;
        this.username = username;
        this.date = date;
        this.status = getStatusEnum(status);
        this.totalPrice = totalPrice;
    }

    private String getStatusEnum(OrderStatus status) {
        if (status == null) {
            return OrderStatus.ORDERED.getDisplayName();
        }
        return status.toString();
    }

}
