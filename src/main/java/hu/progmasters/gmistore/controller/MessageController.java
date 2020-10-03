package hu.progmasters.gmistore.controller;

import hu.progmasters.gmistore.dto.messages.MessagesResponse;
import hu.progmasters.gmistore.dto.messages.NewMessageRequest;
import hu.progmasters.gmistore.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public ResponseEntity<Boolean> createMessage(@RequestBody NewMessageRequest newMessageRequest,
                                                 Principal principal) {
        boolean result = messageService.createMessage(newMessageRequest, principal);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<MessagesResponse> getMessages(Principal principal) {
        MessagesResponse messages = messageService.getMessagesForCurrentUser(principal);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteMessage(@PathVariable("id") Long id, Principal principal) {
        boolean result = messageService.deleteMessageForCurrentUser(id, principal);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
