package hu.progmasters.gmistore.model;

import hu.progmasters.gmistore.enums.DomainType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "lookup")
public class LookupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "domain_type")
    private DomainType domainType;

    @Column(name = "lookup_key")
    private String lookupKey;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "order_sequence")
    private int orderSequence;

    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private LookupEntity parent;

    @Column(name = "display_flag")
    private boolean displayFlag = true;
}
