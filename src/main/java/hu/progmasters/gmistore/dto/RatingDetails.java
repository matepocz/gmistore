package hu.progmasters.gmistore.dto;

import hu.progmasters.gmistore.model.Rating;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class RatingDetails {
    private Long id;
    private Boolean active;
    private String username;
    private int actualRating;
    private String title;
    private String positiveComment;
    private String negativeComment;
    private Set<String> pictures;
    private Integer upVotes;
    private Set<String> voters;
    private LocalDateTime timeStamp;

    public RatingDetails(Rating rating) {
        this.id = rating.getId();
        this.active = rating.getActive();
        this.username = rating.getUsername();
        this.actualRating = rating.getActualRating();
        this.title = rating.getTitle();
        this.positiveComment = rating.getPositiveComment();
        this.negativeComment = rating.getNegativeComment();
        this.pictures = rating.getPictures();
        this.upVotes = rating.getUpVotes();
        this.voters = rating.getVoters();
        this.timeStamp = rating.getTimeStamp();
    }
}
