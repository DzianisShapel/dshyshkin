package org.herokuapp;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import java.time.Duration;

public class ExceptionsTests {
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

    @Test
    public void notVisibleTest() {

        String url = "http://the-internet.herokuapp.com/dynamic_loading/1";
        driver.get(url);
        driver.findElement(By.xpath("//button[text()='Start']")).click();

        WebElement fiinishButton = driver.findElement(By.id("finish"));

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(fiinishButton));

        String actualMessage = fiinishButton.getText();
        String expectedMessage = "Hello World!";

        Assert.assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void timeoutTest() {

        String url = "http://the-internet.herokuapp.com/dynamic_loading/1";
        driver.get(url);
        driver.findElement(By.xpath("//button[text()='Start']")).click();

        WebElement finishButton = driver.findElement(By.id("finish"));

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        try {
            wait.until(ExpectedConditions.visibilityOf(finishButton));
        } catch (TimeoutException e) {
            System.out.println("Exception catched: " + e.getMessage());
            sleep(3000);
        }

        String actualMessage = finishButton.getText();
        String expectedMessage = "Hello World!";

        Assert.assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void noSuchElementTest() {

        String url = "http://the-internet.herokuapp.com/dynamic_loading/2";
        driver.get(url);
        driver.findElement(By.xpath("//button[text()='Start']")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement finishButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("finish")));

        String actualMessage = finishButton.getText();
        String expectedMessage = "Hello World!";

        Assert.assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void staleElementTest() {

        String url = "http://the-internet.herokuapp.com/dynamic_controls";
        driver.get(url);

        WebElement checkbox = driver.findElement(By.id("checkbox"));
        WebElement removeButton = driver.findElement(By.xpath("//button[contains(text(), 'Remove')]"));

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        removeButton.click();

        Assert.assertTrue(wait.until(ExpectedConditions.stalenessOf(checkbox)));

        WebElement addButton = driver.findElement(By.xpath("//button[contains(text(), 'Add')]"));
        addButton.click();

        checkbox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("checkbox")));
        Assert.assertTrue(checkbox.isDisplayed());

    }


    @Test
    public void challengeTest() {

        String url = "http://the-internet.herokuapp.com/dynamic_controls";
        driver.get(url);

        WebElement textField = driver.findElement(By.xpath("(//input)[2]"));
        WebElement enableButton = driver.findElement(By.xpath("//button[contains(text(), 'Enable')]"));

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        enableButton.click();
        wait.until(ExpectedConditions.elementToBeClickable(textField));
        textField.sendKeys("Hello World!");

        Assert.assertEquals(textField.getAttribute("value"), "Hello World!");
    }







}
