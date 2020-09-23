package hu.progmasters.gmistore.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "email")
public class EmailFromUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "email")
    @Email
    @NotNull(message = "Email must be not empty")
    private String email;

    @Column(name = "subject", columnDefinition = "TEXT")
    @NotNull
    private String subject;

    @Column(name = "message", columnDefinition = "TEXT")
    @NotNull
    private String message;

    @Column(name = "messageCreateTime")
    private LocalDateTime messageCreateTime;

}
