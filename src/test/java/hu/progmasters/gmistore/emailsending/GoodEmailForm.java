package hu.progmasters.gmistore.emailsending;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class GoodEmailForm extends PageObject {


    @FindBy(id = "email-input-field")
    private WebElement emailInputField;

    @FindBy(id = "subject-input-field")
    private WebElement subjectInputField;

    @FindBy(id = "message-input-field")
    private WebElement messageInputField;

    @FindBy(xpath = "//*[@id=\"email-send-btn\"]")
    private WebElement emailSendBtn;

    @FindBy(xpath ="//*[@id=\"gdpr-btn\"]")
    private WebElement gdprSubmit;

    public GoodEmailForm(WebDriver driver) {
        super(driver);
    }

    public void emailInput(String emailInput) {
        getEmailInputField().sendKeys(emailInput);
    }

    public void subjectInput(String subjectInput) {
        getSubjectInputField().sendKeys(subjectInput);
    }

    public void messageInput(String messageInput) {
        getMessageInputField().sendKeys(messageInput);
    }

    public void pressSendEmailButton() {
        getEmailSendBtn().click();
    }
    public void pressGdprButton() {
        getGdprSubmit().click();
    }

}
