package hu.progmasters.gmistore.controller;

import hu.progmasters.gmistore.dto.messages.EmailCreatingDto;
import hu.progmasters.gmistore.dto.messages.PagedActiveEmailList;
import hu.progmasters.gmistore.dto.messages.ReplyEmailDto;
import hu.progmasters.gmistore.service.EmailFromUserService;
import hu.progmasters.gmistore.validator.EmailValidator;
import hu.progmasters.gmistore.validator.ReplyEmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/contact-messages")
public class EmailController {

    private EmailFromUserService emailFromUserService;
    private EmailValidator emailValidator;
    private ReplyEmailValidator replyEmailValidator;

    @Autowired
    public EmailController(EmailFromUserService emailFromUserService, EmailValidator emailValidator, ReplyEmailValidator replyEmailValidator) {
        this.emailFromUserService = emailFromUserService;
        this.emailValidator = emailValidator;
        this.replyEmailValidator = replyEmailValidator;
    }

    @InitBinder(value = "emailCreatingDto")
    public void init(WebDataBinder binder) {
        binder.addValidators(emailValidator);
    }
    @InitBinder(value = "replyEmailDto")
    public void initReplyEmail(WebDataBinder binder) {
        binder.addValidators(replyEmailValidator);
    }

    @PostMapping
    public ResponseEntity<Void> createEmail(@Valid @RequestBody EmailCreatingDto emailCreatingDto) {
        emailFromUserService.saveEmailFromUser(emailCreatingDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/income-emails")
    public ResponseEntity<PagedActiveEmailList> getAllActiveIncomeEmails(
            @RequestParam(value = "size", defaultValue = "20") String size,
            @RequestParam(value = "page", defaultValue = "0") String page
            ) {
        PagedActiveEmailList emails = emailFromUserService.getAllActiveIncomeEmails(Integer.parseInt(size),Integer.parseInt(page));
        return new ResponseEntity<>(emails, HttpStatus.OK);
    }

    @PostMapping("/reply")
    public ResponseEntity<Boolean> getCurrentEmail(@RequestBody ReplyEmailDto replyEmailDto) {
        boolean result = emailFromUserService.sendReplyEmail(replyEmailDto);
        return result ?
                new ResponseEntity<>(result, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteEmail(@PathVariable Long id) {
        boolean result = emailFromUserService.deleteEmail(id);
        return result ?
                new ResponseEntity<>(true, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
