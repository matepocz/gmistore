package hu.progmasters.gmistore.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderStatusDetailsDto {
    private String displayName;
    private String value;
    @JsonFormat()
    private LocalDateTime date;

    public OrderStatusDetailsDto( String value,String displayName, LocalDateTime date) {
        this.displayName = displayName;
        this.value = value;
        this.date = date;
    }
}
