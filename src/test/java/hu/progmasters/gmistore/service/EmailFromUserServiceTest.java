package hu.progmasters.gmistore.service;

import hu.progmasters.gmistore.dto.messages.EmailCreatingDto;
import hu.progmasters.gmistore.dto.messages.EmailTableDto;
import hu.progmasters.gmistore.dto.messages.PagedActiveEmailList;
import hu.progmasters.gmistore.dto.messages.ReplyEmailDto;
import hu.progmasters.gmistore.model.EmailFromUser;
import hu.progmasters.gmistore.repository.EmailRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmailFromUserServiceTest {

    private EmailFromUserService emailFromUserService;

    @Mock
    private EmailRepository emailRepositoryMock;

    @Mock
    private EmailSenderService emailSenderServiceMock;

    @Spy
    List<EmailTableDto> emailTableDTOMock = new ArrayList<>();

    private final Pageable pageable = PageRequest.of(0, 10);

    @BeforeEach
    public void setup() {
        emailTableDTOMock = new ArrayList<>();
        emailFromUserService = new EmailFromUserService(emailRepositoryMock, emailSenderServiceMock);
    }

    private final Supplier<EmailCreatingDto> emailCreatingDtoSupplier = () -> {
        EmailCreatingDto emailCreatingDto = new EmailCreatingDto();
        emailCreatingDto.setId(1L);
        emailCreatingDto.setEmail("teszt@gm.co");
        emailCreatingDto.setSubject("teszt");
        emailCreatingDto.setMessage("teszt szöveg");
        emailCreatingDto.setMessageCreateTime(LocalDateTime.now());
        emailCreatingDto.setActive(true);
        return emailCreatingDto;
    };

    private final Supplier<EmailFromUser> emailFromUserSupplier = () -> {
        EmailFromUser newEmail = new EmailFromUser();
        newEmail.setId(1L);
        newEmail.setEmail("teszt@gmail.co");
        newEmail.setSubject("teszt subject");
        newEmail.setMessage("teszt message");
        newEmail.setMessageCreateTime(LocalDateTime.now());
        newEmail.setActive(true);
        return newEmail;
    };

    private final Supplier<List<EmailFromUser>> allActiveEmailListSupplier = () -> {
        List<EmailFromUser> emailFromUserList = new ArrayList<>();
        EmailFromUser email1 = new EmailFromUser();
        email1.setId(1L);
        email1.setEmail("teszt@gmail.com");
        email1.setSubject("teszt");
        email1.setMessage("teszt üzenet");
        email1.setMessageCreateTime(LocalDateTime.now());
        email1.setActive(true);

        EmailFromUser email2 = new EmailFromUser();
        email2.setId(1L);
        email2.setEmail("teszt2@gmail.com");
        email2.setSubject("teszt2");
        email2.setMessage("teszt üzenet 2");
        email2.setMessageCreateTime(LocalDateTime.now());
        email2.setActive(true);
        emailFromUserList.add(email1);
        emailFromUserList.add(email2);
        return emailFromUserList;
    };

    private final Supplier<List<EmailFromUser>> oneActiveEmailInListSupplier = () -> {
        List<EmailFromUser> emailFromUserList = new ArrayList<>();
        EmailFromUser email1 = new EmailFromUser();
        email1.setId(1L);
        email1.setEmail("teszt3@gmail.com");
        email1.setSubject("teszt 3");
        email1.setMessage("teszt üzenet 3");
        email1.setMessageCreateTime(LocalDateTime.now());
        email1.setActive(true);

        EmailFromUser email2 = new EmailFromUser();
        email2.setId(1L);
        email2.setEmail("teszt4@gmail.com");
        email2.setSubject("teszt4");
        email2.setMessage("teszt üzenet 4");
        email2.setMessageCreateTime(LocalDateTime.now());
        email2.setActive(false);
        emailFromUserList.add(email1);
        emailFromUserList.add(email2);
        return emailFromUserList;
    };

    @Spy
    private final Page<EmailFromUser> emailFromUserPage = new PageImpl<>(allActiveEmailListSupplier.get(), pageable, 2);

    @Spy
    private final Page<EmailFromUser> emailFromUserPageWithOneActiveEmail = new PageImpl<>(oneActiveEmailInListSupplier.get(), pageable, 2);


    private final Supplier<ReplyEmailDto> replyEmailDtoSupplier = () -> {
        ReplyEmailDto replyEmailDto = new ReplyEmailDto();
        replyEmailDto.setId(1L);
        replyEmailDto.setEmail("teszt@gmail.co");
        replyEmailDto.setSubject("teszt subject");
        replyEmailDto.setMessage("teszt message");
        replyEmailDto.setActive(true);
        return replyEmailDto;
    };

    @Test
    public void saveEmailFromUserTest() {
        EmailCreatingDto emailDTO = emailCreatingDtoSupplier.get();

        when(emailRepositoryMock.save(any(EmailFromUser.class))).thenAnswer(returnsFirstArg());

        EmailFromUser savedEmail = emailFromUserService.saveEmailFromUser(emailDTO);

        assertTrue(savedEmail.isActive());
        assertEquals("teszt@gm.co", savedEmail.getEmail());
        assertEquals("teszt", savedEmail.getSubject());
        assertEquals("teszt szöveg", savedEmail.getMessage());
    }

    @Test
    public void saveEmailAndAfterDeleteTest_OptionalEmailIsInactive_andDeleteTrue() {
        EmailCreatingDto emailDTO = emailCreatingDtoSupplier.get();
        EmailFromUser emailFromUser = emailFromUserSupplier.get();

        when(emailRepositoryMock.save(any(EmailFromUser.class))).thenAnswer(returnsFirstArg());
        when(emailRepositoryMock.findById(1L)).thenReturn(Optional.of(emailFromUser));

        emailFromUserService.saveEmailFromUser(emailDTO);

        boolean deletedEmail = emailFromUserService.deleteEmail(1L);
        Optional<EmailFromUser> emailFromUser1 = emailRepositoryMock.findById(1L);
        boolean isActiveAfterDelete = true;

        if (emailFromUser1.isPresent()) {
            isActiveAfterDelete = emailFromUser1.get().isActive();
        }
        assertFalse(isActiveAfterDelete);
        assertTrue(deletedEmail);
    }

    @Test
    public void getAllActiveEmailTest_resultNotNull() {
        EmailCreatingDto emailDTO = emailCreatingDtoSupplier.get();

        when(emailRepositoryMock.save(any(EmailFromUser.class))).thenAnswer(returnsFirstArg());
        when(emailRepositoryMock.findAllActiveEmail(any(Pageable.class))).thenReturn(emailFromUserPageWithOneActiveEmail);

        emailFromUserService.saveEmailFromUser(emailDTO);
        PagedActiveEmailList oneActiveIncomeEmails = emailFromUserService.getAllActiveIncomeEmails(1, 10);

        assertNotNull(oneActiveIncomeEmails);
    }

    @Test
    public void getAllActiveEmailTest() {
        EmailCreatingDto emailDTO = emailCreatingDtoSupplier.get();

        when(emailRepositoryMock.save(any(EmailFromUser.class))).thenAnswer(returnsFirstArg());
        when(emailRepositoryMock.findAllActiveEmail(any(Pageable.class))).thenReturn(emailFromUserPage);

        emailFromUserService.saveEmailFromUser(emailDTO);
        PagedActiveEmailList allActiveIncomeEmails = emailFromUserService.getAllActiveIncomeEmails(1, 10);

        assertTrue(allActiveIncomeEmails.getEmails().get(0).isActive());
        assertTrue(allActiveIncomeEmails.getEmails().get(1).isActive());
    }

    @Test
    public void getAllActiveEmailTest_resultOne() {
        EmailCreatingDto emailDTO = emailCreatingDtoSupplier.get();

        when(emailRepositoryMock.save(any(EmailFromUser.class))).thenAnswer(returnsFirstArg());
        when(emailRepositoryMock.findAllActiveEmail(any(Pageable.class))).thenReturn(emailFromUserPageWithOneActiveEmail);

        emailFromUserService.saveEmailFromUser(emailDTO);
        PagedActiveEmailList oneActiveIncomeEmails = emailFromUserService.getAllActiveIncomeEmails(1, 10);

        assertEquals("teszt3@gmail.com", oneActiveIncomeEmails.getEmails().get(0).getEmail());
        assertEquals("teszt4@gmail.com", oneActiveIncomeEmails.getEmails().get(1).getEmail());
        assertTrue(oneActiveIncomeEmails.getEmails().get(0).isActive());
        assertFalse(oneActiveIncomeEmails.getEmails().get(1).isActive());
    }


    @Test
    public void replyEmailToUserTest_resultTrue() {
        EmailCreatingDto emailDTO = emailCreatingDtoSupplier.get();
        EmailFromUser emailFromUser = emailFromUserSupplier.get();
        ReplyEmailDto replyEmailDto = replyEmailDtoSupplier.get();

        when(emailRepositoryMock.save(any(EmailFromUser.class))).thenAnswer(returnsFirstArg());
        when(emailRepositoryMock.findById(1L)).thenReturn(Optional.of(emailFromUser));

        emailFromUserService.saveEmailFromUser(emailDTO);

        boolean sendReplyEmail = emailFromUserService.sendReplyEmail(replyEmailDto);
        assertTrue(sendReplyEmail);
    }

    @Test
    public void replyEmailToUserButEmailIsNotExist() {
        EmailCreatingDto emailDTO = emailCreatingDtoSupplier.get();
        ReplyEmailDto replyEmailDto = replyEmailDtoSupplier.get();

        when(emailRepositoryMock.save(any(EmailFromUser.class))).thenAnswer(returnsFirstArg());
        when(emailRepositoryMock.findById(1L)).thenReturn(Optional.empty());


        emailFromUserService.saveEmailFromUser(emailDTO);

        boolean sendReplyEmail = emailFromUserService.sendReplyEmail(replyEmailDto);
        assertFalse(sendReplyEmail);
    }

}
