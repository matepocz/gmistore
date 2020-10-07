package selenium.emailsending;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class PageObject {
    protected WebDriver driver;

    public PageObject(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(id = "email-input-field")
    private WebElement emailInputField;

    @FindBy(id = "subject-input-field")
    private WebElement subjectInputField;

    @FindBy(id = "message-input-field")
    private WebElement messageInputField;

    @FindBy(xpath = "//*[@id=\"too-short-or-empty-subject-error\"]")
    private WebElement tooShortOrEmptySubjectErrorMessage;

    @FindBy(xpath = "//*[@id=\"too-long-subject-error\"]")
    private WebElement tooLongSubjectErrorMessage;

    @FindBy(xpath = "//*[@id=\"submit-server-error\"]")
    private WebElement submitServerErrorMessage;

    @FindBy(xpath = "//*[@id=\"gdpr-btn\"]")
    private WebElement gdprSubmit;

    @FindBy(xpath = "//*[@id=\"wrong-email-input\"]")
    private WebElement wrongEmailInputMessage;

    @FindBy(xpath = "//*[@id=\"email-send-btn\"]")
    private WebElement emailSendBtn;

    @FindBy(xpath = "//*[@id=\"too-short-message\"]")
    private WebElement tooShortMessageFieldErrorMessage;

    @FindBy(xpath = "//*[@id=\"too-long-message\"]")
    private WebElement tooLongMessageFieldErrorMessage;

    public WebElement getTooShortMessageFieldErrorMessage() {
        return tooShortMessageFieldErrorMessage;
    }

    public void setTooShortMessageFieldErrorMessage(WebElement tooShortMessageFieldErrorMessage) {
        this.tooShortMessageFieldErrorMessage = tooShortMessageFieldErrorMessage;
    }

    public WebElement getTooLongMessageFieldErrorMessage() {
        return tooLongMessageFieldErrorMessage;
    }

    public void setTooLongMessageFieldErrorMessage(WebElement tooLongMessageFieldErrorMessage) {
        this.tooLongMessageFieldErrorMessage = tooLongMessageFieldErrorMessage;
    }

    public WebElement getWrongEmailInputMessage() {
        return wrongEmailInputMessage;
    }

    public void setWrongEmailInputMessage(WebElement wrongEmailInputMessage) {
        this.wrongEmailInputMessage = wrongEmailInputMessage;
    }

    public WebElement getEmailSendBtn() {
        return emailSendBtn;
    }

    public void setEmailSendBtn(WebElement emailSendBtn) {
        this.emailSendBtn = emailSendBtn;
    }

    public WebElement getEmailInputField() {
        return emailInputField;
    }

    public void setEmailInputField(WebElement emailInputField) {
        this.emailInputField = emailInputField;
    }

    public WebElement getSubjectInputField() {
        return subjectInputField;
    }

    public void setSubjectInputField(WebElement subjectInputField) {
        this.subjectInputField = subjectInputField;
    }

    public WebElement getMessageInputField() {
        return messageInputField;
    }

    public void setMessageInputField(WebElement messageInputField) {
        this.messageInputField = messageInputField;
    }

    public WebElement getTooShortOrEmptySubjectErrorMessage() {
        return tooShortOrEmptySubjectErrorMessage;
    }

    public void setTooShortOrEmptySubjectErrorMessage(WebElement tooShortOrEmptySubjectErrorMessage) {
        this.tooShortOrEmptySubjectErrorMessage = tooShortOrEmptySubjectErrorMessage;
    }

    public WebElement getTooLongSubjectErrorMessage() {
        return tooLongSubjectErrorMessage;
    }

    public void setTooLongSubjectErrorMessage(WebElement tooLongSubjectErrorMessage) {
        this.tooLongSubjectErrorMessage = tooLongSubjectErrorMessage;
    }

    public WebElement getSubmitServerErrorMessage() {
        return submitServerErrorMessage;
    }

    public void setSubmitServerErrorMessage(WebElement submitServerErrorMessage) {
        this.submitServerErrorMessage = submitServerErrorMessage;
    }

    public WebElement getGdprSubmit() {
        return gdprSubmit;
    }

    public void setGdprSubmit(WebElement gdprSubmit) {
        this.gdprSubmit = gdprSubmit;
    }
}
