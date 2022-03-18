package com.oss.bpm;

import com.oss.transport.infrastructure.Environment;
import com.oss.transport.infrastructure.EnvironmentRequestClient;
import com.oss.transport.infrastructure.User;
import com.oss.transport.infrastructure.servicecheck.ServicesChecker;
import io.qameta.allure.Step;
import org.testng.Assert;
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
        Assert.assertTrue(SERVICES_CHECKER.testHealthByApplicationBasePath("/rest/web/bpm"));
    }

    @Test(priority = 2)
    @Step("Check inventory-processes-core")
    public void checkInventoryProcesses() {
        Assert.assertTrue(SERVICES_CHECKER.testHealthByApplicationBasePath("/rest/inventory-processes"));
    }

    @Test(priority = 3)
    @Step("Check planning-core")
    public void checkPlanningCore() {
        Assert.assertTrue(SERVICES_CHECKER.testHealthByApplicationBasePath("/rest/inventory"));
    }

    @Test(priority = 4)
    @Step("Check validation-results-core")
    public void checkValidationResultsCore() {
        Assert.assertTrue(SERVICES_CHECKER.testHealthByApplicationBasePath("/rest/validation-results"));
    }

    @Test(priority = 5)
    @Step("Check web-management")
    public void checkWebManagement() {
        Assert.assertTrue(SERVICES_CHECKER.testHealthByApplicationBasePath("/rest/web/management"));
    }

    @Test(priority = 6)
    @Step("Check inventory-view-core")
    public void checkInventoryViewCore() {
        Assert.assertTrue(SERVICES_CHECKER.testHealthByApplicationBasePath("/rest/web/inventory"));
    }

    @Test(priority = 7)
    @Step("Check attachment-manager-core")
    public void checkAttachmentManagerCore() {
        Assert.assertTrue(SERVICES_CHECKER.testHealthByApplicationBasePath("/rest/web/attachmentmanagercore"));
    }

    @Test(priority = 8)
    @Step("Check attachment-manager-view")
    public void checkAttachmentManagerView() {
        Assert.assertTrue(SERVICES_CHECKER.testHealthByApplicationBasePath("/rest/web/attachmentmanager"));
    }

    @Test(priority = 9)
    @Step("Check capacity-management-core")
    public void checkCapacityManagementCore() {
        Assert.assertTrue(SERVICES_CHECKER.testHealthByApplicationBasePath("/rest/capacity-management"));
    }

    @Test(priority = 10)
    @Step("Check physical-inventory-core")
    public void checkPhysicalInventoryCore() {
        Assert.assertTrue(SERVICES_CHECKER.testHealthByApplicationBasePath("/rest/physical-inventory"));
    }

    @Test(priority = 11)
    @Step("Check physical-inventory-view")
    public void checkPhysicalInventoryView() {
        Assert.assertTrue(SERVICES_CHECKER.testHealthByApplicationBasePath("/rest/web/physical-inventory"));
    }

    @Test(priority = 12)
    @Step("Check graphql-model")
    public void checkGraphQLModel() {
        Assert.assertTrue(SERVICES_CHECKER.testHealthByApplicationBasePath("/rest/graphql-model"));
    }

    @Test(priority = 13)
    @Step("Check authorization-service-core")
    public void checkAuthorizationServiceCore() {
        Assert.assertTrue(SERVICES_CHECKER.testHealthByApplicationBasePath("/rest/authorization-service"));
    }

    @Test(priority = 14)
    @Step("Check global-search-core")
    public void checkGlobalSearchCore() {
        Assert.assertTrue(SERVICES_CHECKER.testHealthByApplicationBasePath("/rest/globalsearch"));
    }

    @Test(priority = 15)
    @Step("Check planning-view")
    public void checkPlanningView() {
        Assert.assertTrue(SERVICES_CHECKER.testHealthByApplicationBasePath("/rest/web/planning"));
    }

    @Test(priority = 16)
    @Step("Check inventory-processes-view")
    public void checkInventoryProcessesView() {
        Assert.assertTrue(SERVICES_CHECKER.testHealthByApplicationBasePath("/rest/web/inventory-processes"));
    }

    private static Environment createEnvironment() {
        try {
            URL url = new URL(BASIC_URL);
            String host = url.getHost();
            int port = url.getPort();
            String userName = CONFIGURATION.getValue("user");
            String pass = CONFIGURATION.getValue("password");
            User user = new User(userName, pass);
            return Environment.builder().withEnvironmentUrl(host).withEnvironmentPort(port).withUser(user).build();
        } catch (MalformedURLException exception) {
            throw new IllegalStateException(exception);
        }
    }

}
