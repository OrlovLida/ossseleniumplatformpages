package com.oss.cmTemplate.checkservices;

import java.net.MalformedURLException;
import java.net.URL;

import org.testng.annotations.Test;

import com.oss.transport.infrastructure.Environment;
import com.oss.transport.infrastructure.EnvironmentRequestClient;
import com.oss.transport.infrastructure.User;
import com.oss.transport.infrastructure.servicecheck.ServicesChecker;

import io.qameta.allure.Step;

import static com.oss.configuration.Configuration.CONFIGURATION;

/**
 * Created by Bartłomiej Jędrzejczyk on 2022-04-14
 */
public class CMServicesCheckTest {

    private static final ServicesChecker SERVICES_CHECKER;

    static {
        Environment environment = createEnvironment();
        EnvironmentRequestClient environmentRequestClient = new EnvironmentRequestClient(environment);
        SERVICES_CHECKER = new ServicesChecker(environmentRequestClient);
    }

    private static Environment createEnvironment() {
        try {
            URL url = new URL(CONFIGURATION.getUrl());
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

    @Test(priority = 1)
    @Step("Check template-filler-core")
    public void checkTemplateFillerCore() {
        SERVICES_CHECKER.testHealth("template-filler");
    }

    @Test(priority = 2)
    @Step("Check template-filler-executor")
    public void checkTemplateFillerExecutor() {
        SERVICES_CHECKER.testHealth("template-filler-executor");
    }

    @Test(priority = 3)
    @Step("Check template-filler-synchronizer")
    public void checkTemplateFillerSynchronizer() {
        SERVICES_CHECKER.testHealth("template-filler-synchronizer");
    }

    @Test(priority = 4)
    @Step("Check template-filler-view")
    public void checkTemplateFillerView() {
        SERVICES_CHECKER.testHealth("template-filler-view");
    }

    @Test(priority = 5)
    @Step("Check python interpreter")
    public void checkPythonInterpreter() {
        SERVICES_CHECKER.testHealth("cmtemplate-interpreter-python");
    }

    @Test(priority = 6)
    @Step("Check cmmediation-base")
    public void checkCMMediationBase() {
        SERVICES_CHECKER.testHealth("mediation-template");
    }

    @Test(priority = 7)
    @Step("Check mediation-repository")
    public void checkMediationRepository() {
        SERVICES_CHECKER.testHealth("mediation-repository");
    }

    @Test(priority = 8)
    @Step("Check mediation-repository-inventory")
    public void checkMediationRepositoryInventory() {
        SERVICES_CHECKER.testHealth("mediation-repository-inventory");
    }

    @Test(priority = 9)
    @Step("Check planning-core")
    public void checkPlanningCore() {
        SERVICES_CHECKER.testHealth("planning-core");
    }

    @Test(priority = 10)
    @Step("Check global-search-core")
    public void checkGlobalSearchCore() {
        SERVICES_CHECKER.testHealth("global-search-core");
    }

    @Test(priority = 11)
    @Step("Check scheduler-service-core")
    public void checkSchedulerServiceCore() {
        SERVICES_CHECKER.testHealth("scheduler-service-core");
    }

    @Test(priority = 12)
    @Step("Check web-management")
    public void checkWebManagement() {
        SERVICES_CHECKER.testHealth("web-management");
    }

    @Test(priority = 13)
    @Step("Check physical-inventory-core")
    public void checkPhysicalInventoryCore() {
        SERVICES_CHECKER.testHealth("physical-inventory-core");
    }

    @Test(priority = 14)
    @Step("Check resource-catalog-core")
    public void checkResourceCatalogCore() {
        SERVICES_CHECKER.testHealth("resource-catalog-core");
    }
}
