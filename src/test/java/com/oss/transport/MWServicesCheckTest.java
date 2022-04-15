package com.oss.transport;

import org.testng.annotations.Test;

import com.oss.transport.infrastructure.Environment;
import com.oss.transport.infrastructure.EnvironmentRequestClient;
import com.oss.transport.infrastructure.servicecheck.ServicesChecker;

import io.qameta.allure.Step;

import static com.oss.configuration.Configuration.CONFIGURATION;

public class MWServicesCheckTest {

    public static final String BASIC_URL = CONFIGURATION.getUrl();
    private static final ServicesChecker SERVICES_CHECKER;

    static {
        Environment environment = Environment.createEnvironmentFromConfiguration();
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
    @Step("Check web-management")
    public void checkWebManagement() {
        SERVICES_CHECKER.testHealth("web-management");
    }

    @Test(priority = 5)
    @Step("Check inventory-view-core")
    public void checkInventoryViewCore() {
        SERVICES_CHECKER.testHealth("inventory-view-core");
    }
}
