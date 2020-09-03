package hu.progmasters.gmistore.service;

import hu.progmasters.gmistore.dto.UserDto;
import hu.progmasters.gmistore.dto.UserRegistrationDTO;
import hu.progmasters.gmistore.enums.Role;
import hu.progmasters.gmistore.model.User;
import hu.progmasters.gmistore.repository.UserRepository;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ProductService.class);
    private final SessionRegistry sessionRegistry;
    private final UserRepository userRepository;

    public AdminService(SessionRegistry sessionRegistry, UserRepository userRepository) {
        this.sessionRegistry = sessionRegistry;
        this.userRepository = userRepository;
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalIdentifierException("No such ID exits."));
        return new UserDto(user);
    }

    public List<String> getUsersFromSessionRegistry() {
        List<String> sessions = sessionRegistry.getAllPrincipals().stream()
                .filter(u -> !sessionRegistry.getAllSessions(u, false).isEmpty())
                .map(Object::toString)
                .collect(Collectors.toList());
        LOGGER.info(String.valueOf(sessions.size()));
        return sessions;
    }

    public Map<String, Integer> getSortedUserRegistrationByDate(Role userRole) {
        Map<String, Integer> userDates = new HashMap<>();
        List<User> sellerRegistrations = getUserRegistrations(userRole);

        for (User sellerRegistration : sellerRegistrations) {
            int year = sellerRegistration.getRegistered().getYear();
            int month = sellerRegistration.getRegistered().getMonthValue();
            int dayOfMonth = sellerRegistration.getRegistered().getDayOfMonth();
            String date = year + "." + month + "." + dayOfMonth;

            userDates.merge(date, 1, Integer::sum);
        }
        return userDates;
    }

    public List<User> getUserRegistrations(Role userRole) {
        List<User> users = this.userRepository.findAll();
        return users.stream().filter(use -> use.getRoles().contains(userRole)).collect(Collectors.toList());
    }

}
