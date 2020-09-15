package hu.progmasters.gmistore.controller;

import com.sun.tools.jconsole.JConsoleContext;
import hu.progmasters.gmistore.dto.AuthenticationResponse;
import hu.progmasters.gmistore.dto.LoginRequest;
import hu.progmasters.gmistore.dto.PasswordTokenDto;
import hu.progmasters.gmistore.dto.RegisterRequest;
import hu.progmasters.gmistore.model.User;
import hu.progmasters.gmistore.response.ConfirmAccountResponse;
import hu.progmasters.gmistore.response.GenericResponse;
import hu.progmasters.gmistore.service.AuthService;
import hu.progmasters.gmistore.service.ResetPasswordService;
import hu.progmasters.gmistore.service.SecurityService;
import hu.progmasters.gmistore.service.UserService;
import hu.progmasters.gmistore.validator.RegisterRequestValidator;
import hu.progmasters.gmistore.validator.ResetPasswordValidator;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AuthController.class);


    private final AuthService authService;
    private final RegisterRequestValidator registerRequestValidator;
    private final SecurityService securityService;
    private final ResetPasswordService resetPasswordService;
    private final UserService userService;
    private final ResetPasswordValidator resetPasswordValidator;

    @Autowired
    public AuthController(AuthService authService,
                          RegisterRequestValidator registerRequestValidator,
                          SecurityService securityService,
                          ResetPasswordService resetPasswordService,
                          UserService userService,
                          ResetPasswordValidator resetPasswordValidator) {
        this.authService = authService;
        this.registerRequestValidator = registerRequestValidator;
        this.securityService = securityService;
        this.resetPasswordService = resetPasswordService;
        this.userService = userService;
        this.resetPasswordValidator = resetPasswordValidator;
    }

    @InitBinder("registerRequest")
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(registerRequestValidator);
    }

    @InitBinder("passwordTokenDto")
    protected void initResetPasswordBinder(WebDataBinder binder) {
        binder.addValidators(resetPasswordValidator);
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
        AuthenticationResponse loginResponse = authService.login(loginRequest);
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    @GetMapping("/check-username-taken/{username}")
    public ResponseEntity<Boolean> checkIfUsernameTaken(@PathVariable("username") String username) {
        boolean result = authService.checkIfUsernameExists(username);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/check-email-taken/{email}")
    public ResponseEntity<Boolean> checkIfEmailTaken(@PathVariable("email") String email) {
        boolean result = authService.checkIfEmailExists(email);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/user-roles")
    public ResponseEntity<List<String>> currentUserRoles() {
        Collection<? extends GrantedAuthority> authorities =
                SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        List<String> roles = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        return !roles.contains("ROLE_ANONYMOUS") ?
                new ResponseEntity<>(roles, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<User> findUserAndSendMailToResetPassword(HttpServletRequest request, @RequestParam("email") String userEmail) {
        resetPasswordService.findUserAndSendMailToResetPassword(request, userEmail);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/savePassword")
    public ResponseEntity<User> savePassword(@Valid @RequestBody PasswordTokenDto passwordDto) {
        System.out.println(passwordDto.getToken());
        String result = securityService.validatePasswordResetToken(passwordDto.getToken());
        if (result != null) {
            LOGGER.info("Not valid token:" + passwordDto.getToken());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<User> user = userService.getUserByPasswordResetToken(passwordDto.getToken());
        if (user.isPresent()) {
            LOGGER.info(user.get().getUsername() + " found");
            userService.changeUserPassword(user.get(), passwordDto.getPassword());
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } else {
            LOGGER.info("User not found by token");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
}
