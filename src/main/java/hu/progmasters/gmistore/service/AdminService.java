package hu.progmasters.gmistore.service;

import hu.progmasters.gmistore.dto.user.UserDto;
import hu.progmasters.gmistore.dto.user.UserEditableDetailsDto;
import hu.progmasters.gmistore.dto.user.UserIsActiveDto;
import hu.progmasters.gmistore.dto.user.UserRegistrationDTO;
import hu.progmasters.gmistore.enums.Role;
import hu.progmasters.gmistore.model.User;
import hu.progmasters.gmistore.repository.UserRepository;
import org.cloudinary.json.JSONObject;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdminService {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ProductService.class);
    private final SessionRegistry sessionRegistry;
    private final UserRepository userRepository;
    private final UserService userService;

    public AdminService(SessionRegistry sessionRegistry, UserRepository userRepository, UserService userService) {
        this.sessionRegistry = sessionRegistry;
        this.userRepository = userRepository;
        this.userService = userService;
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
        List<UserRegistrationDTO> sellerRegistrations = getUserRegistrations(userRole);
        return getStringIntegerMap(sellerRegistrations);
    }

    public Map<String, Integer> getSortedUserRegistrationByDateInterval(List<UserRegistrationDTO> sellerRegistrations) {
        return getStringIntegerMap(sellerRegistrations);
    }

    private Map<String, Integer> getStringIntegerMap(List<UserRegistrationDTO> sellerRegistrations) {
        Map<String, Integer> userDates = new TreeMap<>();
        for (UserRegistrationDTO sellerRegistration : sellerRegistrations) {
            int year = sellerRegistration.getRegistered().getYear();
            int month = sellerRegistration.getRegistered().getMonthValue();
            int dayOfMonth = sellerRegistration.getRegistered().getDayOfMonth();
            String date = year + "." + month + "." + dayOfMonth;

            userDates.merge(date, 1, Integer::sum);
        }
        return userDates;
    }

    public List<UserRegistrationDTO> getUserRegistrations(Role userRole) {
        return this.userRepository.findByRolesIn(userRole);
    }

    public void updateUserActivity(UserIsActiveDto userIsActive) {
        User userById = userService.getUserById(userIsActive.getId());
        userById.setActive(userIsActive.isActive());
    }

    public void updateUser(UserEditableDetailsDto user) {
        userService.updateUserById(user);
    }

    public List<UserRegistrationDTO> getUserRegistrationsByDateInterval(String criteria) {
        JSONObject obj = new JSONObject(criteria);

        String start = obj.getString("start");
        String end = obj.getString("end");
        String startDate = start.substring(0, start.length() - 1);
        String endDate = end.substring(0, end.length() - 1);
        LocalDateTime dateTimeStart = LocalDateTime.parse(startDate);
        LocalDateTime dateTimeEnd = LocalDateTime.parse(endDate);

        return userRepository.findUserRegistrationsByDateInterval(dateTimeStart, dateTimeEnd);
    }
}
