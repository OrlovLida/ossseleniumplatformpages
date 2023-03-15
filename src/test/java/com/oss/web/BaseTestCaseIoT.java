package com.oss.web;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.IHookCallBack;
import org.testng.IHookable;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;

import com.comarch.oss.services.infrastructure.objectmapper.JDK8ObjectMapper;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.ObjectMapperConfig;
import com.jayway.restassured.config.RestAssuredConfig;
import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.mainheader.LoginPanel;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.iaa.NavigationPanelPage;
import com.oss.pages.iaa.servicedesk.ServiceDeskMenuPage;
import com.oss.pages.iaa.servicedesk.issue.ticket.TicketDashboardPage;
import com.oss.pages.platform.HomePage;
import com.oss.pages.platform.LoginPage;
import com.oss.serviceClient.Environment;
import com.oss.serviceClient.EnvironmentRequestClient;
import com.oss.utils.TestListener;

import io.github.bonigarcia.wdm.WebDriverManager;

import static com.oss.configuration.Configuration.CONFIGURATION;
@Listeners({TestListener.class})
public class BaseTestCaseIoT extends BaseTestCase implements IHookable {

    public static final String BASIC_URL = CONFIGURATION.getUrl();
    public static final String MOCK_PATH = CONFIGURATION.getValue("mockPath");
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseTestCaseIoT.class);

    ServiceDeskMenuPage serviceDeskMenuPage;

@ BeforeClass
    public void openBrowser() {
        RestAssured.config = prepareRestAssureConfig();
        Environment environment = Environment.createEnvironmentFromConfiguration();
        environmentRequestClient = new EnvironmentRequestClient(environment);
        if (CONFIGURATION.getDriver().equals("chrome")) {
            startChromeDriver();
        } else {
            startFirefoxDriver();
        }
        webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(50));
        driver.navigate().to("https://vendor.test.iot-450c.swan.comarch");
        driver.findElement(By.cssSelector("[id='username']")).sendKeys("kinga.balcar-mazur@comarch.com");
        driver.findElement(By.cssSelector("[id='password']")).sendKeys("Dziczyzna_2424");
        DelayUtils.sleep(1000);
        driver.findElement(By.cssSelector("[class='mdc-button__ripple']")).click();
        DelayUtils.sleep(2000);
        driver.findElement(By.cssSelector("[name='accept']")).click();
        serviceDeskMenuPage = new ServiceDeskMenuPage(driver, webDriverWait).openMainPage(BASIC_URL);
        addCookies(driver);
    }
}
