package hu.progmasters.gmistore.controller;

import hu.progmasters.gmistore.dto.UserRegistrationDateDto;
import hu.progmasters.gmistore.enums.Role;
import hu.progmasters.gmistore.model.User;
import hu.progmasters.gmistore.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
public class AdminController {

    AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    ResponseEntity<Integer> loggedInUsers() {
        List<String> usersFromSessionRegistry = adminService.getUsersFromSessionRegistry();
        return new ResponseEntity<>(usersFromSessionRegistry.size(), HttpStatus.OK);
    }

    @GetMapping("/registered")
    ResponseEntity<UserRegistrationDateDto> getDateOfAllUserRegistrations() {
        Map<String, Integer> sortedUserRegistrationByDate = adminService.getSortedUserRegistrationByDate(Role.ROLE_USER);
        UserRegistrationDateDto userRegistrations = new UserRegistrationDateDto(sortedUserRegistrationByDate);
        return new ResponseEntity<>(userRegistrations, HttpStatus.OK);
    }
}
