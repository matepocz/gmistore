package hu.progmasters.gmistore.dto;

import java.time.LocalDateTime;

public class UserRegistrationDTO {

    private long id;
    private LocalDateTime registered;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getRegistered() {
        return registered;
    }

    public void setRegistered(LocalDateTime registered) {
        this.registered = registered;
    }
}
