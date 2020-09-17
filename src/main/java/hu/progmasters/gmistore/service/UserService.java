package hu.progmasters.gmistore.service;

import hu.progmasters.gmistore.dto.*;
import hu.progmasters.gmistore.enums.Role;
import hu.progmasters.gmistore.model.PasswordResetToken;
import hu.progmasters.gmistore.model.User;
import hu.progmasters.gmistore.repository.PasswordTokenRepository;
import hu.progmasters.gmistore.repository.UserRepository;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final SessionRegistry sessionRegistry;
    private final PasswordTokenRepository passwordTokenRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(PasswordEncoder passwordEncoder,
                       UserRepository userRepository,
                       SessionRegistry sessionRegistry,
                       PasswordTokenRepository passwordTokenRepository) {
        this.userRepository = userRepository;
        this.sessionRegistry = sessionRegistry;
        this.passwordTokenRepository = passwordTokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto getUserData(String username) {
        System.out.println(sessionRegistry.getAllPrincipals());
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        UserDto userDto = new UserDto(
                user.getId(), user.getUsername(), user.getLastName(), user.getFirstName(), user.getShippingAddress(),
                user.getBillingAddress(), user.getEmail(), user.getPhoneNumber(), user.getRoles(), user.getRegistered(),
                user.isActive(), user.getOrderList());
        return userDto;
    }

    public List<UserListDetailDto> getUserList() {
        return userRepository.findAllUsersWithListDetails();

    }

    public User getUserByUsername(String username) {
        Optional<User> userByUsername = userRepository.findUserByUsername(username);
        return userByUsername.orElse(null);
    }

    public User getUserById(Long id) {
        System.out.println(sessionRegistry.getAllPrincipals());
        return userRepository.findUserById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User with " + id + " not found!"));
    }

    public User getUserByName(String username) {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with " + username + " not found!"));
    }

    public List<RolesFormDto> getRoles() {
        List<RolesFormDto> roles = new ArrayList<>();
        for (Role value : Role.values()) {
            roles.add(new RolesFormDto(value));
        }
        return roles;
    }

    public void updateUserById(UserEditableDetailsDto user) {
        System.out.println(user.getId());
        User userById = getUserById(user.getId());
        updateUser(user, userById);
    }

    public void updateUserByName(UserEditableDetailsDto user) {
        User userByUsername = getUserByUsername(user.getUsername());
        updateUser(user, userByUsername);
    }

    private void updateUser(UserEditableDetailsDto user, User userToEdit) {
        userToEdit.setBillingAddress(user.getBillingAddress());
        userToEdit.setFirstName(user.getFirstName());
        userToEdit.setLastName(user.getLastName());
        userToEdit.setPhoneNumber(user.getPhoneNumber());
        userToEdit.setUsername(user.getUsername());
        userToEdit.setShippingAddress(user.getShippingAddress());
        if (user.getRoles() != null) {
            userToEdit.setRoles(user.getRoles().stream().map(Role::valueOf).collect(Collectors.toList()));
        }
        userRepository.save(userToEdit);
    }

    public User findUserByEmail(String userEmail) {
        return userRepository.findUserByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User with " + userEmail + " not found!"));
    }

    public void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken myToken = new PasswordResetToken(token, user);
        passwordTokenRepository.save(myToken);
    }

    public Optional<User> getUserByPasswordResetToken(String token) {
        return Optional.ofNullable(passwordTokenRepository.findByToken(token).getUser());
    }

    public void changeUserPassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
//        PasswordResetToken byTokenByUser = passwordTokenRepository.findPasswordResetTokenByUser(user);
        passwordTokenRepository.deleteByUser(user);
        userRepository.save(user);
    }
}
