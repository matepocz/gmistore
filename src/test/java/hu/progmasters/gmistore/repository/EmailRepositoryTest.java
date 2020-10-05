package hu.progmasters.gmistore.repository;

import hu.progmasters.gmistore.model.EmailFromUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class EmailRepositoryTest {
    @Autowired
    private EmailRepository emailRepository;

    @Test
    public void saveAndFindAllActiveEmailTest_returnSizeOne() {
        //given
        EmailFromUser emailFromUser = new EmailFromUser();
        emailFromUser.setId(1);
        emailFromUser.setEmail("teszt@gmail.com");
        emailFromUser.setSubject("tesz");
        emailFromUser.setMessage("teszt");
        emailFromUser.setMessageCreateTime(LocalDateTime.now());
        emailFromUser.setActive(true);
        emailRepository.save(emailFromUser);

        //when
        Pageable pageable = PageRequest.of(10, 10);
        Page<EmailFromUser> allActiveEmail = emailRepository.findAllActiveEmail(pageable);

        //then
        assertEquals(1, allActiveEmail.getTotalElements());

    }

    @Test
    public void saveInactiveEmail_findAll_resultSizeZero() {
        EmailFromUser email_1 = new EmailFromUser();
        email_1.setId(1);
        email_1.setEmail("teszt@gmail.com");
        email_1.setSubject("tesz");
        email_1.setMessage("teszt");
        email_1.setMessageCreateTime(LocalDateTime.now());
        email_1.setActive(false);
        emailRepository.save(email_1);

        //when
        Pageable pageable = PageRequest.of(10, 10);
        Page<EmailFromUser> allActiveEmail = emailRepository.findAllActiveEmail(pageable);

        //then
        assertEquals(0, allActiveEmail.getTotalElements());
    }

}
