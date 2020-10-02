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
     * @param newMessageRequest NewMessageRequest DTO that holds the incoming data
     * @param principal The currently logged in user
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
}
