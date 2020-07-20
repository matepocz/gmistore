package hu.progmasters.gmistore.enums;

import lombok.Getter;

public enum Category {
    TV("Tv"), MONITOR("Monitor"), PHONE("Mobiltelefon"),
    FRIDGE("Hűtő"), FREEZER("Fagyasztó"), COMPUTER("Számítógép"),
    LAPTOP("Laptop"), TABLET("Tablet"), PRINTER("Nyomtató"),
    NAVIGATION("Navigáció"), CAMERA("Fényképezőgép"), KEYBOARD("Billenytűzet"),
    MOUSE("Egér"), WASHING_MACHINE("Mosógép"), DRYER("Szárítógép"),
    PROCESSOR("Processzor"), RAM("Memória"), HDD("Merevlemez"),
    SSD("SSD"), MOTHERBOARD("Alaplap"), VGA("Videókártya"),
    UNKNOWN("Nem kategórizált");

    @Getter
    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }
}
