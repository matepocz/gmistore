package hu.progmasters.gmistore.repository;

import hu.progmasters.gmistore.model.EmailFromUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmailRepository extends JpaRepository<EmailFromUser,Long> {

    @Query("SELECT e FROM EmailFromUser e WHERE e.active = true")
    List<EmailFromUser> findAllActiveEmail();

}
