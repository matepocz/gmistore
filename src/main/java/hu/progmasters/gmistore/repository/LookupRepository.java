package hu.progmasters.gmistore.repository;

import hu.progmasters.gmistore.enums.DomainType;
import hu.progmasters.gmistore.model.LookupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LookupRepository extends JpaRepository<LookupEntity, Long> {
    @Query("SELECT l FROM LookupEntity l WHERE l.domainType = :domainType AND l.displayFlag = true " +
            "ORDER BY l.orderSequence, l.displayName")
    List<LookupEntity> findByDomainType(DomainType domainType);

    @Query("SELECT l FROM LookupEntity l WHERE l.domainType = :domainType AND l.lookupKey = :lookupKey")
    LookupEntity findByDomainTypeAndLookupKey(DomainType domainType, String lookupKey);

}
