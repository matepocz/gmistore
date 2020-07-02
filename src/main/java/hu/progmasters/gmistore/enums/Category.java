package hu.progmasters.gmistore.enums;

public enum Category {
    TV("Tv"), PHONE("Phone"), FRIDGE("Fridge"), COMPUTER("Computer"), PRINTER("Printer");

    private String category;

    Category(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}
