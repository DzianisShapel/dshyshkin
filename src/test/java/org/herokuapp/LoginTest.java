package org.herokuapp;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import java.time.Duration;

public class LoginTest {

    private WebDriver driver;

    @Parameters({"browser"})
    @BeforeMethod(alwaysRun = true)
    private void setUp(@Optional("chrome") String browser) {
//		Create driver
        switch (browser) {
            case "chrome":
                System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
                driver = new ChromeDriver();
                break;

            case "firefox":
                System.setProperty("webdriver.gecko.driver", "src/main/resources/geckodriver.exe");
                driver = new FirefoxDriver();
                break;
            default:
                System.out.println("Do not know how to start " + browser + ", starting chrome instead");
                System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
                driver = new ChromeDriver();
                break;
        }

        //implicit wait
        //driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);


    }

    @Test(priority = 1, groups = {"positiveTests", "smokeTests"})
    public void positiveLoginTest() {

        String url = "http://the-internet.herokuapp.com/login";
        String username = "tomsmith";
        String password = "SuperSecretPassword!";
        //explicit wait
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));

        driver.get(url);

        driver.findElement(By.xpath("//input[@name='username']")).sendKeys(username);
        driver.findElement(By.xpath("//input[@name='password']")).sendKeys(password);

        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//button[@type='submit']")))).click();



        String actualSuccessMessage = driver.findElement(By.xpath("//div[@id='flash']")).getText();
        String expectedSuccessMessage = "You logged into a secure area!";
        String actualWelcomeMessage = driver.findElement(By.xpath("//h4[@class='subheader']")).getText();
        String expectedWelcomeMessage = "Welcome to the Secure Area. When you are done click logout below.";
        String expectedUrl = "http://the-internet.herokuapp.com/secure";
        String actualUrl = driver.getCurrentUrl();

        Assert.assertEquals(actualUrl, expectedUrl, "Actual page url is not the same as expected");
        Assert.assertTrue(actualSuccessMessage.contains(expectedSuccessMessage));
        Assert.assertEquals(actualWelcomeMessage, expectedWelcomeMessage);
        Assert.assertTrue(driver.findElement(By.xpath("//a[@class='button secondary radius']")).isDisplayed());

    }

    @Parameters({"username", "password", "expectedMessage"})
    @Test(priority = 2, groups = {"negativeTests", "smokeTests"})
    public void invalidLogin(String username, String password, String expectedMessage) {

        String url = "http://the-internet.herokuapp.com/login";

        driver.get(url);


        driver.findElement(By.xpath("//input[@name='username']")).sendKeys(username);
        driver.findElement(By.xpath("//input[@name='password']")).sendKeys(password);
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        String actualAlertMessage = driver.findElement(By.xpath("//div[@id='flash']")).getText();
        String actualUrl = driver.getCurrentUrl();

        Assert.assertTrue(actualAlertMessage.contains(expectedMessage));
        Assert.assertEquals(actualUrl, url, "Actual page url is not the same as expected");

    }

    @AfterMethod
    private void tearDown() {
        driver.quit();
    }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
