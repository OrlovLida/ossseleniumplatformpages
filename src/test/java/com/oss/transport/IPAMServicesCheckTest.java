package com.oss.transport;

import org.testng.annotations.Test;

import com.oss.serviceClient.Environment;
import com.oss.serviceClient.EnvironmentRequestClient;
import com.oss.transport.infrastructure.servicecheck.ServicesChecker;

import io.qameta.allure.Step;

import static com.oss.configuration.Configuration.CONFIGURATION;

public class IPAMServicesCheckTest {

    public static final String BASIC_URL = CONFIGURATION.getUrl();
    private static final ServicesChecker SERVICES_CHECKER;

    static {
        Environment environment = Environment.createEnvironmentFromConfiguration();
        EnvironmentRequestClient environmentRequestClient = new EnvironmentRequestClient(environment);
        SERVICES_CHECKER = new ServicesChecker(environmentRequestClient);
    }

    @Test(priority = 1)
    @Step("Check ipaddress-management")
    public void checkIPAddressManagement() {
        SERVICES_CHECKER.testHealth("ipaddress-management");
    }

    @Test(priority = 2)
    @Step("Check inventory-view-core")
    public void checkInventoryViewCore() {
        SERVICES_CHECKER.testHealth("inventory-view-core");
    }

    @Test(priority = 3)
    @Step("Check transport-view")
    public void checkTransportView() {
        SERVICES_CHECKER.testHealth("transport-view");
    }
}
