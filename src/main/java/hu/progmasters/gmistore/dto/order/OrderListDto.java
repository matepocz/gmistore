package hu.progmasters.gmistore.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import hu.progmasters.gmistore.model.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
        this.status = order.getStatus().getDisplayName();
        this.totalPrice = order.getTotalPrice();
    }
}
