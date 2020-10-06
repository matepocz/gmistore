package hu.progmasters.gmistore.dto.messages;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MessagesResponse {

    private List<MessageDetails> incoming;
    private List<MessageDetails> outgoing;
}
