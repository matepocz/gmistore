package hu.progmasters.gmistore.emailsending;

import org.openqa.selenium.WebDriver;

public class WrongMessageField extends PageObject implements FieldSettings,ErrorMessegesSettings{


    public WrongMessageField(WebDriver driver) {
        super(driver);
    }

    @Override
    public String emailErrorMessage() {
        return getWrongEmailInputMessage().getText();
    }

    @Override
    public String tooShortOrEmptyErrorMessage() {
        return getTooShortMessageFieldErrorMessage().getText();
    }

    @Override
    public String tooLongErrorMessage() {
        return getTooLongMessageFieldErrorMessage().getText();
    }

    @Override
    public String serverErrorMessage() {
        return getSubmitServerErrorMessage().getText();
    }

    @Override
    public void subjectInput(String subjectInput) {
        getSubjectInputField().sendKeys(subjectInput);
    }

    @Override
    public void messageInput(String messageInput) {
        getMessageInputField().sendKeys(messageInput);
    }

    @Override
    public void emailInput(String emailInput) {
        getEmailInputField().sendKeys(emailInput);
    }

    @Override
    public void pressGdprButton() {
        getGdprSubmit().click();
    }
}
