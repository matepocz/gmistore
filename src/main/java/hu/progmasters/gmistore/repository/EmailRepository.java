package hu.progmasters.gmistore.repository;

import hu.progmasters.gmistore.model.EmailFromUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends JpaRepository<EmailFromUser, Long> {

    @Query("SELECT e FROM EmailFromUser e WHERE e.active = true")
    Page<EmailFromUser> findAllActiveEmail(Pageable pageable);

}
