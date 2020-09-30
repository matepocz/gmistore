package hu.progmasters.gmistore.dto.messages;

import hu.progmasters.gmistore.model.EmailFromUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class EmailCreatingDto {
    private Long id;
    private String email;
    private String subject;
    private String message;
    private LocalDateTime messageCreateTime;
    private boolean active;

    public EmailCreatingDto(EmailFromUser emailFromUser) {
        this.id = emailFromUser.getId();
        this.email = emailFromUser.getEmail();
        this.subject = emailFromUser.getSubject();
        this.message = emailFromUser.getMessage();
        this.messageCreateTime = emailFromUser.getMessageCreateTime();
        this.active = emailFromUser.isActive();
    }
}
