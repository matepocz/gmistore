package hu.progmasters.gmistore.service;

import hu.progmasters.gmistore.dto.RegisterRequest;
import hu.progmasters.gmistore.dto.LoginRequest;
import hu.progmasters.gmistore.model.Address;
import hu.progmasters.gmistore.model.User;
import hu.progmasters.gmistore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(RegisterRequest registerRequest) {
        User user = new User();
        Address address = mapAddressDetailsToAddress(registerRequest, user);
        mapUserDetailsToUser(registerRequest, user, address);
        return userRepository.save(user);
    }

    private void mapUserDetailsToUser(RegisterRequest registerRequest, User user, Address address) {
        user.setUsername(registerRequest.getUsername());
        user.setLastName(registerRequest.getLastName());
        user.setFirstName(registerRequest.getFirstName());
        user.setAddress(address);
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setPhoneNumber(registerRequest.getPhoneNumber());
        user.setRegistered(LocalDateTime.now());
        user.setActive(false);
    }

    private Address mapAddressDetailsToAddress(RegisterRequest registerRequest, User user) {
        Address address = new Address();
        address.setUser(user);
        address.setCity(registerRequest.getCity());
        address.setStreet(registerRequest.getStreet());
        address.setNumber(registerRequest.getNumber());
        address.setPostcode(registerRequest.getPostcode());
        return address;
    }

    public void login(LoginRequest loginRequest) {

    }
}
