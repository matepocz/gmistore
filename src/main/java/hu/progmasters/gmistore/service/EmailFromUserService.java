package hu.progmasters.gmistore.service;

import hu.progmasters.gmistore.dto.EmailCreatingDto;
import hu.progmasters.gmistore.model.EmailFromUser;
import hu.progmasters.gmistore.repository.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmailFromUserService {
    private final EmailRepository emailRepository;

    @Autowired
    public EmailFromUserService(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }


    public void saveEmailFromUser(EmailCreatingDto emailCreatingDto) {
        EmailFromUser email = new EmailFromUser();
        email.setEmail(emailCreatingDto.getEmail());
        email.setSubject(emailCreatingDto.getSubject());
        email.setMessage(emailCreatingDto.getMessage());
        email.setMessageCreateTime(LocalDate.now());
        emailRepository.save(email);
    }

    public List<EmailCreatingDto> getAllIncomeEmails() {
        List<EmailFromUser> getAllIncomeEmail = emailRepository.findAll();

        List<EmailCreatingDto> collect = new ArrayList<>();
        for (EmailFromUser email : getAllIncomeEmail) {
            if (collect.size() < 20) {
                EmailCreatingDto emailCreatingDto = mapEmailModelToEmailDto(email);
                collect.add(emailCreatingDto);
            }else{
                break;
            }
        }
        return collect;
    }

    private EmailCreatingDto mapEmailModelToEmailDto(EmailFromUser email) {
        EmailCreatingDto emailCreatingDto = new EmailCreatingDto();
        emailCreatingDto.setId(email.getId());
        emailCreatingDto.setEmail(email.getEmail());
        emailCreatingDto.setSubject(email.getSubject());
        emailCreatingDto.setMessage(email.getMessage());
        emailCreatingDto.setMessageCreateTime(email.getMessageCreateTime());
        return emailCreatingDto;
    }
}
