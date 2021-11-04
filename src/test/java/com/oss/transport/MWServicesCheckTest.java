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

public class MWServicesCheckTest {

    public static final String BASIC_URL = CONFIGURATION.getUrl();
    private static final ServicesChecker SERVICES_CHECKER;

    static {
        Environment environment = createEnvironment();
        EnvironmentRequestClient environmentRequestClient = new EnvironmentRequestClient(environment);
        SERVICES_CHECKER = new ServicesChecker(environmentRequestClient);
    }

    @Test(priority = 1)
    @Step("Check microwave-core")
    public void checkMicrowaveCore() {
        SERVICES_CHECKER.testHealth("microwave-core");
    }

    @Test(priority = 2)
    @Step("Check microwave-view")
    public void checkMicrowaveView() {
        SERVICES_CHECKER.testHealth("microwave-view");
    }

    @Test(priority = 3)
    @Step("Check attachment-manager-core")
    public void checkAttachmentManagerCore() {
        SERVICES_CHECKER.testHealth("attachment-manager-core");
    }

    @Test(priority = 4)
    @Step("Check web-management-core")
    public void checkWebManagementCore() {
        SERVICES_CHECKER.testHealth("web-management-core");
    }

    @Test(priority = 5)
    @Step("Check inventory-view-core")
    public void checkInventoryViewCore() {
        SERVICES_CHECKER.testHealth("inventory-view-core");
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
