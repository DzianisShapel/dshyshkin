package org.herokuapp;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class NegativeTest {

    @Parameters({ "username", "password", "expectedMessage" })
    @Test(priority=1, groups = { "negativeTests", "smokeTests" })
    public void invalidLogin(String username, String password, String expectedMessage) {

        String url = "http://the-internet.herokuapp.com/login";

        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get(url);
        sleep(1000);

        driver.findElement(By.xpath("//input[@name='username']")).sendKeys(username);
        driver.findElement(By.xpath("//input[@name='password']")).sendKeys(password);
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        String actualAlertMessage = driver.findElement(By.xpath("//div[@id='flash']")).getText();
        String actualUrl = driver.getCurrentUrl();

        Assert.assertTrue(actualAlertMessage.contains(expectedMessage));
        Assert.assertEquals(actualUrl, url, "Actual page url is not the same as expected");

        driver.quit();
    }

    /*
    @Test(priority=2, groups = { "negativeTests" })
    public void invalidPassword() {
        String url = "http://the-internet.herokuapp.com/login";
        String username = "tomsmith";
        String invalidPassword = "invalid";

        System.setProperty("webdriver.chrome.driver", "src\\main\\resources\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get(url);
        sleep(1000);

        driver.findElement(By.xpath("//input[@name='username']")).sendKeys(username);
        driver.findElement(By.xpath("//input[@name='password']")).sendKeys(invalidPassword);
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        String actualAlertMessage = driver.findElement(By.xpath("//div[@id='flash']")).getText();
        String expectedAlertMessage = "Your password is invalid!";
        String actualUrl = driver.getCurrentUrl();

        Assert.assertTrue(actualAlertMessage.contains(expectedAlertMessage));
        Assert.assertEquals(actualUrl, url, "Actual page url is not the same as expected");

        driver.quit();

    }*/

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
