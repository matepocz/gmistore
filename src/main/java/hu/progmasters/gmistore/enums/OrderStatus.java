package hu.progmasters.gmistore.enums;

import lombok.Getter;

public enum OrderStatus {
    UNPROCESSED("Unprocessed"),
    PROCESSED("Megerősítve"),
    UNDER_DELIVERY("Under delivery"),
    DELIVERED("Kiszállítva");

    @Getter
    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }
}
