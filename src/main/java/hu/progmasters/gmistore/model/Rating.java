package hu.progmasters.gmistore.model;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "product_rating")
public class Rating implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "active", columnDefinition = "boolean default true")
    private Boolean active;

    @Column(name = "username", columnDefinition = "varchar(50)")
    @NotNull
    private String username;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @NotNull
    @Range(min = 1, max = 5)
    @Column(name = "rating")
    private int actualRating;

    @Column(name = "title")
    private String title;

    @Column(name = "positive_comment", columnDefinition = "TEXT")
    private String positiveComment;

    @Column(name = "negative_comment", columnDefinition = "TEXT")
    private String negativeComment;

    @ElementCollection(targetClass = String.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "rating_picture")
    @Column(name = "rating_pictures")
    private Set<String> pictures = new HashSet<>();

    @Column(name = "up_votes", columnDefinition = "int default 0")
    private Integer upVotes;

    @ElementCollection(targetClass = String.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "rating_voter")
    @Column(name = "rating_voters")
    private Set<String> voters = new HashSet<>();

    @Column(name = "is_reported")
    private Boolean reported;

    @Column(name = "timestamp")
    @NotNull
    private LocalDateTime timeStamp;

}
