package hu.progmasters.gmistore.service;

import hu.progmasters.gmistore.dto.DashboardData;
import hu.progmasters.gmistore.dto.user.UserDto;
import hu.progmasters.gmistore.dto.user.UserEditableDetailsDto;
import hu.progmasters.gmistore.dto.user.UserIsActiveDto;
import hu.progmasters.gmistore.dto.user.UserRegistrationDTO;
import hu.progmasters.gmistore.enums.Role;
import hu.progmasters.gmistore.model.User;
import hu.progmasters.gmistore.repository.UserRepository;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class AdminService {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ProductService.class);
    private final SessionRegistry sessionRegistry;
    private final UserRepository userRepository;
    private final UserService userService;
    private final DateService dateService;
    private final ProductService productService;

    public AdminService(SessionRegistry sessionRegistry, DateService dateService,
                        UserRepository userRepository, UserService userService, ProductService productService) {
        this.sessionRegistry = sessionRegistry;
        this.userRepository = userRepository;
        this.userService = userService;
        this.dateService = dateService;
        this.productService = productService;
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalIdentifierException("No such ID exits."));
        return new UserDto(user);
    }

    public Map<String, String> getUsersFromSessionRegistry() {
        List<Object> principals = sessionRegistry.getAllPrincipals();
        Map<String, String> users = new HashMap<>();

        for (Object principal : principals) {

            if (principal instanceof UserDetails) {
                users.put(((UserDetails) principal).getUsername(),String.valueOf(((UserDetails) principal).getAuthorities()));

            }
        }
        LOGGER.info(String.valueOf(users));

        return users;
    }

    public Map<String, Integer> getSortedUserRegistrationByDate(Role userRole) {
        List<UserRegistrationDTO> sellerRegistrations = getUserRegistrations(userRole);
        return getStringIntegerMap(sellerRegistrations);
    }

    public Map<String, Integer> getSortedUserRegistrationByDateInterval(List<UserRegistrationDTO> userRegistrations) {
        return getStringIntegerMap(userRegistrations);
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
        DateService.CreateDates createDates = dateService.stringToDate(criteria);
        return userRepository.findUserRegistrationsByDateInterval(createDates.getStart(), createDates.getEnd());
    }

    public DashboardData getDashBasicDashBoardData() {
        Integer customers = userService.countAllByRole(Role.ROLE_USER);
        Integer sellers = userService.countAllByRole(Role.ROLE_SELLER);
        Integer productsActive = productService.countAllByActive();
        Integer productsInactive = productService.countAllByInActive();
        return new DashboardData(customers,sellers,productsActive,productsInactive);
    }

//    public CreateDates stringToDate(String criteria){
//        JSONObject obj = new JSONObject(criteria);
//
//        String start = obj.getString("start");
//        String end = obj.getString("end");
//        String startDate = start.substring(0, start.length() - 1);
//        String endDate = end.substring(0, end.length() - 1);
//        LocalDateTime dateTimeStart = LocalDateTime.parse(startDate);
//        LocalDateTime dateTimeEnd = LocalDateTime.parse(endDate);
//
//        return new CreateDates(dateTimeStart, dateTimeEnd);
//    }
//
//    private static class CreateDates {
//        private final LocalDateTime start;
//        private final LocalDateTime end;
//        public CreateDates(LocalDateTime dateTimeStart, LocalDateTime dateTimeEnd) {
//            this.start = dateTimeStart;
//            this.end = dateTimeEnd;
//        }
//
//        public LocalDateTime getStart() {
//            return start;
//        }
//
//        public LocalDateTime getEnd() {
//            return end;
//        }
//    }
}

