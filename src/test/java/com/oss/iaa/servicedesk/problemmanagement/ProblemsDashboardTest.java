package com.oss.iaa.servicedesk.problemmanagement;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.iaa.servicedesk.issue.problem.ProblemDashboardPage;
import com.oss.pages.platform.NotificationWrapperPage;

import io.qameta.allure.Description;

public class ProblemsDashboardTest extends BaseTestCase {

    private ProblemDashboardPage problemDashboardPage;
    private NotificationWrapperPage notificationWrapperPage;

    private static final String PROBLEM_DOWNLOAD_FILE = "Problem*.xlsx";

    @BeforeMethod
    public void openProblemsDashboard() {
        problemDashboardPage = new ProblemDashboardPage(driver, webDriverWait).goToPage(driver, BASIC_URL);
    }

    @Test(priority = 1, testName = "Export from Problems Dashboard", description = "Export XLSX file from Problems Dashboard")
    @Description("Export XLSX file from Problems Dashboard")
    public void exportFromProblemsDashboard() {
        problemDashboardPage.exportFromDashboard(PROBLEM_DOWNLOAD_FILE);
        notificationWrapperPage = problemDashboardPage.openNotificationPanel();

        Assert.assertEquals(notificationWrapperPage.amountOfNotifications(), 0);
        Assert.assertTrue(problemDashboardPage.checkIfFileIsNotEmpty(PROBLEM_DOWNLOAD_FILE));
    }
}
