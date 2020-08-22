package hu.progmasters.gmistore.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class NewRatingRequest {
    private String product;
    private Integer actualRating;
    private String title;
    private String positiveComment;
    private String negativeComment;
    private Set<String> pictures;
}
