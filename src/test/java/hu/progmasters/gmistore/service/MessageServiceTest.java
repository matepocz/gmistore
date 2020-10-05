package hu.progmasters.gmistore.service;

import hu.progmasters.gmistore.dto.messages.MessagesResponse;
import hu.progmasters.gmistore.dto.messages.NewMessageRequest;
import hu.progmasters.gmistore.model.Message;
import hu.progmasters.gmistore.model.User;
import hu.progmasters.gmistore.repository.MessageRepository;
import hu.progmasters.gmistore.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {

    private MessageService messageService;

    @Mock
    private MessageRepository messageRepositoryMock;

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private Principal principalMock;

    @Mock
    private User sender;

    @Mock
    private User receiver;

    @Spy
    private List<Message> messageListMock = new ArrayList<>();

    private final String senderName = "sender";
    private final String receiverName = "receiver";

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
        messageListMock = new ArrayList<>();
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

    @Test
    public void testCreateMessageReturnsTrue() {
        NewMessageRequest messageRequest = messageRequestSupplier.get();

        when(userRepositoryMock.findUserByUsername(senderName)).thenReturn(Optional.of(sender));
        when(userRepositoryMock.findUserByUsername(receiverName)).thenReturn(Optional.of(receiver));
        when(principalMock.getName()).thenReturn(senderName);
        when(sender.getOutgoingMessages()).thenReturn(new ArrayList<>());
        when(receiver.getIncomingMessages()).thenReturn(new ArrayList<>());

        boolean actualResult = messageService.createMessage(messageRequest, principalMock);

        assertTrue(actualResult);
        verify(userRepositoryMock, times(1)).findUserByUsername(senderName);
        verify(userRepositoryMock, times(1)).findUserByUsername(receiverName);
        verify(messageRepositoryMock, times(1)).save(any());
    }

    @Test
    public void testCreateMessageSenderNotExists() {
        NewMessageRequest messageRequest = messageRequestSupplier.get();

        when(userRepositoryMock.findUserByUsername(senderName)).thenReturn(Optional.empty());
        when(userRepositoryMock.findUserByUsername(receiverName)).thenReturn(Optional.of(receiver));
        when(principalMock.getName()).thenReturn(senderName);

        boolean actualResult = messageService.createMessage(messageRequest, principalMock);

        assertFalse(actualResult);
        verify(userRepositoryMock, times(1)).findUserByUsername(senderName);
        verify(userRepositoryMock, times(1)).findUserByUsername(receiverName);
        verifyNoInteractions(messageRepositoryMock);
    }

    @Test
    public void testCreateMessageReceiverNotExists() {
        NewMessageRequest messageRequest = messageRequestSupplier.get();

        when(userRepositoryMock.findUserByUsername(senderName)).thenReturn(Optional.of(sender));
        when(userRepositoryMock.findUserByUsername(receiverName)).thenReturn(Optional.empty());
        when(principalMock.getName()).thenReturn(senderName);

        boolean actualResult = messageService.createMessage(messageRequest, principalMock);

        assertFalse(actualResult);
        verify(userRepositoryMock, times(1)).findUserByUsername(senderName);
        verify(userRepositoryMock, times(1)).findUserByUsername(receiverName);
        verifyNoInteractions(messageRepositoryMock);
    }

    @Test
    public void testDeleteMessageWhereUserIsSender() {
        Message message = messageSupplier.get();

        when(messageRepositoryMock.findById(1L)).thenReturn(Optional.of(message));
        when(userRepositoryMock.findUserByUsername(senderName)).thenReturn(Optional.of(sender));
        when(principalMock.getName()).thenReturn(senderName);

        boolean result = messageService.deleteMessageForCurrentUser(1L, principalMock);

        assertTrue(result);
        verify(userRepositoryMock, times(1)).findUserByUsername(senderName);
        verify(messageRepositoryMock, times(1)).findById(1L);
    }

    @Test
    public void testDeleteMessageWereUserIsReceiver() {
        Message message = messageSupplier.get();

        when(messageRepositoryMock.findById(1L)).thenReturn(Optional.of(message));
        when(userRepositoryMock.findUserByUsername(receiverName)).thenReturn(Optional.of(receiver));
        when(principalMock.getName()).thenReturn(receiverName);

        boolean result = messageService.deleteMessageForCurrentUser(1L, principalMock);

        assertTrue(result);
        verify(userRepositoryMock, times(1)).findUserByUsername(receiverName);
        verify(messageRepositoryMock, times(1)).findById(1L);
    }

    @Test
    public void testGetMessagesForCurrentUserReturnsTwoOutgoingMessages() {
        Message message0 = messageSupplier.get();
        Message message1 = messageSupplier.get();
        message1.setId(2L);
        messageListMock.add(message0);
        messageListMock.add(message1);

        when(userRepositoryMock.findUserByUsername(senderName)).thenReturn(Optional.of(sender));
        when(sender.getOutgoingMessages()).thenReturn(messageListMock);
        when(principalMock.getName()).thenReturn(senderName);

        MessagesResponse messagesForCurrentUser = messageService.getMessagesForCurrentUser(principalMock);

        assertNotNull(messagesForCurrentUser);
        assertEquals(2, messagesForCurrentUser.getOutgoing().size());

        verify(userRepositoryMock, times(1)).findUserByUsername(senderName);
        verify(principalMock, times(1)).getName();
    }

    @Test
    public void testGetMessagesForCurrentUserReturnsTwoIncomingMessages() {
        Message message0 = messageSupplier.get();
        Message message1 = messageSupplier.get();
        message1.setId(2L);
        messageListMock.add(message0);
        messageListMock.add(message1);

        when(userRepositoryMock.findUserByUsername(receiverName)).thenReturn(Optional.of(receiver));
        when(receiver.getIncomingMessages()).thenReturn(messageListMock);
        when(principalMock.getName()).thenReturn(receiverName);

        MessagesResponse messagesForCurrentUser = messageService.getMessagesForCurrentUser(principalMock);

        assertNotNull(messagesForCurrentUser);
        assertEquals(2, messagesForCurrentUser.getIncoming().size());

        verify(userRepositoryMock, times(1)).findUserByUsername(receiverName);
        verify(principalMock, times(1)).getName();
    }

    @Test
    public void testGetCountOfUnreadMailsForCurrentUserReturnsTwo() {
        Message message0 = messageSupplier.get();
        Message message1 = messageSupplier.get();
        message1.setId(2L);
        messageListMock.add(message0);
        messageListMock.add(message1);

        when(userRepositoryMock.findUserByUsername(receiverName)).thenReturn(Optional.of(receiver));
        when(receiver.getIncomingMessages()).thenReturn(messageListMock);
        when(principalMock.getName()).thenReturn(receiverName);

        int countOfUnreadMailsForCurrentUser = messageService.getCountOfUnreadMailsForCurrentUser(principalMock);

        assertEquals(2, countOfUnreadMailsForCurrentUser);
        verify(userRepositoryMock, times(1)).findUserByUsername(receiverName);
        verify(principalMock, times(1)).getName();
    }

    @Test
    public void testGetCountOfUnreadMailsForCurrentUserReturnsOne() {
        Message message0 = messageSupplier.get();
        Message message1 = messageSupplier.get();
        message1.setId(2L);
        message1.setRead(true);
        messageListMock.add(message0);
        messageListMock.add(message1);

        when(userRepositoryMock.findUserByUsername(receiverName)).thenReturn(Optional.of(receiver));
        when(receiver.getIncomingMessages()).thenReturn(messageListMock);
        when(principalMock.getName()).thenReturn(receiverName);

        int countOfUnreadMailsForCurrentUser = messageService.getCountOfUnreadMailsForCurrentUser(principalMock);
        assertEquals(1, countOfUnreadMailsForCurrentUser);
        verify(userRepositoryMock, times(1)).findUserByUsername(receiverName);
        verify(principalMock, times(1)).getName();
    }
}
