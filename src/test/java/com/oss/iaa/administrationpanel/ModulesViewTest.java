package com.oss.iaa.administrationpanel;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.administration.administrationpanel.LogsWizardPage;
import com.oss.pages.administration.administrationpanel.ModulesPage;

import io.qameta.allure.Description;

public class ModulesViewTest extends BaseTestCase {

    private static final String DOWNLOADED_LOGS_FILE = "*.zip";

    private ModulesPage modulesPage;

    @BeforeMethod
    public void goToModulesPage() {
        modulesPage = ModulesPage.goToModulesPage(driver);
    }

    @Test(priority = 1, testName = "Check Help button", description = "Check Help button in Modules table")
    @Description("Check Help button in Modules table")
    public void checkHelp() {
        Assert.assertFalse(modulesPage.isModulesTableEmpty());

        modulesPage.clickHelp();
        Assert.assertTrue(modulesPage.isTextInHelp());

        modulesPage.clickAccept();
        Assert.assertFalse(modulesPage.isModulesTableEmpty());
    }

    @Test(priority = 2, testName = "Refresh Modules Table", description = "Check Refresh button in Modules Table")
    @Description("Check Refresh button in Modules Table")
    public void checkRefreshInModulesTable() {
        Assert.assertFalse(modulesPage.isModulesTableEmpty());

        modulesPage.clickRefreshInModulesTable();

        Assert.assertFalse(modulesPage.isModulesTableEmpty());
    }

    @Test(priority = 3, testName = "Refresh Instance Table", description = "Check Refresh button in Instance Table")
    @Description("Check Refresh button in Instance Table")
    public void checkRefresh() {
        Assert.assertFalse(modulesPage.isInstancesTableEmpty());

        modulesPage.clickRefreshInInstanceTable();

        Assert.assertFalse(modulesPage.isInstancesTableEmpty());
    }

    @Parameters({"moduleName"})
    @Test(priority = 4, testName = "Search In Modules Table", description = "Search In Modules Table")
    @Description("Search In Modules Table")
    public void searchInModulesTable(
            @Optional("ASWeb") String moduleName
    ) {
        modulesPage.searchInModulesTable(moduleName);
        Assert.assertEquals(modulesPage.getFirstModuleNameFromModulesTable(), moduleName);

        modulesPage.selectFirstRowInModulesTable();
        Assert.assertEquals(modulesPage.getFirstModuleNameFromInstanceTable(), moduleName);
    }

    @Parameters({"portNumber"})
    @Test(priority = 5, testName = "Search in Instance Table - Ports", description = "Search in Instance Table - Ports")
    @Description("Search in Instance Table - Ports")
    public void searchInInstanceTablePorts(
            @Optional("25600") String portNumber
    ) {
        modulesPage.searchForPortsInInstanceTable(portNumber);
        Assert.assertTrue(modulesPage.getPortsFromFirstRowInInstanceTable().contains(portNumber));
    }

    @Parameters({"moduleName", "logFileName"})
    @Test(priority = 6, testName = "Download Logs", description = "Download Logs from subsystem")
    @Description("Download Logs from subsystem")
    public void downloadLogs(
            @Optional("ASWeb") String moduleName,
            @Optional("bookmark-manager-core.log") String logFileName

    ) {
        modulesPage.searchInModulesTable(moduleName);
        modulesPage.selectFirstRowInModulesTable();
        LogsWizardPage logsWizard = modulesPage.clickLogsButton();
        logsWizard.toggleNode(logsWizard.getPathToFileName(moduleName, logFileName));
        modulesPage = logsWizard.clickAcceptInLogsWizard();
        modulesPage.downloadLogFromNotifications();
        modulesPage.attachFileToReport(DOWNLOADED_LOGS_FILE);
        Assert.assertTrue(modulesPage.checkIfFileIsNotEmpty(DOWNLOADED_LOGS_FILE));
    }
}
