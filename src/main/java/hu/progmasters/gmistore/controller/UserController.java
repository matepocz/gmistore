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

    @PostMapping("/resetPassword")
    public GenericResponse findUserAndSendMailToResetPassword(HttpServletRequest request, @RequestParam("email") String userEmail) {
        resetPasswordService.findUserAndSendMailToResetPassword(request, userEmail);
        return new GenericResponse(
                "jkkj");
    }

    @GetMapping("/changePassword")
    public String showChangePasswordPage(Locale locale, Model model,
                                         @RequestParam("token") String token) {
        String result = securityService.validatePasswordResetToken(token);

        //TODO - redirect to frontend page
        if (result != null) {
//            String message = messages.getMessage("auth.message." + result, null, locale);
            return "redirect:/login.html?lang="
                    + locale.getLanguage() + "&message=";
        } else {
            model.addAttribute("token", token);
            return "redirect:/updatePassword.html?lang=" + locale.getLanguage();
        }
    }

    @PostMapping("/savePassword")
    public ResponseEntity<User> savePassword(final Locale locale, @Valid PasswordTokenDto passwordDto) {

        String result = securityService.validatePasswordResetToken(passwordDto.getToken());
        if (result != null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<User> user = userService.getUserByPasswordResetToken(passwordDto.getToken());
        if (user.isPresent()) {
            userService.changeUserPassword(user.get(), passwordDto.getNewPassword());
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
