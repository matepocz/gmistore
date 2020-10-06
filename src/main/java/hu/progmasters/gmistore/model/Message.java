package hu.progmasters.gmistore.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private User sender;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "receiver_id", referencedColumnName = "id")
    private User receiver;

    @NotNull
    @Column(name = "subject", columnDefinition = "varchar(100)")
    private String subject;

    @NotNull
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "is_read", columnDefinition = "boolean default false")
    private boolean read;

    @Column(name = "display_for_sender", columnDefinition = "boolean default true")
    private boolean displayForSender;

    @Column(name = "display_for_receiver", columnDefinition = "boolean default true")
    private boolean displayForReceiver;

    @NotNull
    @Column(name = "timestamp")
    private LocalDateTime timestamp;
}
