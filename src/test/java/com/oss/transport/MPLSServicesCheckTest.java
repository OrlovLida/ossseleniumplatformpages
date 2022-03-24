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

public class MPLSServicesCheckTest {

    public static final String BASIC_URL = CONFIGURATION.getUrl();
    private static final ServicesChecker SERVICES_CHECKER;

    static {
        Environment environment = createEnvironment();
        EnvironmentRequestClient environmentRequestClient = new EnvironmentRequestClient(environment);
        SERVICES_CHECKER = new ServicesChecker(environmentRequestClient);
    }

    @Test(priority = 1)
    @Step("Check mpls-core")
    public void checkMplsCore() {
        SERVICES_CHECKER.testHealth("mpls-core");
    }

    @Test(priority = 2)
    @Step("Check mpls-view")
    public void checkMplsView() {
        SERVICES_CHECKER.testHealth("mpls-view");
    }

    @Test(priority = 3)
    @Step("Check inventory-view-core")
    public void checkInventoryViewCore() {
        SERVICES_CHECKER.testHealth("inventory-view-core");
    }

    @Test(priority = 4)
    @Step("Check transport-core")
    public void checkTransportCore() {
        SERVICES_CHECKER.testHealth("transport-core");
    }

    @Test(priority = 5)
    @Step("Check transport-view")
    public void checkTransportView() {
        SERVICES_CHECKER.testHealth("transport-view");
    }

    @Test(priority = 6)
    @Step("Check tp-service")
    public void checkTpService() {
        SERVICES_CHECKER.testHealth("tp-service");
    }

    @Test(priority = 7)
    @Step("Check ethernet-core")
    public void checkEthernetCore() {
        SERVICES_CHECKER.testHealth("ethernet-core");
    }

    @Test(priority = 8)
    @Step("Check ethernet-view")
    public void checkEthernetView() {
        SERVICES_CHECKER.testHealth("ethernet-view");
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
