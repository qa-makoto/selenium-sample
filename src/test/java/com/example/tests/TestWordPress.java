package com.example.tests;

import static org.junit.Assert.*;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class TestWordPress {
    private WebDriver driver;
    private String baseUrl;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();

    @Before
    public void setUp() throws Exception {
        String name = System.getenv("SAUCE_USERNAME");
        String accesskey = System.getenv("SAUCE_ACCESS_KEY");
        System.out.println(name);
        String url = String.format("http://%s:%s@ondemand.saucelabs.com:80/wd/hub", name, accesskey);

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setBrowserName(System.getenv("SELENIUM_BROWSER"));
        caps.setVersion(System.getenv("SELENIUM_VERSION"));
        caps.setCapability(CapabilityType.PLATFORM, System.getenv("SELENIUM_PLATFORM"));
        caps.setCapability("name", System.getenv("JENKINS_BUILD_NUMBER"));
        driver = new RemoteWebDriver(new URL(url), caps);

        baseUrl = "http://localhost:8080/";
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().window().maximize();
    }

    @Test
    public void testLoginLogout() throws Exception {
        driver.get(baseUrl);
        driver.findElement(By.linkText("Log in")).click();
        driver.findElement(By.id("user_login")).clear();
        driver.findElement(By.id("user_login")).sendKeys("admin");
        driver.findElement(By.id("user_pass")).clear();
        driver.findElement(By.id("user_pass")).sendKeys("admin");
        driver.findElement(By.id("wp-submit")).click();
        assertEquals("Dashboard ‹ Sample — WordPress", driver.getTitle());
        new Actions(driver).moveToElement(driver.findElement(By.id("wp-admin-bar-my-account"))).perform();
        ;
        driver.findElement(By.xpath("//li[@id='wp-admin-bar-logout']/a")).click();
        assertEquals("You are now logged out.", driver.findElement(By.cssSelector("p.message")).getText());
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

    private boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    private String closeAlertAndGetItsText() {
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            if (acceptNextAlert) {
                alert.accept();
            } else {
                alert.dismiss();
            }
            return alertText;
        } finally {
            acceptNextAlert = true;
        }
    }
}
