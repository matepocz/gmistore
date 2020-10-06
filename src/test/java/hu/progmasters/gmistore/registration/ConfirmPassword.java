package hu.progmasters.gmistore.registration;

import org.openqa.selenium.WebDriver;

public class ConfirmPassword extends PageObject implements FieldSettings, ErrorMessagesSettings {
    public ConfirmPassword(WebDriver driver) {
        super(driver);
    }

    @Override
    public String frontendValidationError() {
        return getConfirmPasswordMatchingError().getText();
    }

    @Override
    public String serverError() {
        return "";
    }

    @Override
    public String usingError() {
        return "";
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


}
