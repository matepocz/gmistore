package hu.progmasters.gmistore.controller;

import hu.progmasters.gmistore.dto.UserDto;
import hu.progmasters.gmistore.model.User;
import hu.progmasters.gmistore.response.GenericResponse;
import hu.progmasters.gmistore.service.EmailSenderService;
import hu.progmasters.gmistore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.UUID;
import java.util.logging.LogRecord;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final EmailSenderService emailSenderService;

    @Autowired
    public UserController(UserService userService, EmailSenderService emailSenderService) {
        this.userService = userService;
        this.emailSenderService = emailSenderService;
    }

    @GetMapping("/my-account")
    public ResponseEntity<UserDto> getUserData() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDto userDto = userService.getUserData(username);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PostMapping("/resetPassword")
    public GenericResponse resetPassword(HttpServletRequest request,
                                         @RequestParam("email") String userEmail) {
        User user = userService.findUserByEmail(userEmail);
        String token = UUID.randomUUID().toString();

        userService.createPasswordResetTokenForUser(user, token);

        emailSenderService.sendEmail(constructResetTokenEmail(getAppUrl(request),
                request.getLocale(), token, user));
        return new GenericResponse(
                "jkkj");
    }

    private SimpleMailMessage constructResetTokenEmail(
            String contextPath, Locale locale, String token, User user) {
        String url = contextPath + "api/user/changePassword?token=" + token;
        String message = "Reset password:" + locale + " \r\n";
        return constructEmail("Reset Password", message + " \r\n" + url, user);
    }

    private SimpleMailMessage constructEmail(String subject, String body,
                                             User user) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(user.getEmail());
        email.setFrom("gmistarter@gmail.com");
        return email;
    }

    private String getAppUrl(HttpServletRequest request) {
        return request.getRequestURL().toString().replace(request.getRequestURI(), request.getContextPath());
    }
}
