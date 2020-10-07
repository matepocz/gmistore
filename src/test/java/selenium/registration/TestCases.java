package selenium.registration;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestCases {
    private static final WebDriver driver = new ChromeDriver();
    private static final Cookie GDPR = new Cookie("isView", "true");

    @BeforeSuite
    public void setup() {
        // ChromeDriver location set up in Utils class
        System.setProperty("webdriver.chrome.driver", selenium.registration.Utils.CHROME_DRIVER_LOCATION);
    }

    @BeforeMethod
    public void settings() {
        driver.get(selenium.registration.Utils.BASE_URL);
        driver.manage().addCookie(GDPR);
    }

    @Test(testName = "Send valid form")
    public static void sendValidRegistrationForm() {
        Email email = new Email(driver);
        email.firstNameInputField("Teszt");
        email.lastNameInputField("Elek");
        email.userNameInputField("tesztelek");
        email.emailInputField("teszt@teszt.co");
        email.passwordInputField("Teszt1@");
        email.confirmPasswordInputField("Teszt1@");
        assertTrue(email.getRegistrationSendButton().isEnabled());
    }

    @Test(testName = "Try submit form with wrong fields")
    public static void trySendingFormWithWrongFields() {
        Email email = new Email(driver);
        email.firstNameInputField("");
        email.lastNameInputField("");
        email.userNameInputField("");
        email.emailInputField("");
        email.passwordInputField("");
        email.confirmPasswordInputField("");
        assertFalse(email.getRegistrationSendButton().isEnabled());
    }

    @AfterSuite
    public static void cleanUp() {
        driver.manage().deleteAllCookies();
        driver.close();
    }
}
