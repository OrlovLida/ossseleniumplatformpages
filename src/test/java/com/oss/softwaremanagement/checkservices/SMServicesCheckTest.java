package com.oss.softwaremanagement.checkservices;

import org.testng.annotations.Test;

import com.oss.serviceClient.Environment;
import com.oss.serviceClient.EnvironmentRequestClient;
import com.oss.transport.infrastructure.servicecheck.ServicesChecker;

import io.qameta.allure.Step;

/**
 * Created by Bartłomiej Jędrzejczyk on 2022-04-14
 */
public class SMServicesCheckTest {

    private static final ServicesChecker SERVICES_CHECKER;

    static {
        Environment environment = Environment.createEnvironmentFromConfiguration();
        EnvironmentRequestClient environmentRequestClient = new EnvironmentRequestClient(environment);
        SERVICES_CHECKER = new ServicesChecker(environmentRequestClient);
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
    @Step("Check software-management-export")
    public void checkSoftwareManagementExport() {
        SERVICES_CHECKER.testHealth("software-management-export");
    }

    @Test(priority = 4)
    @Step("Check software-management-core")
    public void checkSoftwareManagementCore() {
        SERVICES_CHECKER.testHealth("software-management-core");
    }

    @Test(priority = 5)
    @Step("Check software-management-dispatcher")
    public void checkSoftwareManagementDispatcher() {
        SERVICES_CHECKER.testHealth("software-management-dispatcher");
    }

    @Test(priority = 6)
    @Step("Check software-management-view")
    public void checkSoftwareManagementView() {
        SERVICES_CHECKER.testHealth("software-management-view");
    }

    @Test(priority = 7)
    @Step("Check software-management-loader")
    public void checkSoftwareManagementLoader() {
        SERVICES_CHECKER.testHealthByApplicationBasePath("/rest/mediation/software-management");
    }

    @Test(priority = 8)
    @Step("Check python interpreter")
    public void checkPythonInterpreter() {
        SERVICES_CHECKER.testHealth("cmtemplate-interpreter-python");
    }

    @Test(priority = 9)
    @Step("Check cmmediation-base")
    public void checkCMMediationBase() {
        SERVICES_CHECKER.testHealth("mediation-template");
    }

    @Test(priority = 10)
    @Step("Check mediation-repository")
    public void checkMediationRepository() {
        SERVICES_CHECKER.testHealth("mediation-repository");
    }

    @Test(priority = 11)
    @Step("Check mediation-repository-inventory")
    public void checkMediationRepositoryInventory() {
        SERVICES_CHECKER.testHealth("mediation-repository-inventory");
    }

    @Test(priority = 12)
    @Step("Check planning-core")
    public void checkPlanningCore() {
        SERVICES_CHECKER.testHealth("planning-core");
    }

    @Test(priority = 13)
    @Step("Check global-search-core")
    public void checkGlobalSearchCore() {
        SERVICES_CHECKER.testHealth("global-search-core");
    }

    @Test(priority = 14)
    @Step("Check web-management")
    public void checkWebManagement() {
        SERVICES_CHECKER.testHealth("web-management");
    }

    @Test(priority = 15)
    @Step("Check physical-inventory-core")
    public void checkPhysicalInventoryCore() {
        SERVICES_CHECKER.testHealth("physical-inventory-core");
    }

    @Test(priority = 16)
    @Step("Check logical-function-core")
    public void checkLogicalFunctionCore() {
        SERVICES_CHECKER.testHealth("logical-function-core");
    }

    @Test(priority = 17)
    @Step("Check tmf-catalog-core")
    public void checkTMFCatalogCore() {
        SERVICES_CHECKER.testHealth("tmf-catalog-core");
    }
}
