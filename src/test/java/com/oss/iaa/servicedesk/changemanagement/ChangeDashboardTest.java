package com.oss.iaa.servicedesk.changemanagement;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.comarch.oss.web.pages.NotificationWrapperPage;
import com.oss.BaseTestCase;
import com.oss.pages.iaa.servicedesk.changemanagement.ChangeDashboardPage;

import io.qameta.allure.Description;

public class ChangeDashboardTest extends BaseTestCase {

    private ChangeDashboardPage changeDashboardPage;
    private NotificationWrapperPage notificationWrapperPage;

    private static final String CHANGES_EXPORT_FILE = "ChangeRequest*.xlsx";

    @BeforeMethod
    public void openChangeDashboardPage() {
        changeDashboardPage = new ChangeDashboardPage(driver, webDriverWait).goToPage(driver, BASIC_URL);
    }

    @Test(priority = 1, testName = "Export from Changes Dashboard", description = "Export XLSX file from Changes Dashboard")
    @Description("Export XLSX file from Changes Dashboard")
    public void exportFromChangesDashboard() {
        changeDashboardPage.exportFromDashboard(CHANGES_EXPORT_FILE);
        notificationWrapperPage = changeDashboardPage.openNotificationPanel();

        Assert.assertEquals(notificationWrapperPage.amountOfNotifications(), 0);
        Assert.assertTrue(changeDashboardPage.checkIfFileIsNotEmpty(CHANGES_EXPORT_FILE));
    }
}
