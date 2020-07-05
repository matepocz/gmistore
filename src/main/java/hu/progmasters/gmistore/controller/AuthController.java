package hu.progmasters.gmistore.controller;

import hu.progmasters.gmistore.dto.RegisterRequest;
import hu.progmasters.gmistore.dto.LoginRequest;
import hu.progmasters.gmistore.model.User;
import hu.progmasters.gmistore.service.AuthService;
import hu.progmasters.gmistore.validator.RegisterRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class AuthController {

    private final AuthService authService;
    private final RegisterRequestValidator registerRequestValidator;

    @Autowired
    public AuthController(AuthService authService, RegisterRequestValidator registerRequestValidator) {
        this.authService = authService;
        this.registerRequestValidator = registerRequestValidator;
    }

    @InitBinder("registerRequest")
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(registerRequestValidator);
    }

    @PostMapping("/register")
    public ResponseEntity registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        User user = authService.registerUser(registerRequest);
        return user != null ?
                new ResponseEntity<>(HttpStatus.CREATED) :
                new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/login")
    public void login(@RequestBody LoginRequest loginRequest) {
        authService.login(loginRequest);
    }
}
