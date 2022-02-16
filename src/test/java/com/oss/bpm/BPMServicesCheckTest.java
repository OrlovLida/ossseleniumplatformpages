package com.oss.bpm;

import com.oss.transport.infrastructure.Environment;
import com.oss.transport.infrastructure.EnvironmentRequestClient;
import com.oss.transport.infrastructure.User;
import com.oss.transport.infrastructure.servicecheck.ServicesChecker;
import io.qameta.allure.Step;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static com.oss.configuration.Configuration.CONFIGURATION;

public class BPMServicesCheckTest {

    public static final String BASIC_URL = CONFIGURATION.getUrl();
    private static final ServicesChecker SERVICES_CHECKER;

    static {
        Environment environment = createEnvironment();
        EnvironmentRequestClient environmentRequestClient = new EnvironmentRequestClient(environment);
        SERVICES_CHECKER = new ServicesChecker(environmentRequestClient);
    }

    @Test(priority = 1)
    @Step("Check bpm-view")
    public void checkBPMView() {
        SERVICES_CHECKER.testHealth("bpm-view");
    }

    @Test(priority = 2)
    @Step("Check inventory-processes-core")
    public void checkInventoryProcesses() {
        SERVICES_CHECKER.testHealth("inventory-processes");
    }

    @Test(priority = 3)
    @Step("Check planning-core")
    public void checkPlanningCore() {
        SERVICES_CHECKER.testHealth("planning-core");
    }

    @Test(priority = 4)
    @Step("Check validation-results-core")
    public void checkValidationResultsCore() {
        SERVICES_CHECKER.testHealth("validation-results-core");
    }

    @Test(priority = 5)
    @Step("Check web-management")
    public void checkWebManagement() {
        SERVICES_CHECKER.testHealth("web-management");
    }

    @Test(priority = 6)
    @Step("Check inventory-view-core")
    public void checkInventoryViewCore() {
        SERVICES_CHECKER.testHealth("inventory-view-core");
    }

    @Test(priority = 7)
    @Step("Check attachment-manager-core")
    public void checkAttachmentManagerCore() {
        SERVICES_CHECKER.testHealth("attachment-manager-core");
    }

    @Test(priority = 8)
    @Step("Check attachment-manager-view")
    public void checkAttachmentManagerView() {
        SERVICES_CHECKER.testHealth("attachment-manager-view");
    }

    @Test(priority = 9)
    @Step("Check capacity-management-core")
    public void checkCapacityManagementCore() {
        SERVICES_CHECKER.testHealth("capacity-management-core");
    }

    @Test(priority = 10)
    @Step("Check physical-inventory-core")
    public void checkPhysicalInventoryCore() {
        SERVICES_CHECKER.testHealth("physical-inventory-core");
    }

    @Test(priority = 11)
    @Step("Check physical-inventory-view")
    public void checkPhysicalInventoryView() {
        SERVICES_CHECKER.testHealth("physical-inventory-view");
    }

    @Test(priority = 12)
    @Step("Check graphql-model")
    public void checkGraphQLModel() {
        SERVICES_CHECKER.testHealth("graphql-model");
    }

    @Test(priority = 13)
    @Step("Check authorization-service-core")
    public void checkAuthorizationServiceCore() {
        SERVICES_CHECKER.testHealth("authorization-service-core");
    }

    @Test(priority = 14)
    @Step("Check global-search-core")
    public void checkGlobalSearchCore() {
        SERVICES_CHECKER.testHealth("global-search-core");
    }

    //      TODO
//    @Test(priority = 5)
//    @Step("Check planning-view")
//    public void checkPlanningView() {
//        SERVICES_CHECKER.testHealth("planning-view");
//    }

    //    @Test(priority = 5)
//    @Step("Check inventory-processes-view")
//    public void checkPlanningView() {
//        SERVICES_CHECKER.testHealth("inventory-processes-view");
//    }

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
