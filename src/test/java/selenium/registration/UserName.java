package selenium.registration;

import org.openqa.selenium.WebDriver;

public class UserName extends PageObject implements FieldSettings, ErrorMessagesSettings {
    public UserName(WebDriver driver) {
        super(driver);
    }

    @Override
    public String frontendValidationError() {
        return getUserNameFrontendValidationError().getText();
    }

    @Override
    public String serverError() {
        return getUserNameServerError().getText();
    }

    @Override
    public String usingError() {
        return getUserNameServerError().getText();
    }

    @Override
    public void firstNameInputField(String firstName) {
        this.firstNameInputField(firstName);
    }

    @Override
    public void lastNameInputField(String lastName) {
        this.lastNameInputField(lastName);
    }

    @Override
    public void userNameInputField(String username) {
        this.userNameInputField(username);
    }

    @Override
    public void emailInputField(String email) {
        this.emailInputField(email);
    }

    @Override
    public void passwordInputField(String password) {
        this.passwordInputField(password);
    }

    @Override
    public void confirmPasswordInputField(String confirmPassword) {
        this.confirmPasswordInputField(confirmPassword);
    }
}
