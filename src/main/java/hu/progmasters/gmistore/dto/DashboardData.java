package hu.progmasters.gmistore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DashboardData {
    private Integer customers;
    private Integer sellers;
    private Integer productsActive;
    private Integer productsInactive;
}
