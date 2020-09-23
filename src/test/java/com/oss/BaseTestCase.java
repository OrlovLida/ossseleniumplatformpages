package com.oss;

import com.oss.pages.platform.HomePage;
import com.oss.pages.platform.LoginPage;

import com.oss.utils.TestListener;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import static com.oss.configuration.Configuration.CONFIGURATION;

@Listeners({TestListener.class})
public class BaseTestCase {

    public static final String BASIC_URL = CONFIGURATION.getUrl();
    public static final String MOCK_PATH = CONFIGURATION.getValue("mockPath");

    public WebDriver driver;
    public WebDriverWait webDriverWait;
    protected HomePage homePage;


    @BeforeClass
    public void openBrowser() {
        if (CONFIGURATION.getDriver().equals("chrome")) {
            startChromeDriver();
        }
        else{
            startFirefoxDriver();
        }
        webDriverWait = new WebDriverWait(driver, 50);
        LoginPage loginPage = new LoginPage(driver, BASIC_URL).open();
        driver.manage().addCookie(new Cookie("i18nCurrentLocale", "en", BASIC_URL.split("//")[1].split(":")[0], "/",  null, false, false));
        this.homePage = loginPage.login();
    }

    @AfterClass
    public void closeBrowser() {
        if(driver != null) {
            driver.quit();
        }
    }

    private void startChromeDriver(){
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        if (CONFIGURATION.getValue("locally").equals("true")) {
            System.setProperty("webdriver.chrome.driver", CONFIGURATION.getValue("chromeDriverPath"));
            options.addArguments("start-maximized");
        }
        else {
            System.setProperty("webdriver.chrome.driver", CONFIGURATION.getValue("chromeDriverLinuxPath"));
            options.addArguments("--window-size=1920,1080");
            options.addArguments("--headless");
        }
        driver = new ChromeDriver(options);
    }

    private void startFirefoxDriver(){
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--width=1920");
        options.addArguments("--height=1080");
        if (CONFIGURATION.getValue("locally").equals("true")) {
            System.setProperty("webdriver.gecko.driver", CONFIGURATION.getValue("geckoDriverPath"));
        }
        else {
            System.setProperty("webdriver.gecko.driver", CONFIGURATION.getValue("geckoDriverLinuxPath"));
            options.addArguments("--headless");
        }
        driver = new FirefoxDriver(options);
        driver.manage().window().maximize();
    }

}
