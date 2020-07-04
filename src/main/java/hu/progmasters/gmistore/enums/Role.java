package hu.progmasters.gmistore.enums;

import lombok.Getter;

@Getter
public enum Role {
    ROLE_CUSTOMER("customer"),
    ROLE_ADMIN("admin");

    private String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }
}
