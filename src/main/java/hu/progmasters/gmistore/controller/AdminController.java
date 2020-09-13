package hu.progmasters.gmistore.controller;

import hu.progmasters.gmistore.dto.*;
import hu.progmasters.gmistore.enums.Role;
import hu.progmasters.gmistore.model.User;
import hu.progmasters.gmistore.response.GenericResponse;
import hu.progmasters.gmistore.service.AdminService;
import hu.progmasters.gmistore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
public class AdminController {

    AdminService adminService;
    UserService userService;

    @Autowired
    public AdminController(AdminService adminService,UserService userService) {
        this.adminService = adminService;
        this.userService = userService;
    }

    @GetMapping("users/{id}")
    ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        UserDto user = adminService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
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

    @GetMapping("/users")
    ResponseEntity<List<UserListDetailDto>> getAllUsers() {
        List<UserListDetailDto> userList = userService.getUserList();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping("/users/roles")
    ResponseEntity<List<RolesFormDto>> getRoles() {
        List<RolesFormDto> roles = userService.getRoles();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @PutMapping("users/active")
    ResponseEntity<Void> setUserIsActive(@RequestBody UserIsActiveDto userIsActive) {
        adminService.updateUserActivity(userIsActive);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/users")
    ResponseEntity<Void> updateUserDetails(@RequestBody UserEditableDetailsDto user) {
        System.out.println(user);
        adminService.updateUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
