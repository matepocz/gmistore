package hu.progmasters.gmistore.registration;

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
        System.setProperty("webdriver.chrome.driver", hu.progmasters.gmistore.registration.Utils.CHROME_DRIVER_LOCATION);
    }

    @BeforeMethod
    public void settings() {
        driver.get(hu.progmasters.gmistore.registration.Utils.BASE_URL);
        driver.manage().addCookie(GDPR);
    }

    @Test(testName = "Send valid form")
    public static void sendValidRegistrationForm() {
        hu.progmasters.gmistore.registration.Email email = new hu.progmasters.gmistore.registration.Email(driver);
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
        hu.progmasters.gmistore.registration.Email email = new hu.progmasters.gmistore.registration.Email(driver);
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
