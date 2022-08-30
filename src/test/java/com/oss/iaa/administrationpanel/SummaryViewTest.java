package com.oss.iaa.administrationpanel;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.administration.administrationpanel.SummaryViewPage;

import io.qameta.allure.Description;

public class SummaryViewTest extends BaseTestCase {

    private final static String APM_URL_SUFFIX = "/apm";
    private final static String KEYCLOAK_URL_SUFFIX = "/auth";

    private SummaryViewPage summaryViewPage;

    @BeforeMethod
    public void goToAdminPanelSummaryPage() {
        summaryViewPage = SummaryViewPage.goToPage(driver);
    }

    @Parameters("environmentName")
    @Test(priority = 1, testName = "Check Environment", description = "Check Environment")
    @Description("Check Environment")
    public void checkEnvironment(
            @Optional("IAA") String environmentName
    ) {
        Assert.assertEquals(summaryViewPage.getValueFromPanel("Environment Name"), environmentName);
    }

    @Parameters("versionName")
    @Test(priority = 2, testName = "Check Version", description = "Check Version")
    @Description("Check Version")
    public void checkVersion(
            @Optional("RC") String versionName
    ) {
        Assert.assertEquals(summaryViewPage.getValueFromPanel("Version"), versionName);
    }

    @Test(priority = 3, testName = "Check APM Link", description = "Check APM Link")
    @Description("Check APM Link")
    public void checkApmLink() {
        summaryViewPage.clickLinkFromPanel("APM");
        Assert.assertTrue(summaryViewPage.isChosenUrlOpen(APM_URL_SUFFIX));
    }

    @Test(priority = 4, testName = "Check Keycloak Link", description = "Check Keycloak Link")
    @Description("Check Keycloak Link")
    public void checkKeycloakLink() {
        summaryViewPage.clickLinkFromPanel("Keycloak");
        Assert.assertTrue(summaryViewPage.isChosenUrlOpen(KEYCLOAK_URL_SUFFIX));
    }

    @Test(priority = 5, testName = "Check Help button in Hosts Table", description = "Check Help button in Hosts Table")
    @Description("Check Help button in Hosts Table")
    public void checkHelpButton() {
        summaryViewPage.clickHelp();
        Assert.assertTrue(summaryViewPage.isTextInHelp());
        summaryViewPage.clickAccept();
        Assert.assertFalse(summaryViewPage.isHostsTableEmpty());
    }

    @Test(priority = 6, testName = "Check Refresh button in Hosts table", description = "Check Refresh button in Hosts table")
    @Description("Check Refresh button in Hosts table")
    public void checkRefreshHosts() {
        Assert.assertFalse(summaryViewPage.isHostsTableEmpty());
        summaryViewPage.clickRefreshInHostsTable();
        Assert.assertFalse(summaryViewPage.isHostsTableEmpty());
    }

    @Parameters("columnLabel")
    @Test(priority = 7, testName = "Change columns order", description = "Change columns order")
    @Description("Change columns order")
    public void changeColumnsOrder(
            @Optional("Used Memory [MB]") String columnLabel
    ) {
        summaryViewPage.changeFirstColumnInHostsTable(columnLabel);
        Assert.assertEquals(summaryViewPage.getFirstColumnLabelInHostsTable(), columnLabel);
    }
}