package hu.progmasters.gmistore.controller;

import hu.progmasters.gmistore.dto.EmailCreatingDto;
import hu.progmasters.gmistore.service.EmailFromUserService;
import hu.progmasters.gmistore.validator.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/contact-messages")
public class EmailController {

    private EmailFromUserService emailFromUserService;
    private EmailValidator emailValidator;

    @Autowired
    public EmailController(EmailFromUserService emailFromUserService, EmailValidator emailValidator) {
        this.emailFromUserService = emailFromUserService;
        this.emailValidator = emailValidator;
    }

    @InitBinder(value = "emailCreating")
    public void init(WebDataBinder binder) {
        binder.addValidators(emailValidator);
    }

    @PostMapping
    public ResponseEntity<Void> createEmail(@Valid @RequestBody EmailCreatingDto emailCreatingDto) {
        emailFromUserService.saveEmailFromUser(emailCreatingDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/income-emails")
    public ResponseEntity<List<EmailCreatingDto>> getIncomeEmails() {
        List<EmailCreatingDto> emails = emailFromUserService.getAllIncomeEmails();
        return new ResponseEntity<>(emails, HttpStatus.OK);
    }
}
