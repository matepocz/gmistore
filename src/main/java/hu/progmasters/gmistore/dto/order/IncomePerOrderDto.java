package hu.progmasters.gmistore.dto.order;

import hu.progmasters.gmistore.model.OrderItem;
import hu.progmasters.gmistore.model.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IncomePerOrderDto {
    private Double income;
    private Double spent;
    private LocalDateTime date;
    private String orderId;

    public IncomePerOrderDto(Set<OrderItem> items, LocalDateTime date, String orderId) {
        this.income = items.stream().map(OrderItem::getProduct).mapToDouble(Product::getPrice).sum();
        this.spent = items.stream().map(OrderItem::getProduct).mapToDouble(Product::getPriceGross).sum();
        this.date = date;
        this.orderId = orderId;
    }
}
