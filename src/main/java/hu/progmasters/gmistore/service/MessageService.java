package hu.progmasters.gmistore.service;

import hu.progmasters.gmistore.dto.messages.MessageDetails;
import hu.progmasters.gmistore.dto.messages.MessagesResponse;
import hu.progmasters.gmistore.dto.messages.NewMessageRequest;
import hu.progmasters.gmistore.model.Message;
import hu.progmasters.gmistore.model.User;
import hu.progmasters.gmistore.repository.MessageRepository;
import hu.progmasters.gmistore.repository.UserRepository;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class MessageService {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MessageService.class);

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public MessageService(MessageRepository messageRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    /**
     * Attempts to create a new message
     *
     * @param newMessageRequest NewMessageRequest DTO that holds the incoming data
     * @param principal         The currently logged in user
     * @return A boolean, true if successful, false otherwise
     */
    public boolean createMessage(NewMessageRequest newMessageRequest, Principal principal) {
        Optional<User> senderByName = userRepository.findUserByUsername(principal.getName());
        Optional<User> receiverByName = userRepository.findUserByUsername(newMessageRequest.getReceiver());
        if (senderByName.isPresent() && receiverByName.isPresent()) {
            User sender = senderByName.get();
            User receiver = receiverByName.get();
            Message message = createNewMessage(newMessageRequest, sender, receiver);
            sender.getOutgoingMessages().add(message);
            receiver.getIncomingMessages().add(message);
            LOGGER.debug("Message created! sender: {}, receiver: {}", sender.getUsername(), receiver.getUsername());
            return true;
        }
        LOGGER.debug("Message creation failed! Either the sender or the receiver does not exist!");
        return false;
    }

    private Message createNewMessage(NewMessageRequest newMessageRequest, User sender, User receiver) {
        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setSubject(newMessageRequest.getSubject());
        message.setContent(newMessageRequest.getContent());
        message.setDisplayForSender(true);
        message.setDisplayForReceiver(true);
        message.setRead(false);
        message.setTimestamp(LocalDateTime.now());
        return messageRepository.save(message);
    }

    /**
     * Fetch all the messages belongs to the currently signed in user
     *
     * @param principal The currently logged in user
     * @return A MessagesResponse DTO containing a list of incoming, another list of outgoing messages
     */
    public MessagesResponse getMessagesForCurrentUser(Principal principal) {
        Optional<User> userByUsername = userRepository.findUserByUsername(principal.getName());
        if (userByUsername.isPresent()) {
            MessagesResponse messages = new MessagesResponse();
            User currentUser = userByUsername.get();
            messages.setIncoming(filterAndMapIncomingMessages(currentUser));
            messages.setOutgoing(filterAndMapOutgoingMessages(currentUser));
            return messages;
        }
        throw new UsernameNotFoundException("Could not find user with username: " + principal.getName());
    }

    private List<MessageDetails> filterAndMapOutgoingMessages(User currentUser) {
        return currentUser.getOutgoingMessages()
                .stream()
                .filter(Message::isDisplayForSender)
                .map(MessageDetails::new)
                .collect(Collectors.toList());
    }

    private List<MessageDetails> filterAndMapIncomingMessages(User currentUser) {
        return currentUser.getIncomingMessages()
                .stream()
                .filter(Message::isDisplayForReceiver)
                .map(MessageDetails::new)
                .collect(Collectors.toList());
    }

    /**
     * Set the given message's display flag to false for the currently logged in user,
     * in case if it has been deleted for both the sender and the receiver,
     * the message will be removed from the database.
     *
     * @param id        The given Message object's id
     * @param principal The currently logged in user's details
     * @return A boolean, true if successful, false otherwise
     */
    public boolean deleteMessageForCurrentUser(Long id, Principal principal) {
        Optional<User> userByUsername = userRepository.findUserByUsername(principal.getName());
        Optional<Message> messageById = messageRepository.findById(id);
        if (userByUsername.isPresent() && messageById.isPresent()) {
            User user = userByUsername.get();
            Message message = messageById.get();
            if (message.getSender().equals(user)) {
                message.setDisplayForSender(false);
                user.getOutgoingMessages()
                        .removeIf(currentMessage -> currentMessage.getId().equals(id));
            }
            if (message.getReceiver().equals(user)) {
                message.setDisplayForReceiver(false);
                user.getIncomingMessages()
                        .removeIf(currentMessage -> currentMessage.getId().equals(id));
            }
            isMessageDeletedForEveryone(message);
            return true;
        }
        LOGGER.debug("Message delete failed! Message id: {}", id);
        return false;
    }

    private void isMessageDeletedForEveryone(Message message) {
        if (!message.isDisplayForReceiver() && !message.isDisplayForSender()) {
            LOGGER.debug("Message deleted from the database, id: {}", message.getId());
            messageRepository.deleteById(message.getId());
        }
    }

    /**
     * Get the count of unread incoming messages for the currently logged in user
     *
     * @param principal The currently logged in user's details
     * @return The count of unread messages (int)
     */
    public int getCountOfUnreadMailsForCurrentUser(Principal principal) {
        Optional<User> userByUsername = userRepository.findUserByUsername(principal.getName());
        return userByUsername.map(user ->
                (int) user.getIncomingMessages()
                        .stream()
                        .filter(incomingMessage -> !incomingMessage.isRead())
                        .count())
                .orElse(0);
    }

    /**
     * Mark the given message status to read by the user
     *
     * @param id The given message's unique ID
     * @return A boolean
     */
    public boolean markMessageRead(Long id) {
        Optional<Message> messageById = messageRepository.findById(id);
        messageById.ifPresent(message -> message.setRead(true));
        return true;
    }
}
