package hu.progmasters.gmistore.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class IncomeByDaysDto {
    private Collection<Double> income;
    private Set<String> date;
}
