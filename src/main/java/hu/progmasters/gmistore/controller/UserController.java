package hu.progmasters.gmistore.controller;

import hu.progmasters.gmistore.dto.PasswordTokenDto;
import hu.progmasters.gmistore.dto.UserDto;
import hu.progmasters.gmistore.model.User;
import hu.progmasters.gmistore.response.GenericResponse;
import hu.progmasters.gmistore.service.ResetPasswordService;
import hu.progmasters.gmistore.service.SecurityService;
import hu.progmasters.gmistore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Locale;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final SecurityService securityService;
    private final ResetPasswordService resetPasswordService;

    @Autowired
    public UserController(SecurityService securityService,
                          UserService userService,
                          ResetPasswordService resetPasswordService) {
        this.userService = userService;
        this.securityService = securityService;
        this.resetPasswordService = resetPasswordService;
    }

    @GetMapping("/my-account")
    public ResponseEntity<UserDto> getUserData() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDto userDto = userService.getUserData(username);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
}
