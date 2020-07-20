package hu.progmasters.gmistore.service;

import hu.progmasters.gmistore.dto.LoginRequest;
import hu.progmasters.gmistore.dto.RegisterRequest;
import hu.progmasters.gmistore.model.ConfirmationToken;
import hu.progmasters.gmistore.model.User;
import hu.progmasters.gmistore.repository.ConfirmationTokenRepository;
import hu.progmasters.gmistore.repository.UserRepository;
import hu.progmasters.gmistore.response.ConfirmAccountResponse;
import hu.progmasters.gmistore.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final EmailSenderService emailSenderService;

    @Autowired
    public AuthService(UserRepository userRepository, ConfirmationTokenRepository confirmationTokenRepository,
                       PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager,
                       JwtProvider jwtProvider, EmailSenderService emailSenderService) {
        this.userRepository = userRepository;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.emailSenderService = emailSenderService;
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
        user.setPhoneNumber(registerRequest.getPhoneNumber());
        user.setRegistered(LocalDateTime.now());
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
                " a regisztrációd megerősítéséhez kérjük kattints ide : "
                +"http://localhost:4200/confirm-account?token="+confirmationToken.getToken() +
                "\n" +
                "Amennyiben nem te regisztráltál, kérjük hagyd figyelmen kívűl ezt az emailt!" +
                "\n" +
                "GMI Store team");
        emailSenderService.sendEmail(mailMessage);
    }

    /**
     * Set the user's account to active
     * @param confirmationToken The token that has been sent out in the confirmation email
     * @return A ConfirmAccountResponse containing the results
     */
    public ConfirmAccountResponse confirmAccount(String confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository.findByToken(confirmationToken);
        ConfirmAccountResponse response = new ConfirmAccountResponse();
        if(token != null) {
            Optional<User> userByEmail = userRepository.findUserByEmail(token.getUser().getEmail());
            if (userByEmail.isPresent() && !userByEmail.get().isActive()) {
                User user = userByEmail.get();
                user.setActive(true);
                userRepository.save(user);
                response.setResult("Success");
                response.setEmail(user.getEmail());
            }
        } else {
            response.setResult("Failed");
        }
        return response;
    }

    /**
     * Attempts to log in the user
     *
     * @param loginRequest A DTO containing username and password
     * @return A JWT token
     */
    public String login(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        return jwtProvider.generateToken(authenticate);
    }
}
