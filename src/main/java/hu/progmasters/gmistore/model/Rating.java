package hu.progmasters.gmistore.model;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "product_rating")
public class Rating implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", columnDefinition = "varchar(50)")
    @NotNull
    private String username;

    @NotNull
    @Range(min = 1, max = 5)
    @Column(name = "rating")
    private int actualRating;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @Column(name = "timestamp")
    @NotNull
    private LocalDateTime timeStamp;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;
}
