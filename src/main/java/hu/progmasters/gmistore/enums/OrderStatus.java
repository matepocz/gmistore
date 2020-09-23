package hu.progmasters.gmistore.enums;

import lombok.Getter;

import javax.persistence.Column;
import java.time.LocalDateTime;

public enum OrderStatus {
    UNPROCESSED("Unprocessed"),
    PROCESSED("Megerősítve"),
    UNDER_DELIVERY("Under delivery"),
    DELIVERED("Kiszállítva");

    @Getter
    private final String displayName;
    @Getter
    private final LocalDateTime statusDate;

    OrderStatus(String displayName) {
        this.displayName = displayName;
        this.statusDate = LocalDateTime.now();
    }
}
