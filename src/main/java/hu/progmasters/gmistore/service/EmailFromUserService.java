package hu.progmasters.gmistore.service;

import hu.progmasters.gmistore.dto.messages.EmailCreatingDto;
import hu.progmasters.gmistore.dto.messages.EmailTableDto;
import hu.progmasters.gmistore.dto.messages.PagedActiveEmailList;
import hu.progmasters.gmistore.dto.messages.ReplyEmailDto;
import hu.progmasters.gmistore.model.EmailFromUser;
import hu.progmasters.gmistore.repository.EmailRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

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
        email.setMessageCreateTime(LocalDateTime.now());
        email.setActive(true);
        emailRepository.save(email);
        LOGGER.debug("Email has been saved.");
    }

    public PagedActiveEmailList getAllActiveIncomeEmails(Integer size,Integer page) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EmailFromUser> getAllIncomeEmail = emailRepository.findAllActiveEmail(pageable);

        PagedActiveEmailList activeEmailList = new PagedActiveEmailList();
        activeEmailList.setEmails(
                getAllIncomeEmail.stream().map(this::mapEmailToEmailTableDto)
                        .collect(Collectors.toList()));
        activeEmailList.setTotalElements(getAllIncomeEmail.getTotalElements());
        return activeEmailList;

    }

    private EmailTableDto mapEmailToEmailTableDto(EmailFromUser emailFromUser) {
        EmailTableDto emailTableDto = new EmailTableDto(emailFromUser);
        return emailTableDto;
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
        if (optionalEmail.isPresent()) {
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
