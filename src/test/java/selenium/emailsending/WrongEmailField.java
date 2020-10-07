package selenium.emailsending;

import org.openqa.selenium.WebDriver;

public class WrongEmailField extends PageObject implements FieldSettings, ErrorMessegesSettings {

    public WrongEmailField(WebDriver driver) {
        super(driver);
    }

    @Override
    public void emailInput(String emailInput) {
        getEmailInputField().sendKeys(emailInput);
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
    public String emailErrorMessage() {
        return getWrongEmailInputMessage().getText();
    }

    @Override
    public String tooShortOrEmptyErrorMessage() {
        return null;
    }

    @Override
    public String tooLongErrorMessage() {
        return null;
    }

    @Override
    public String serverErrorMessage() {
        return null;
    }

    @Override
    public void pressGdprButton() {
        getGdprSubmit().click();
    }

}
