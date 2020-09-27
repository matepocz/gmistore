package hu.progmasters.gmistore.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class EmailCreatingDto {
    private Long id;
    private String email;
    private String subject;
    private String message;
    private LocalDate messageCreateTime;

}
