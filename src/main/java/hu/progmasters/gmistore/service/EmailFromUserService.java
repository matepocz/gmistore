package hu.progmasters.gmistore.service;

import hu.progmasters.gmistore.dto.messages.EmailCreatingDto;
import hu.progmasters.gmistore.dto.messages.ReplyEmailDto;
import hu.progmasters.gmistore.model.EmailFromUser;
import hu.progmasters.gmistore.repository.EmailRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmailFromUserService {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(EmailFromUserService.class);

    private final EmailRepository emailRepository;
    private final EmailSenderService emailSenderService;

    @Autowired
    public EmailFromUserService(EmailRepository emailRepository, EmailSenderService emailSenderService) {
        this.emailRepository = emailRepository;
        this.emailSenderService = emailSenderService;
    }


    public void saveEmailFromUser(EmailCreatingDto emailCreatingDto) {
        EmailFromUser email = new EmailFromUser();
        email.setEmail(emailCreatingDto.getEmail());
        email.setSubject(emailCreatingDto.getSubject());
        email.setMessage(emailCreatingDto.getMessage());
        email.setMessageCreateTime(LocalDate.now());
        email.setActive(true);
        emailRepository.save(email);
        LOGGER.debug("Email has been saved.");
    }

    public List<EmailCreatingDto> getAllActiveIncomeEmails() {
        List<EmailFromUser> getAllIncomeEmail = emailRepository.findAllActiveEmail();

        List<EmailCreatingDto> collect = new ArrayList<>();
        for (EmailFromUser email : getAllIncomeEmail) {
            if (collect.size() < 20) {
                EmailCreatingDto emailCreatingDto = mapEmailModelToEmailDto(email);
                collect.add(emailCreatingDto);
            } else {
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


    public boolean sendReplyEmail(ReplyEmailDto replyEmailDto) {
        Optional<EmailFromUser> byId = emailRepository.findById(replyEmailDto.getId());
        if (byId.isPresent()) {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(byId.get().getEmail());
            mailMessage.setSubject("RE: " + byId.get().getSubject());
            mailMessage.setFrom("gmistarter@gmail.com");
            mailMessage.setText(replyEmailDto.getMessage());
            emailSenderService.sendEmail(mailMessage);
            LOGGER.debug("Email has been send");
            return true;
        }
        return false;
    }

    public boolean deleteEmail(Long id) {
        Optional<EmailFromUser> optionalEmail = emailRepository.findById(id);
        if(optionalEmail.isPresent()){
            EmailFromUser currentEmail = optionalEmail.get();
            currentEmail.setActive(false);
            emailRepository.save(currentEmail);
            LOGGER.debug("Email has been set to inactive Id : {}", id);
            return true;
        }
        LOGGER.warn("Email delete request, but email not found! id: {}", id);
        return false;
    }
}
