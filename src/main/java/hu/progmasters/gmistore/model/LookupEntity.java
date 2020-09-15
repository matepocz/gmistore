package hu.progmasters.gmistore.model;

import hu.progmasters.gmistore.enums.DomainType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name = "lookup")
public class LookupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "domain_type")
    private DomainType domainType;

    @NotNull
    @Column(name = "lookup_key", unique = true)
    private String lookupKey;

    @NotNull
    @Column(name = "display_name")
    private String displayName;

    @NotNull
    @Column(name = "order_sequence")
    private int orderSequence;

    @Column(name = "category_icon")
    private String categoryIcon;

    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private LookupEntity parent;

    @Column(name = "display_flag")
    private boolean displayFlag = true;
}
