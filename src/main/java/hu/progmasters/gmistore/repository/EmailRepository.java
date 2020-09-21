package hu.progmasters.gmistore.repository;

import hu.progmasters.gmistore.model.EmailFromUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends JpaRepository<EmailFromUser,Long> {

}
