package hu.progmasters.gmistore.dto;

import hu.progmasters.gmistore.enums.OrderStatus;
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

    private Long id;
    private String generatedUniqueId;
    private OrderStatus status;
    private Integer quantity;
    private LocalDateTime date;
    private LocalDateTime deliveryDate;
    private List<Product> productList;
    private User user;
}
