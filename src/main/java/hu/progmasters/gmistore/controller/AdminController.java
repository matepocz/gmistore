package hu.progmasters.gmistore.controller;

import hu.progmasters.gmistore.dto.*;
import hu.progmasters.gmistore.dto.order.*;
import hu.progmasters.gmistore.dto.product.ProductTableDto;
import hu.progmasters.gmistore.dto.user.*;
import hu.progmasters.gmistore.enums.Role;
import hu.progmasters.gmistore.model.Product;
import hu.progmasters.gmistore.model.User;
import hu.progmasters.gmistore.service.AdminService;
import hu.progmasters.gmistore.service.OrderService;
import hu.progmasters.gmistore.service.ProductService;
import hu.progmasters.gmistore.service.UserService;
import hu.progmasters.gmistore.validator.UserEditValidator;
import org.apache.tomcat.util.json.JSONParser;
import org.cloudinary.json.JSONArray;
import org.cloudinary.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    AdminService adminService;
    UserService userService;
    ProductService productService;
    OrderService orderService;
    UserEditValidator userEditValidator;
    ExecutorService executor;
    ActiveUserStore activeUserStore;

    @Autowired
    public AdminController(AdminService adminService, UserService userService,
                           ProductService productService, OrderService orderService,
                           UserEditValidator userEditValidator, ExecutorService executor, ActiveUserStore activeUserStore
    ) {
        this.adminService = adminService;
        this.userService = userService;
        this.userEditValidator = userEditValidator;
        this.activeUserStore = activeUserStore;
        this.orderService = orderService;
        this.executor = executor;
        this.productService = productService;
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

    //TODO - session_registry
    @GetMapping("/users/logged-in")
    public ResponseEntity<Map<String,String>> loggedInUsers() {
        Map<String, String> usersFromSessionRegistry = adminService.getUsersFromSessionRegistry();
        return new ResponseEntity<>(usersFromSessionRegistry, HttpStatus.OK);
    }

    @GetMapping("/registered")
    public ResponseEntity<UserRegistrationDateDto> getDateOfAllUserRegistrations() {
        Map<String, Integer> sortedUserRegistrationByDate = adminService.getSortedUserRegistrationByDate(Role.ROLE_USER);
        UserRegistrationDateDto userRegistrations = new UserRegistrationDateDto(sortedUserRegistrationByDate);
        return new ResponseEntity<>(userRegistrations, HttpStatus.OK);
    }

    @GetMapping("/registered/")
    public ResponseEntity<UserRegistrationDateDto> getDateOfAllUserRegistrationsByDateInterval(@RequestParam String criteria) {
        List<UserRegistrationDTO> userRegistrationsByDateInterval = adminService.getUserRegistrationsByDateInterval(criteria);
        Map<String, Integer> sortedUserRegistrationByDate = adminService.getSortedUserRegistrationByDateInterval(userRegistrationsByDateInterval);
        UserRegistrationDateDto userRegistrations = new UserRegistrationDateDto(sortedUserRegistrationByDate);
        return new ResponseEntity<>(userRegistrations, HttpStatus.OK);
    }

    @GetMapping("/income/")
    public ResponseEntity<IncomeByDaysDto> getIncomePerOrder(@RequestParam String criteria) {
        IncomeByDaysDto userRegistrationsByDateInterval = orderService.getIncomePerOrder(criteria);
        return userRegistrationsByDateInterval != null ?
                new ResponseEntity<>(userRegistrationsByDateInterval, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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

    @GetMapping("/sse")
    public SseEmitter handleSse() {
        SseEmitter emitter = new SseEmitter(150000L);
        executor.execute(() -> {
            try {
                while (true) {
                    emitter.send(adminService.getUsersFromSessionRegistry());
                    Thread.sleep(4000);
                }
            } catch (Exception ex) {
                emitter.completeWithError(ex);
            }
        });

        return emitter;
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductTableDto>> getAllProductsToTable() {
        List<ProductTableDto> orders = productService.getAllProductsToTable();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardData> getDashboardData() {
        DashboardData data = adminService.getDashBasicDashBoardData();
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

}
