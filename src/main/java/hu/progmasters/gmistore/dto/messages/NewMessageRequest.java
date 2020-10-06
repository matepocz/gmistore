package hu.progmasters.gmistore.dto.messages;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewMessageRequest {

    private String receiver;
    private String subject;
    private String content;
}
