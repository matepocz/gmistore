package hu.progmasters.gmistore.enums;

import lombok.Getter;

public enum OrderStatus {
    ORDERED("Megrendelve"),
    UNDER_DELIVERY("Kiszállítás alatt"),
    DELIVERED("Kiszállítva"),
    WAITING_PAYMENT("Fizetésre vár"),
    PAYMENT_SUCCESS("Kifizetve");

    @Getter
    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }
}
