package com.oss.transport;

import com.oss.transport.infrastructure.Environment;
import com.oss.transport.infrastructure.EnvironmentRequestClient;
import com.oss.transport.infrastructure.User;
import com.oss.transport.infrastructure.servicecheck.ServicesChecker;
import io.qameta.allure.Step;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static com.oss.configuration.Configuration.CONFIGURATION;

public class ETHServicesCheckTest {

    public static final String BASIC_URL = CONFIGURATION.getUrl();

    @Test(priority = 1)
    @Step("Check ethernet-core")
    public void checkEthernetCore() {
        Environment environment = createEnvironment();
        EnvironmentRequestClient environmentRequestClient = new EnvironmentRequestClient(environment);
        ServicesChecker sc = new ServicesChecker(environmentRequestClient);
        sc.testHealth("ethernet-core");
    }

    @Test(priority = 2)
    @Step("Check ethernet-view")
    public void checkEthernetView() {
        Environment environment = createEnvironment();
        EnvironmentRequestClient environmentRequestClient = new EnvironmentRequestClient(environment);
        ServicesChecker sc = new ServicesChecker(environmentRequestClient);
        sc.testHealth("ethernet-view");
    }

    @Test(priority = 3)
    @Step("Check start-dashboard-view")
    public void checkStartDashboardView() {
        Environment environment = createEnvironment();
        EnvironmentRequestClient environmentRequestClient = new EnvironmentRequestClient(environment);
        ServicesChecker sc = new ServicesChecker(environmentRequestClient);
        sc.testHealth("start-dashboard-view");
    }

    @Test(priority = 4)
    @Step("Check web-management")
    public void checkWebManagement() {
        Environment environment = createEnvironment();
        EnvironmentRequestClient environmentRequestClient = new EnvironmentRequestClient(environment);
        ServicesChecker sc = new ServicesChecker(environmentRequestClient);
        sc.testHealth("web-management");
    }

    @Test(priority = 5)
    @Step("Check not existing")
    public void checkNotExisting() {
        Environment environment = createEnvironment();
        EnvironmentRequestClient environmentRequestClient = new EnvironmentRequestClient(environment);
        ServicesChecker sc = new ServicesChecker(environmentRequestClient);
        sc.testHealth("not-existing");
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
}
