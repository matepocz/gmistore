package hu.progmasters.gmistore.dto.messages;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReplyEmailDto {
    private Long id;
    private String email;
    private String subject;
    private String message;
    private boolean active;
}
