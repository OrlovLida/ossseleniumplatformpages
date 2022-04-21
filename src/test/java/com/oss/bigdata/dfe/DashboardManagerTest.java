package com.oss.bigdata.dfe;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.administration.DashboardManagerPage;
import com.oss.pages.administration.DashboardPage;
import com.oss.pages.administration.managerwizards.DashboardWizardPage;

import io.qameta.allure.Description;

public class DashboardManagerTest extends BaseTestCase {

    private static final String DASHBOARD_NAME = "Test Selenium";

    private DashboardManagerPage dashboardManagerPage;
    private DashboardWizardPage dashboardWizardPage;
    private DashboardPage dashboardPage;

    @BeforeMethod
    public void goToBookmarkManagerPage() {
        dashboardManagerPage = DashboardManagerPage.goToPage(driver, BASIC_URL);
    }

    @Test(priority = 1, testName = "Creating Dashboard", description = "Creating Dashboard")
    @Description("Creating Dashboard")
    public void createDashboard(){
        dashboardManagerPage.clickAddCustomDashboard();
        dashboardWizardPage = new DashboardWizardPage(driver, webDriverWait);
        dashboardWizardPage.fillDashboardName(DASHBOARD_NAME);
        dashboardWizardPage.clickSave();
        dashboardManagerPage.searchForDashboard(DASHBOARD_NAME);
        Assert.assertTrue(dashboardManagerPage.isAnyDashboardInList());
    }

    @Test(priority = 2, testName = "Editing Dashboard", description = "Editing Dashboard")
    @Description("Editing Dashboard")
    public void editDashboard(){
        dashboardPage = dashboardManagerPage.openDashboard(DASHBOARD_NAME);
        dashboardPage.clickEditCustomDashboardButton();
        dashboardPage.chooseWidgetType("Indicators View Widget");
        dashboardPage.fillWidgetTitle("Test Selenium");
        dashboardPage.clickAddWidgetButton();
        dashboardPage.clickSaveButton();
     }

    @Test(priority = 3, testName = "Delete Dashboard", description = "Delete Dashboard")
    @Description("Delete Dashboard")
    public void deleteDashboard(){
        dashboardManagerPage.clickDeleteDashboard(DASHBOARD_NAME);
        Assert.assertTrue(dashboardManagerPage.isDashboardDeleted(DASHBOARD_NAME));
    }
}
