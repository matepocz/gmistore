package hu.progmasters.gmistore.controller;

import hu.progmasters.gmistore.dto.LoginRequest;
import hu.progmasters.gmistore.dto.RegisterRequest;
import hu.progmasters.gmistore.model.User;
import hu.progmasters.gmistore.response.AuthenticationResponse;
import hu.progmasters.gmistore.response.ConfirmAccountResponse;
import hu.progmasters.gmistore.service.AuthService;
import hu.progmasters.gmistore.validator.RegisterRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
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
    public ResponseEntity<Void> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        User user = authService.registerUser(registerRequest);
        return user != null ?
                new ResponseEntity<>(HttpStatus.CREATED) :
                new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/confirm-account/{token}")
    public ResponseEntity<ConfirmAccountResponse> confirmAccount(@PathVariable("token") String confirmationToken) {
        ConfirmAccountResponse response = authService.confirmAccount(confirmationToken);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest loginRequest) {
        AuthenticationResponse response = new AuthenticationResponse(
                authService.login(loginRequest),
                loginRequest.getUsername()
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/check-username-taken/{username}")
    public ResponseEntity<Boolean> checkIfUsernameTaken(@PathVariable("username") String username) {
        boolean result = authService.checkIfUsernameExists(username);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @GetMapping("/check-email-taken/{email}")
    public ResponseEntity<Boolean> checkIfEmailTaken(@PathVariable("email") String email) {
        boolean result = authService.checkIfEmailExists(email);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }
}
