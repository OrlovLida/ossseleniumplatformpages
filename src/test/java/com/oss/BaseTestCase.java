package com.oss;

import com.comarch.oss.services.infrastructure.objectmapper.JDK8ObjectMapper;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.ObjectMapperConfig;
import com.jayway.restassured.config.RestAssuredConfig;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.mainheader.LoginPanel;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.HomePage;
import com.oss.pages.platform.LoginPage;
import com.oss.serviceClient.Environment;
import com.oss.serviceClient.EnvironmentRequestClient;
import com.oss.utils.TestListener;
import io.github.bonigarcia.wdm.WebDriverManager;
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

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.oss.configuration.Configuration.CONFIGURATION;

@Listeners({TestListener.class})
public class BaseTestCase implements IHookable {

    public static final String BASIC_URL = CONFIGURATION.getUrl();
    public static final String MOCK_PATH = CONFIGURATION.getValue("mockPath");
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseTestCase.class);

    public WebDriver driver;
    public WebDriverWait webDriverWait;
    protected HomePage homePage;
    protected EnvironmentRequestClient environmentRequestClient;

    @BeforeClass
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
        LoginPage loginPage = new LoginPage(driver, BASIC_URL).open();
        addCookies(driver);
        this.homePage = loginPage.login();
    }

    @AfterClass
    public void closeBrowser() {
        if (driver != null) {
            try {
                logout();
            } catch (Exception e) {
                LOGGER.warn("Cannot logout. Exception occured: {}", e.getMessage());
            }
            DelayUtils.sleep(5000);
            driver.quit();
        }
    }

    @Override
    public void run(IHookCallBack cb, ITestResult testResult) {
        cb.runTestMethod(testResult);
        if (CONFIGURATION.getCheckErrors().equals("true")) {
            try {
                SystemMessageContainer systemMessage = SystemMessageContainer.create(this.driver, new WebDriverWait(this.driver, Duration.ofSeconds(5)));
                List<String> errors = systemMessage.getErrors();
                errors.forEach(LOGGER::error);
                Assert.assertTrue(errors.isEmpty(), "Some errors occurred during the test. Please check logs for details.\n");
            } catch (Exception e) {
                Assert.fail("Page didn't load.");
            }
        }
    }

    private Cookie createCookie() {
        return new Cookie("i18nCurrentLocale", "en", BASIC_URL.split("//")[1].split(":")[0], "/", null, false, false);
    }

    public void addCookies(WebDriver driver) {
        boolean isWebRunner = Boolean.parseBoolean(CONFIGURATION.getValue("webRunner"));
        if (!isWebRunner) {
            driver.manage().addCookie(createCookie());
        }
    }

    private Map<String, Object> getPreferences() {
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.prompt_for_download", false);
        prefs.put("download.directory_upgrade", true);
        prefs.put("profile.default_content_settings.popups", 0);
        prefs.put("download.default_directory", CONFIGURATION.getDownloadDir());
        return prefs;
    }

    private ChromeOptions getAdditionalOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("start-maximized");
        options.addArguments("enable-automation");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-browser-side-navigation");
        options.addArguments("--disable-gpu");
        options.addArguments("--remote-allow-origins=*");
        options.setExperimentalOption("prefs", getPreferences());
        return options;
    }

    private void setWebDriver(ChromeOptions options) {
        boolean isLocally = CONFIGURATION.isLocally();

        if (!isLocally) {
            options.addArguments("--window-size=1920,1080");
            options.addArguments("--headless=new");
        }
    }

    public void startChromeDriver() {
        ChromeOptions options = getAdditionalOptions();
        setWebDriver(options);
        driver = WebDriverManager.chromedriver().capabilities(options).create();
    }

    public void startFirefoxDriver() {
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--width=1920");
        options.addArguments("--height=1080");
        if (!CONFIGURATION.isLocally()) {
            options.addArguments("--headless");
        }
        driver = WebDriverManager.firefoxdriver().capabilities(options).create();
        driver.manage().window().maximize();
    }

    protected RestAssuredConfig prepareRestAssureConfig() {
        return RestAssuredConfig.config()
                .objectMapperConfig(new ObjectMapperConfig().jackson2ObjectMapperFactory((clazz, s) -> JDK8ObjectMapper.getMapper()));
    }

    private void logout() {
        LoginPanel.create(driver, webDriverWait).open().logOut();
        try {
            driver.switchTo().alert().accept();
        } catch (NoAlertPresentException ignored) {
        }
    }

}
