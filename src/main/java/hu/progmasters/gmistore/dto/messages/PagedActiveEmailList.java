package hu.progmasters.gmistore.dto.messages;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PagedActiveEmailList {
    private List<EmailTableDto> emails;
    private Long totalElements;
}
