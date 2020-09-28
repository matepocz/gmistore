package hu.progmasters.gmistore.controller;

import hu.progmasters.gmistore.dto.*;
import hu.progmasters.gmistore.dto.order.IncomePerOrderDto;
import hu.progmasters.gmistore.dto.order.OrderDto;
import hu.progmasters.gmistore.dto.order.OrderListDto;
import hu.progmasters.gmistore.dto.order.OrderProductDetailsDto;
import hu.progmasters.gmistore.dto.product.ProductTableDto;
import hu.progmasters.gmistore.dto.user.*;
import hu.progmasters.gmistore.enums.Role;
import hu.progmasters.gmistore.model.Product;
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
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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

    @Autowired
    public AdminController(AdminService adminService, UserService userService,
                           ProductService productService, OrderService orderService,
                           UserEditValidator userEditValidator, ExecutorService executor
    ) {
        this.adminService = adminService;
        this.userService = userService;
        this.userEditValidator = userEditValidator;
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

    @GetMapping("/registered/")
    public ResponseEntity<UserRegistrationDateDto> getDateOfAllUserRegistrationsByDateInterval(@RequestParam String criteria) {
        List<UserRegistrationDTO> userRegistrationsByDateInterval = adminService.getUserRegistrationsByDateInterval(criteria);
        Map<String, Integer> sortedUserRegistrationByDate = adminService.getSortedUserRegistrationByDateInterval(userRegistrationsByDateInterval);
        UserRegistrationDateDto userRegistrations = new UserRegistrationDateDto(sortedUserRegistrationByDate);
        return new ResponseEntity<>(userRegistrations, HttpStatus.OK);
    }

    @GetMapping("/income")
    public ResponseEntity<List<IncomePerOrderDto>> getIncomePerOrder(@RequestParam String criteria) {
        List<IncomePerOrderDto> userRegistrationsByDateInterval = orderService.getIncomePerOrder(criteria);
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
        SseEmitter emitter = new SseEmitter();
        executor.execute(() -> {
            try {
                for (int i = 0; i < 20; i++) {
                    emitter.send(new Date());
                    Thread.sleep(4000);
                }
                emitter.complete();
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
}
