package hu.progmasters.gmistore.dto.messages;

import hu.progmasters.gmistore.model.Message;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MessageDetails {

    private Long id;
    private String sender;
    private String receiver;
    private String subject;
    private String content;
    private Boolean read;
    private LocalDateTime timestamp;

    public MessageDetails(Message message) {
        this.id = message.getId();
        this.sender = message.getSender().getUsername();
        this.receiver = message.getReceiver().getUsername();
        this.subject = message.getSubject();
        this.content = message.getContent();
        this.read = message.isRead();
        this.timestamp = message.getTimestamp();
    }
}
