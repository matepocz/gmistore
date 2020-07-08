package hu.progmasters.gmistore.enums;

import lombok.Getter;

public enum OrderStatus {
    UNPROCESSED("Unprocessed"),
    PROCESSED("Processed"),
    UNDER_DELIVERY("Under delivery"),
    DELIVERED("Delivered");

    @Getter
    private String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }
}
