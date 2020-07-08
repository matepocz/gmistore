package hu.progmasters.gmistore.service;

import hu.progmasters.gmistore.dto.RegisterRequest;
import hu.progmasters.gmistore.dto.LoginRequest;
import hu.progmasters.gmistore.model.Address;
import hu.progmasters.gmistore.model.User;
import hu.progmasters.gmistore.repository.UserRepository;
import hu.progmasters.gmistore.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    public User registerUser(RegisterRequest registerRequest) {
        User user = new User();
        mapUserDetailsToUser(registerRequest, user);
        return userRepository.save(user);
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

    public String login(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        return jwtProvider.generateToken(authenticate);
    }
}
