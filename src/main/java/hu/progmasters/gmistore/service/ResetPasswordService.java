package hu.progmasters.gmistore.service;

import hu.progmasters.gmistore.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.UUID;

@Service
public class ResetPasswordService {

    @Value("${client-url}")
    private String clientUrl;

    private final UserService userService;
    private final EmailSenderService emailSenderService;

    @Autowired
    public ResetPasswordService(UserService userService, EmailSenderService emailSenderService) {
        this.userService = userService;
        this.emailSenderService = emailSenderService;
    }

    public void findUserAndSendMailToResetPassword(HttpServletRequest request, String userEmail) {
        User user = userService.findUserByEmail(userEmail);
        String token = UUID.randomUUID().toString();

        userService.createPasswordResetTokenForUser(user, token);
        emailSenderService.sendEmail(constructResetTokenEmail(getAppUrl(),
                request.getLocale(), token, user));
    }

    private String getAppUrl() {
        return clientUrl;
    }

    private SimpleMailMessage constructResetTokenEmail(
            String contextPath, Locale locale, String token, User user) {
        String url = contextPath + "/changePassword?token=" + token;
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
}
