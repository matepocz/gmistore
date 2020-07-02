package hu.progmasters.gmistore.enums;

import lombok.Getter;

public enum Category {
    TV("Tv"), PHONE("Phone"), FRIDGE("Fridge"),
    COMPUTER("Computer"), PRINTER("Printer");

    @Getter
    private String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }
}
