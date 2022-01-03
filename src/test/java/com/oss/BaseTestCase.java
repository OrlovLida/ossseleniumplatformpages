package com.oss;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
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
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.HomePage;
import com.oss.pages.platform.LoginPage;
import com.oss.transport.infrastructure.Environment;
import com.oss.transport.infrastructure.EnvironmentRequestClient;
import com.oss.transport.infrastructure.User;
import com.oss.utils.TestListener;

import static com.oss.configuration.Configuration.CONFIGURATION;

@Listeners({TestListener.class})
public class BaseTestCase implements IHookable {

    public static final String BASIC_URL = CONFIGURATION.getUrl();
    public static final String MOCK_PATH = CONFIGURATION.getValue("mockPath");

    public WebDriver driver;
    public WebDriverWait webDriverWait;
    protected HomePage homePage;
    protected EnvironmentRequestClient environmentRequestClient;

    @BeforeClass
    public void openBrowser() {
        RestAssured.config = prepareRestAssureConfig();
        Environment environment = createEnvironment();
        environmentRequestClient = new EnvironmentRequestClient(environment);
        if (CONFIGURATION.getDriver().equals("chrome")) {
            startChromeDriver();
        } else {
            startFirefoxDriver();
        }
        webDriverWait = new WebDriverWait(driver, 50);
        LoginPage loginPage = new LoginPage(driver, BASIC_URL).open();
        addCookies(driver);
        this.homePage = loginPage.login();
    }

    @AfterClass
    public void closeBrowser() {
        if (driver != null) {
            DelayUtils.sleep(5000);
            driver.close();
            driver.quit();
        }
    }

    @Override
    public void run(IHookCallBack cb, ITestResult testResult) {
        cb.runTestMethod(testResult);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(this.driver, new WebDriverWait(this.driver, 5));
        Assert.assertFalse(systemMessage.isErrorDisplayed(false), "Some errors occurred during the test. Please check logs for details.\n");
    }

    private Cookie createCookie() {
        return new Cookie("i18nCurrentLocale", "en", BASIC_URL.split("//")[1].split(":")[0], "/", null, false, false);
    }

    private void addCookies(WebDriver driver) {
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
        options.setExperimentalOption("prefs", getPreferences());

        return options;
    }

    private void setWebDriver(ChromeOptions options) {
        boolean isLocally = Boolean.parseBoolean(CONFIGURATION.getValue("locally"));
        boolean isWebRunner = Boolean.parseBoolean(CONFIGURATION.getValue("webRunner"));

        if (isLocally) {
            System.setProperty("webdriver.chrome.driver", CONFIGURATION.getValue("chromeDriverPath"));
            options.addArguments("start-maximized");
        } else if (isWebRunner) {
            System.setProperty("webdriver.chrome.driver", CONFIGURATION.getValue("chromeDriverWebRunner"));
            options.addArguments("--window-size=1920,1080");
            options.addArguments("--headless");
        } else {
            System.setProperty("webdriver.chrome.driver", CONFIGURATION.getValue("chromeDriverLinuxPath"));
            options.addArguments("--window-size=1920,1080");
            options.addArguments("--headless");
        }
    }

    private void startChromeDriver() {
        ChromeOptions options = getAdditionalOptions();
        setWebDriver(options);
        driver = new ChromeDriver(options);
    }

    private void startFirefoxDriver() {
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--width=1920");
        options.addArguments("--height=1080");
        if (CONFIGURATION.getValue("locally").equals("true")) {
            System.setProperty("webdriver.gecko.driver", CONFIGURATION.getValue("geckoDriverPath"));
        } else {
            System.setProperty("webdriver.gecko.driver", CONFIGURATION.getValue("geckoDriverLinuxPath"));
            options.addArguments("--headless");
        }
        driver = new FirefoxDriver(options);
        driver.manage().window().maximize();
    }

    private Environment createEnvironment() {
        try {
            URL url = new URL(BASIC_URL);
            String host = url.getHost();
            int port = url.getPort();
            String userName = CONFIGURATION.getValue("user");
            String pass = CONFIGURATION.getValue("password");
            User user = new User(userName, pass);
            return Environment.builder()
                    .withEnvironmentUrl(host)
                    .withEnvironmentPort(port)
                    .withUser(user)
                    .build();
        } catch (MalformedURLException exception) {
            throw new IllegalStateException(exception);
        }
    }

    protected RestAssuredConfig prepareRestAssureConfig() {
        return RestAssuredConfig.config()
                .objectMapperConfig(new ObjectMapperConfig().jackson2ObjectMapperFactory((clazz, s) -> JDK8ObjectMapper.getMapper()));
    }

}
