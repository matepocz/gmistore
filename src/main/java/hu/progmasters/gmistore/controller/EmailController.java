package hu.progmasters.gmistore.controller;

import hu.progmasters.gmistore.dto.EmailCreating;
import hu.progmasters.gmistore.service.EmailFromUserService;
import hu.progmasters.gmistore.validator.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/contact-us")
public class EmailController {

    private EmailFromUserService emailFromUserService;
    private EmailValidator emailValidator;

    @Autowired
    public EmailController(EmailFromUserService emailFromUserService, EmailValidator emailValidator) {
        this.emailFromUserService = emailFromUserService;
        this.emailValidator = emailValidator;
    }

    @InitBinder(value = "emailCreating")
    public void init(WebDataBinder binder){
        binder.addValidators(emailValidator);
    }

    @PostMapping
    public ResponseEntity<Void> createEmail(@Valid @RequestBody EmailCreating emailCreating) {
        emailFromUserService.saveEmailFromUser(emailCreating);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
