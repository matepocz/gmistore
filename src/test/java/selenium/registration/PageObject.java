package selenium.registration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class PageObject {
    protected WebDriver driver;

    public PageObject(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(id = "firstname-input")
    private WebElement firstNameInputField;

    @FindBy(id = "lastname-input")
    private WebElement lastNameInputField;

    @FindBy(id = "username-input")
    private WebElement userNameInputField;

    @FindBy(id = "email-input")
    private WebElement emailInputField;

    @FindBy(id = "password-input")
    private WebElement passwordInputField;

    @FindBy(id = "confirm-password-input")
    private WebElement confirmPasswordInputField;

    @FindBy(xpath = "//*[@id=\"firstname-server-error\"]")
    private WebElement firstNameServerError;

    @FindBy(xpath = "//*[@id=\"firstname-frontend-validation-error\"]")
    private WebElement firstNameFrontendValidationError;

    @FindBy(xpath = "//*[@id=\"lastname-server-error\"]")
    private WebElement lastNameServerError;

    @FindBy(xpath = "//*[@id=\"lastname-frontend-validation-error\"]")
    private WebElement lastNameFrontendValidationError;

    @FindBy(xpath = "//*[@id=\"username-server-error\"]")
    private WebElement userNameServerError;

    @FindBy(xpath = "//*[@id=\"usename-frontend-validation-error\"]")
    private WebElement userNameFrontendValidationError;

    @FindBy(xpath = "//*[@id=\"email-server-error\"]")
    private WebElement emailServerError;

    @FindBy(xpath = "//*[@id=\"email-frontend-validation-error\"]")
    private WebElement emailFrontendValidationError;

    @FindBy(xpath = "//*[@id=\"email-using-error\"]")
    private WebElement emailUsingError;

    @FindBy(xpath = "//*[@id=\"password-server-error\"]")
    private WebElement passwordServerError;

    @FindBy(xpath = "//*[@id=\"password-frontend-validation-error\"]")
    private WebElement passwordFrontendValidationError;

    @FindBy(xpath = "//*[@id=\"confirm-password-notMatch-error\"]")
    private WebElement confirmPasswordMatchingError;

    @FindBy(xpath = "//*[@id=\"submit-button\"]")
    private WebElement registrationSendButton;

    //Getters,Setters


    public WebElement getFirstNameInputField() {
        return firstNameInputField;
    }

    public void setFirstNameInputField(WebElement firstNameInputField) {
        this.firstNameInputField = firstNameInputField;
    }

    public WebElement getLastNameInputField() {
        return lastNameInputField;
    }

    public void setLastNameInputField(WebElement lastNameInputField) {
        this.lastNameInputField = lastNameInputField;
    }

    public WebElement getUserNameInputField() {
        return userNameInputField;
    }

    public void setUserNameInputField(WebElement userNameInputField) {
        this.userNameInputField = userNameInputField;
    }

    public WebElement getEmailInputField() {
        return emailInputField;
    }

    public void setEmailInputField(WebElement emailInputField) {
        this.emailInputField = emailInputField;
    }

    public WebElement getPasswordInputField() {
        return passwordInputField;
    }

    public void setPasswordInputField(WebElement passwordInputField) {
        this.passwordInputField = passwordInputField;
    }

    public WebElement getConfirmPasswordInputField() {
        return confirmPasswordInputField;
    }

    public void setConfirmPasswordInputField(WebElement confirmPasswordInputField) {
        this.confirmPasswordInputField = confirmPasswordInputField;
    }

    public WebElement getFirstNameServerError() {
        return firstNameServerError;
    }

    public void setFirstNameServerError(WebElement firstNameServerError) {
        this.firstNameServerError = firstNameServerError;
    }

    public WebElement getFirstNameFrontendValidationError() {
        return firstNameFrontendValidationError;
    }

    public void setFirstNameFrontendValidationError(WebElement firstNameFrontendValidationError) {
        this.firstNameFrontendValidationError = firstNameFrontendValidationError;
    }

    public WebElement getLastNameServerError() {
        return lastNameServerError;
    }

    public void setLastNameServerError(WebElement lastNameServerError) {
        this.lastNameServerError = lastNameServerError;
    }

    public WebElement getLastNameFrontendValidationError() {
        return lastNameFrontendValidationError;
    }

    public void setLastNameFrontendValidationError(WebElement lastNameFrontendValidationError) {
        this.lastNameFrontendValidationError = lastNameFrontendValidationError;
    }

    public WebElement getUserNameServerError() {
        return userNameServerError;
    }

    public void setUserNameServerError(WebElement userNameServerError) {
        this.userNameServerError = userNameServerError;
    }

    public WebElement getUserNameFrontendValidationError() {
        return userNameFrontendValidationError;
    }

    public void setUserNameFrontendValidationError(WebElement userNameFrontendValidationError) {
        this.userNameFrontendValidationError = userNameFrontendValidationError;
    }

    public WebElement getEmailServerError() {
        return emailServerError;
    }

    public void setEmailServerError(WebElement emailServerError) {
        this.emailServerError = emailServerError;
    }

    public WebElement getEmailFrontendValidationError() {
        return emailFrontendValidationError;
    }

    public void setEmailFrontendValidationError(WebElement emailFrontendValidationError) {
        this.emailFrontendValidationError = emailFrontendValidationError;
    }

    public WebElement getEmailUsingError() {
        return emailUsingError;
    }

    public void setEmailUsingError(WebElement emailUsingError) {
        this.emailUsingError = emailUsingError;
    }

    public WebElement getPasswordServerError() {
        return passwordServerError;
    }

    public void setPasswordServerError(WebElement passwordServerError) {
        this.passwordServerError = passwordServerError;
    }

    public WebElement getPasswordFrontendValidationError() {
        return passwordFrontendValidationError;
    }

    public void setPasswordFrontendValidationError(WebElement passwordFrontendValidationError) {
        this.passwordFrontendValidationError = passwordFrontendValidationError;
    }

    public WebElement getConfirmPasswordMatchingError() {
        return confirmPasswordMatchingError;
    }

    public void setConfirmPasswordMatchingError(WebElement confirmPasswordMatchingError) {
        this.confirmPasswordMatchingError = confirmPasswordMatchingError;
    }

    public WebElement getRegistrationSendButton() {
        return registrationSendButton;
    }

    public void setRegistrationSendButton(WebElement registrationSendButton) {
        this.registrationSendButton = registrationSendButton;
    }
}
