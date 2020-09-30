package hu.progmasters.gmistore.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import hu.progmasters.gmistore.model.OrderItem;
import hu.progmasters.gmistore.model.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IncomePerOrderDto {
    private Double income;
    @JsonFormat(pattern = "YYYY.MMM.dd")
    private LocalDateTime date;
    private String orderId;
}

