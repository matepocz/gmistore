package hu.progmasters.gmistore.controller;

import hu.progmasters.gmistore.dto.*;
import hu.progmasters.gmistore.dto.order.OrderListDto;
import hu.progmasters.gmistore.dto.user.*;
import hu.progmasters.gmistore.enums.Role;
import hu.progmasters.gmistore.service.AdminService;
import hu.progmasters.gmistore.service.OrderService;
import hu.progmasters.gmistore.service.UserService;
import hu.progmasters.gmistore.validator.UserEditValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    AdminService adminService;
    UserService userService;
    OrderService orderService;
    UserEditValidator userEditValidator;

    @Autowired
    public AdminController(AdminService adminService,
                           UserService userService,
                           OrderService orderService,
                           UserEditValidator userEditValidator) {
        this.adminService = adminService;
        this.userService = userService;
        this.userEditValidator = userEditValidator;
        this.orderService = orderService;
    }

    @InitBinder("userEditableDetailsDto")
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(userEditValidator);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        UserDto user = adminService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Integer> loggedInUsers() {
        List<String> usersFromSessionRegistry = adminService.getUsersFromSessionRegistry();
        return new ResponseEntity<>(usersFromSessionRegistry.size(), HttpStatus.OK);
    }

    @GetMapping("/registered")
    public ResponseEntity<UserRegistrationDateDto> getDateOfAllUserRegistrations() {
        Map<String, Integer> sortedUserRegistrationByDate = adminService.getSortedUserRegistrationByDate(Role.ROLE_USER);
        UserRegistrationDateDto userRegistrations = new UserRegistrationDateDto(sortedUserRegistrationByDate);
        return new ResponseEntity<>(userRegistrations, HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserListDetailDto>> getAllUsers() {
        List<UserListDetailDto> userList = userService.getUserList();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping("/users/roles")
    public ResponseEntity<List<RolesFormDto>> getRoles() {
        List<RolesFormDto> roles = userService.getRoles();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @PutMapping("/users/active")
    public ResponseEntity<Void> setUserIsActive(@RequestBody UserIsActiveDto userIsActive) {
        adminService.updateUserActivity(userIsActive);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/users")
    public ResponseEntity<Void> updateUserDetails(@RequestBody @Valid UserEditableDetailsDto user) {
        System.out.println(user);
        adminService.updateUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderListDto>> getAllOrders() {
        List<OrderListDto> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}
