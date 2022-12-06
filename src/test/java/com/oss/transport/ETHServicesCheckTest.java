package com.oss.transport;

import org.testng.annotations.Test;

import com.oss.serviceClient.Environment;
import com.oss.serviceClient.EnvironmentRequestClient;
import com.oss.transport.infrastructure.servicecheck.ServicesChecker;

import io.qameta.allure.Step;

import static com.oss.configuration.Configuration.CONFIGURATION;

public class ETHServicesCheckTest {

    public static final String BASIC_URL = CONFIGURATION.getUrl();
    private static final ServicesChecker SERVICES_CHECKER;

    static {
        Environment environment = Environment.createEnvironmentFromConfiguration();
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
}
