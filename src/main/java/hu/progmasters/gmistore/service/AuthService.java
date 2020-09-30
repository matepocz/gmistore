package hu.progmasters.gmistore.service;

import hu.progmasters.gmistore.dto.AuthenticationResponse;
import hu.progmasters.gmistore.dto.LoginRequest;
import hu.progmasters.gmistore.dto.RegisterRequest;
import hu.progmasters.gmistore.enums.Role;
import hu.progmasters.gmistore.model.ConfirmationToken;
import hu.progmasters.gmistore.model.User;
import hu.progmasters.gmistore.repository.ConfirmationTokenRepository;
import hu.progmasters.gmistore.repository.UserRepository;
import hu.progmasters.gmistore.response.ConfirmAccountResponse;
import hu.progmasters.gmistore.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AuthService {

    @Value("${client-url}")
    private String clientUrl;

    private final UserRepository userRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final EmailSenderService emailSenderService;
    private final SessionRegistry sessionRegistry;


    @Autowired
    public AuthService(UserRepository userRepository, ConfirmationTokenRepository confirmationTokenRepository,
                       PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager,
                       SessionRegistry sessionRegistry,
                       JwtProvider jwtProvider, EmailSenderService emailSenderService) {
        this.userRepository = userRepository;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.emailSenderService = emailSenderService;
        this.sessionRegistry = sessionRegistry;
    }

    /**
     * Registers a user to the database, send a confirmation email to the user
     *
     * @param registerRequest A DTO containing the details
     * @return A registered User
     */
    public User registerUser(RegisterRequest registerRequest) {
        User user = new User();
        mapUserDetailsToUser(registerRequest, user);
        User savedUser = userRepository.save(user);
        sendConfirmationEmailToUser(user);
        return savedUser;
    }

    private void mapUserDetailsToUser(RegisterRequest registerRequest, User user) {
        user.setUsername(registerRequest.getUsername());
        user.setLastName(registerRequest.getLastName());
        user.setFirstName(registerRequest.getFirstName());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setRegistered(LocalDateTime.now());
        List<Role> roles = user.getRoles();
        roles.add(Role.ROLE_USER);
        if (registerRequest.getSeller()) {
            roles.add(Role.ROLE_SELLER);
        }
        user.setActive(false);
    }

    private void sendConfirmationEmailToUser(User user) {
        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        confirmationTokenRepository.save(confirmationToken);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Regisztráció véglegesítése");
        mailMessage.setFrom("gmistarter@gmail.com");
        mailMessage.setText("Kedves " + user.getFirstName() + ", \n" +
                "A regisztrációd megerősítéséhez, kérjük kattints ide : "
                + clientUrl + "/confirm-account?token=" + confirmationToken.getToken() +
                "\n" +
                "\n" +
                "Amennyiben nem te regisztráltál, kérjük hagyd figyelmen kívűl ezt az emailt!" +
                "\n" +
                "GMI Store team");
        emailSenderService.sendEmail(mailMessage);
    }

    /**
     * Set the user's account to active
     *
     * @param confirmationToken The token that has been sent out in the confirmation email
     * @return A ConfirmAccountResponse containing the results
     */
    public ConfirmAccountResponse confirmAccount(String confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository.findByToken(confirmationToken);
        ConfirmAccountResponse response = new ConfirmAccountResponse();
        if (token != null) {
            Optional<User> userByEmail = userRepository.findUserByEmail(token.getUser().getEmail());
            if (userByEmail.isPresent() && !userByEmail.get().isActive()) {
                User user = userByEmail.get();
                user.setActive(true);
                userRepository.save(user);
                response.setResult("Success");
                response.setEmail(user.getEmail());
                return response;
            }
        }
        response.setResult("Failed");
        return response;
    }

    /**
     * Attempts to log in the user
     *
     * @param loginRequest A DTO containing username and password
     * @return A DTO, containing the username, the JWT token
     */
    public AuthenticationResponse login(LoginRequest loginRequest) {
        String username = new String(Base64.getDecoder().decode(loginRequest.getUsername()));
        String password = new String(Base64.getDecoder().decode(loginRequest.getPassword()));
        Optional<User> userByUsername = userRepository.findUserByUsername(username);
        if (userByUsername.isPresent()) {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
            SecurityContextHolder.getContext().setAuthentication(authenticate);
            AuthenticationResponse response = new AuthenticationResponse();
            response.setAuthenticationToken(jwtProvider.generateToken(authenticate));
            response.setUsername(loginRequest.getUsername());

            sessionRegistry.registerNewSession(response.getAuthenticationToken(), authenticate.getPrincipal());

            return response;
        }
        throw new UsernameNotFoundException("User not found!");
    }

    /**
     * Checks if the given username taken already
     *
     * @param username A username to check if it is already taken
     * @return a boolean, true if the username already taken, false otherwise
     */
    public boolean checkIfUsernameExists(String username) {
        Optional<User> userByUsername = userRepository.findUserByUsername(username);
        return userByUsername.isPresent();
    }

    /**
     * Checks if the given email address taken already
     *
     * @param email An email address to check if it is already taken
     * @return a boolean, true if the email address already taken, false otherwise
     */
    public boolean checkIfEmailExists(String email) {
        Optional<User> userByEmail = userRepository.findUserByEmail(email);
        return userByEmail.isPresent();
    }
}
