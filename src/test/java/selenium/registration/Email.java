package selenium.registration;

import org.openqa.selenium.WebDriver;

public class Email extends PageObject implements FieldSettings, ErrorMessagesSettings {
    public Email(WebDriver driver) {
        super(driver);
    }

    @Override
    public String frontendValidationError() {
        return getEmailFrontendValidationError().getText();
    }

    @Override
    public String serverError() {
        return getEmailServerError().getText();
    }

    @Override
    public String usingError() {
        return getEmailUsingError().getText();
    }

    @Override
    public void firstNameInputField(String firstName) {
        getFirstNameInputField().sendKeys(firstName);
    }

    @Override
    public void lastNameInputField(String lastName) {
       getLastNameInputField().sendKeys(lastName);
    }

    @Override
    public void userNameInputField(String username) {
        getUserNameInputField().sendKeys(username);
    }

    @Override
    public void emailInputField(String email) {
        getEmailInputField().sendKeys(email);
    }

    @Override
    public void passwordInputField(String password) {
        getPasswordInputField().sendKeys(password);
    }

    @Override
    public void confirmPasswordInputField(String confirmPassword) {
        getConfirmPasswordInputField().sendKeys(confirmPassword);
    }

    public void registrationSendButton() {
        this.getRegistrationSendButton().click();
    }


}
