package hu.progmasters.gmistore.dto.order;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderStatusOptionsDto {
    private String displayName;
    private String value;

    public OrderStatusOptionsDto(String value, String displayName) {
        this.displayName = displayName;
        this.value = value;
    }
}
