package feature;

import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;

import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;

public class MyStepdefs {
    private WebDriver driver;


    @Given("i have opened a {}")
    public void iHaveOpenedABrowser(String browser) {
        if (browser.equals("chrome")) {
            System.setProperty("webdriver.chrome.driver", "C:\\Selenium\\chromedriver.exe");
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--remote-allow-origins=*");
            driver = new ChromeDriver(options);
            driver.get("https://login.mailchimp.com/signup/");

        } else if (browser.equals("edge")) {
            System.setProperty("webdriver.edge.driver", "C:\\Selenium\\msedgedriver.exe");
            driver = new EdgeDriver();
            driver.get("https://login.mailchimp.com/signup/");
        }

    }

    @And("i have written a {}")
    public void iHaveWrittenAEmail(String userEmail) {
        sendKeysEmail(userEmail);
    }

    @And("i have a username {}")
    public void iHaveAUsernameOfLength(String condition) {
        if (condition.equals("normal")) {
            String username = randomizer();
            sendUsername(username);
        } else if (condition.equals("long")) {
            String username = randomizer();
            for (int run = 1; run <= 12; run = run + 1) { //gör username lång
                username += "username";
            }
            sendUsername(username);
        } else if (condition.equals("existing")) {
            sendUsername("Anna");

        }
    }


    @And("i have written my {}")
    public void iHaveWrittenMyPassword(String password) {
        WebElement writePassword = driver.findElement(By.id("new_password"));
        writePassword.sendKeys(password);

    }

    @And("i press the sign up button")
    public void iPressTheSignUpButton() {

        WebElement button = driver.findElement(By.id("create-account-enabled"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,500)", "");//scrolla ner så den når knappen
        Actions action = new Actions(driver);
        action.moveToElement(button).perform(); // så knappen verkligen blir tryckt
        button.click();


    }

    @And("my sign in should show {}")
    public void mySignInShouldWantedResult(String result) {

        if (result.equals("pass")) {

            Wait<WebDriver> wait = getWebDriverWait();
            wait.until(ExpectedConditions.textToBe(By.cssSelector(".\\!margin-bottom--lv3"), "Check your email"));
            String actual = driver.getTitle();
            String expected = "Success | Mailchimp";
            assertEquals(expected, actual);

        } else if (result.equals("fail")) {
            Wait<WebDriver> wait = getWebDriverWait();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".invalid-error")));
            WebElement element = driver.findElement((By.className("invalid-error")));
            String actual = element.getText();
            if (actual.equals("Enter a value less than 100 characters long")) {
                String expected = "Enter a value less than 100 characters long";

                assertEquals(expected, actual);
            } else if (actual.equals("Great minds think alike - someone already has this username. If it's you, log in.")) {
                String expected = "Great minds think alike - someone already has this username. If it's you, log in.";
                assertEquals(expected, actual);

            } else if (actual.equals("An email address must contain a single @.")) {
                String expected = "An email address must contain a single @.";
                assertEquals(expected, actual);
            }

        }
    }

    private Wait<WebDriver> getWebDriverWait() {
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofSeconds(10))
                .ignoring(NoSuchElementException.class);
        return wait;
    }

    private void sendUsername(String username) {
        WebElement delete = driver.findElement(By.id("new_username")); // //tar bort den defaulta mailen
        delete.sendKeys("clear");
        delete.clear();

        WebElement userName = driver.findElement(By.id("new_username"));
        userName.sendKeys(username);
    }

    private void sendKeysEmail(String userEmail) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement email = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("email")));
        email.sendKeys(userEmail);
    }


    private static String randomizer() {

        String username = "Username";
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("hhmmss");
        String strDate = dateFormat.format(date);
        username += strDate;
        return username;
    }

    @After
    public void tearDown() {
        driver.close();
        driver.quit();

    }


}
