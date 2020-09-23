package hu.progmasters.gmistore.service;

import hu.progmasters.gmistore.dto.EmailCreating;
import hu.progmasters.gmistore.model.EmailFromUser;
import hu.progmasters.gmistore.repository.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmailFromUserService {
    private final EmailRepository emailRepository;

    @Autowired
    public EmailFromUserService(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }


    public void saveEmailFromUser(EmailCreating emailCreating) {
        EmailFromUser email = new EmailFromUser();
        email.setEmail(emailCreating.getEmail());
        email.setSubject(emailCreating.getSubject());
        email.setMessage(emailCreating.getMessage());
        email.setMessageCreateTime(LocalDateTime.now());
        emailRepository.save(email);
    }
}
