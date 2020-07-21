package hu.progmasters.gmistore.controller;

import hu.progmasters.gmistore.dto.UserDto;
import hu.progmasters.gmistore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/my-account")
    public ResponseEntity<UserDto> getUserData() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDto userDto = userService.getUserData(username);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
}
