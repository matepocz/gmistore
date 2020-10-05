package hu.progmasters.gmistore.service;

import hu.progmasters.gmistore.dto.messages.NewMessageRequest;
import hu.progmasters.gmistore.model.Message;
import hu.progmasters.gmistore.model.User;
import hu.progmasters.gmistore.repository.MessageRepository;
import hu.progmasters.gmistore.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {

    private MessageService messageService;

    @Mock
    private MessageRepository messageRepositoryMock;

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private Principal principal;

    private final Supplier<NewMessageRequest> messageRequestSupplier = () -> {
        NewMessageRequest messageRequest = new NewMessageRequest();
        messageRequest.setReceiver("receiver");
        messageRequest.setSubject("Test subject in request");
        messageRequest.setContent("Test content in request");
        return messageRequest;
    };

    private final Supplier<Message> messageSupplier = () -> {
        User sender = new User();
        sender.setUsername("sender");

        User receiver = new User();
        receiver.setUsername("receiver");

        Message message = new Message();
        message.setId(1L);
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setSubject("Test subject");
        message.setContent("Test content");
        message.setDisplayForReceiver(true);
        message.setDisplayForSender(true);
        message.setRead(false);
        message.setTimestamp(LocalDateTime.now());
        return message;
    };

    @BeforeEach
    public void setup() {
        messageService = new MessageService(messageRepositoryMock, userRepositoryMock);
    }

    @Test
    public void testMarkMessageRead() {
        Message message = messageSupplier.get();

        when(messageRepositoryMock.findById(1L)).thenReturn(Optional.of(message));

        boolean result = messageService.markMessageRead(1L);
        assertTrue(result);
        assertTrue(message.isRead());

        verify(messageRepositoryMock, times(1)).findById(1L);
    }

}
