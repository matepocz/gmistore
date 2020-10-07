package selenium.emailsending;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestCases {
    private static final WebDriver driver = new ChromeDriver();
    private static final Cookie GDPR = new Cookie("isView", "true");

    @BeforeSuite
    public void setup() {
        // ChromeDriver location set up in Utils class
        System.setProperty("webdriver.chrome.driver", Utils.CHROME_DRIVER_LOCATION);
    }

    @BeforeMethod
    public void cookieSetting() {
        driver.get(Utils.BASE_URL);
        driver.manage().addCookie(GDPR);
    }


    @Test(testName = "Send a valid form")
    public static void sendEmailWithGoodInputFieldsTest() throws InterruptedException {
        GoodEmailForm goodEmailForm = new GoodEmailForm(driver);
        goodEmailForm.emailInput("teszt@gmi.om");
        goodEmailForm.subjectInput("teszt");
        goodEmailForm.messageInput("teszt üzenet");
        Thread.sleep(1000);
        goodEmailForm.pressSendEmailButton();
        assertTrue(goodEmailForm.getEmailSendBtn().isEnabled());
    }

    @Test(testName = "Wrong email")
    public static void wrongEmailInputFieldTest() {
        WrongEmailField wrongEmailField = new WrongEmailField(driver);
        wrongEmailField.emailInput("teszt");
        wrongEmailField.subjectInput("teszt");
        wrongEmailField.messageInput("teszt üzenet");
        String errorMessage = wrongEmailField.emailErrorMessage();
        assertEquals(errorMessage, "Érvénytelen email cím!");
    }

    @Test(testName = "Too short or empty subject")
    public static void tooShortOrEmptySubjectInputFieldTest() {
        WrongSubjectField wrongSubjectField = new WrongSubjectField(driver);
        wrongSubjectField.emailInput("teszt@gmi.om");
        wrongSubjectField.subjectInput("");
        wrongSubjectField.messageInput("teszt üzenet");
        String errorMessage = wrongSubjectField.tooShortOrEmptyErrorMessage();
        assertEquals(errorMessage, "Az tárgy mező kötelező(min. 3 karakter)!");
    }

    @Test(testName = "Too long subject")
    public static void tooLongSubjectInputFieldTest() {
        WrongSubjectField wrongSubjectField = new WrongSubjectField(driver);
        wrongSubjectField.emailInput("teszt@gmi.om");
        wrongSubjectField.subjectInput("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaass");
        wrongSubjectField.messageInput("teszt üzenet");
        String errorMessage = wrongSubjectField.tooLongErrorMessage();
        assertEquals(errorMessage, "Max 30 karakter!");
    }

    @Test(testName = "Too short message")
    public static void tooShortMessageFieldTest() throws InterruptedException {
        WrongMessageField wrongMessageField = new WrongMessageField(driver);
        wrongMessageField.messageInput("a");
        wrongMessageField.emailInput("teszt@gmi.om");
        wrongMessageField.subjectInput("teszt");
        String errorMessage = wrongMessageField.tooShortOrEmptyErrorMessage();
        Thread.sleep(1000);
        assertEquals(errorMessage, "A mező kitöltése kötelező (min. 3 karakter)!");
    }

    @Test(testName = "Too long message")
    public static void tooLongMessageFieldTest() throws InterruptedException {
        WrongMessageField wrongMessageField = new WrongMessageField(driver);
        wrongMessageField.messageInput("Lorem ipsum dolor sit amet, " +
                "consectetur adipiscing elit. Nulla fringilla pulvinar tellus. " +
                "Mauris convallis purus augue, eget pharetra orci consectetur ac. " +
                "In hac habitasse platea dictumst. Aenean eget ex id libero aliquet fringilla. " +
                "Maecenas sagittis magna arcu, et vestibulum augue blandit eu. " +
                "Integer ornare congue justo nec dictum. " +
                "Aenean ac est mi. Phasellus ut vulputate nisl. Quisque mauris lorem, consequat vel accumsan vitae," +
                " mattis pellentesque sapien. In lectus lorem, convallis nec risus quis, tristique ullamcorper dolor. " +
                "Praesent ultrices nisi at eros consequat consectetur. Aliquam iaculis semper consequat. " +
                "Nullam venenatis eros sit amet finibus ornare. Cras massa magna, elementum sed purus id," +
                " consectetur cursus lorem." + "Class aptent taciti sociosqu ad litora torquent per conubia nostra, " +
                "per inceptos himenaeos. Morbi ultrices dapibus interdum. Integer lobortis feugiat leo at commodo. " +
                "Fusce sapien tortor, laoreet et rutrum eu, posuere dapibus turpis. Fusce accumsan consectetur eros.");
        wrongMessageField.emailInput("teszt@gmi.om");
        wrongMessageField.subjectInput("teszt");
        String errorMessage = wrongMessageField.tooLongErrorMessage();
        Thread.sleep(1000);
        assertEquals(errorMessage, "Max 1000 karakter!");
    }

    @Test(testName = "All input is wrong")
    public static void allFieldIsWrongTest() {
        WrongForm wrongForm = new WrongForm(driver);
        wrongForm.emailInput("");
        wrongForm.subjectInput("");
        wrongForm.messageInput("");
        assertFalse(wrongForm.getEmailSendBtn().isEnabled());
    }

    @AfterSuite
    public static void cleanUp() {
        driver.manage().deleteAllCookies();
        driver.close();
    }
}
