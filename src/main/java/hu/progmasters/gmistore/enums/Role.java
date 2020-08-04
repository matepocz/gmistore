package hu.progmasters.gmistore.enums;

import lombok.Getter;

@Getter
public enum Role {
    ROLE_USER("user"),
    ROLE_ADMIN("admin"),
    ROLE_SELLER("seller");

    private String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }
}
