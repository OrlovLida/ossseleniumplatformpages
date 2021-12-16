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
    private static final ServicesChecker SERVICES_CHECKER;

    static {
        Environment environment = createEnvironment();
        EnvironmentRequestClient environmentRequestClient = new EnvironmentRequestClient(environment);
        SERVICES_CHECKER = new ServicesChecker(environmentRequestClient);
    }

    @Test(priority = 1)
    @Step("Check ethernet-core")
    public void checkEthernetCore() {
        SERVICES_CHECKER.testHealth("ethernet-core");
    }

    @Test(priority = 2)
    @Step("Check ethernet-view")
    public void checkEthernetView() {
        SERVICES_CHECKER.testHealth("ethernet-view");
    }

    @Test(priority = 3)
    @Step("Check web-management")
    public void checkWebManagement() {
        SERVICES_CHECKER.testHealth("web-management");
    }

    @Test(priority = 4)
    @Step("Check inventory-view-core")
    public void checkInventoryViewCore() {
        SERVICES_CHECKER.testHealth("inventory-view-core");
    }

    @Test(priority = 5)
    @Step("Check logical-function-core")
    public void checkLogicalFunctionCore() {
        SERVICES_CHECKER.testHealth("logical-function-core");
    }

    @Test(priority = 6)
    @Step("Check capacity-management-core")
    public void checkCapacityManagementCore() {
        SERVICES_CHECKER.testHealth("capacity-management-core");
    }

    @Test(priority = 7)
    @Step("Check tp-service")
    public void checkTpService() {
        SERVICES_CHECKER.testHealth("tp-service");
    }

    private static Environment createEnvironment() {
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
