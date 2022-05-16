package com.oss.servicedesk.problemmanagement;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.platform.NotificationWrapperPage;
import com.oss.pages.servicedesk.issue.IssueDetailsPage;
import com.oss.pages.servicedesk.issue.problem.MyProblemsPage;
import com.oss.pages.servicedesk.issue.problem.ProblemDashboardPage;
import com.oss.pages.servicedesk.issue.wizard.SDWizardPage;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

import static com.oss.pages.servicedesk.ServiceDeskConstants.ID_ATTRIBUTE;
import static com.oss.pages.servicedesk.ServiceDeskConstants.PROBLEM_ISSUE_TYPE;

@Listeners({TestListener.class})
public class MyGroupProblemsTest extends BaseTestCase {
    private ProblemDashboardPage problemDashboardPage;
    private MyProblemsPage myProblemsPage;
    private NotificationWrapperPage notificationWrapperPage;
    private IssueDetailsPage issueDetailsPage;
    private SDWizardPage sdWizardPage;
    private String problemID;

    private static final String DOWNLOAD_FILE = "Selenium test*.csv";
    private static final String EXPORT_WIZARD_ID = "exportgui-mainview";
    private static final String DETAILS_WINDOW_ID = "_detailsWindow";
    private static final String SELENIUM_TEST_USER = "sd_seleniumtest";
    private static final String PROBLEM_DESCRIPTION = "Selenium Test Problem";

    @BeforeMethod
    public void goToTicketDashboardPage() {
        problemDashboardPage = new ProblemDashboardPage(driver, webDriverWait).goToPage(driver, BASIC_URL);
    }

    @Parameters({"MOIdentifier", "problemAssignee"})
    @Test(priority = 1, testName = "Create Problem", description = "Create Problem")
    @Description("Create Problem")
    public void createProblem(
            @Optional("CFS_Access_Product_Selenium_1") String MOIdentifier,
            @Optional("Tier 2 Mobile") String problemAssignee
    ) {
        sdWizardPage = problemDashboardPage.clickCreateProblem();
        sdWizardPage.createProblem(MOIdentifier, problemAssignee, PROBLEM_DESCRIPTION);
        Assert.assertTrue(problemDashboardPage.isProblemCreated(PROBLEM_DESCRIPTION));
        problemID = problemDashboardPage.getProblemIdWithProblemName(PROBLEM_DESCRIPTION);
    }

    @Test(priority = 2, testName = "Check My Group Problems", description = "Check My Group Problems")
    @Description("Check My Group Problems")
    public void checkMyGroupProblems() {
        myProblemsPage = new MyProblemsPage(driver, webDriverWait).openView(driver, BASIC_URL);
        myProblemsPage.filterByTextField(ID_ATTRIBUTE, problemID);
        myProblemsPage.getIdForNthTicketInTable(0);
        Assert.assertFalse(myProblemsPage.isIssueTableEmpty());
        Assert.assertEquals(problemID, myProblemsPage.getIdForNthTicketInTable(0));
    }

    @Test(priority = 3, testName = "Refresh MyGroup Problems", description = "Refresh MyGroup Problems")
    @Description("Refresh MyGroup Tickets")
    public void refreshMyGroupProblems() {
        myProblemsPage = new MyProblemsPage(driver, webDriverWait).openView(driver, BASIC_URL);
        if (!myProblemsPage.isIssueTableEmpty()) {
            int problemsInTable = myProblemsPage.countIssuesInTable();
            myProblemsPage.clickRefresh();
            int problemsAfterRefresh = myProblemsPage.countIssuesInTable();

            Assert.assertFalse(myProblemsPage.isIssueTableEmpty());
            Assert.assertTrue(problemsInTable >= problemsAfterRefresh);
        } else {
            Assert.fail("No data in table - cannot check refresh function");
        }
    }

    @Test(priority = 4, testName = "Export from My Group Problems", description = "Export from My Group Problems")
    @Description("Export from My Group Problems")
    public void exportFromMyGroupProblems() {
        myProblemsPage = new MyProblemsPage(driver, webDriverWait).openView(driver, BASIC_URL);
        try {
            myProblemsPage.exportFromSearchViewTable(EXPORT_WIZARD_ID);
        } catch (RuntimeException e) {
            Assert.fail(e.getMessage());
        }
        notificationWrapperPage = new NotificationWrapperPage(driver);

        Assert.assertEquals(notificationWrapperPage.amountOfNotifications(), 0);
        Assert.assertTrue(myProblemsPage.checkIfFileIsNotEmpty(DOWNLOAD_FILE));
    }

    @Test(priority = 5, testName = "My Problems Check", description = "Change assignee and check if problem is visible in My Problems View")
    @Description("Change assignee and check if problem is visible in My Problems View")
    public void myProblemsCheck() {
        issueDetailsPage = problemDashboardPage.openIssueDetailsView(problemID, BASIC_URL, PROBLEM_ISSUE_TYPE);
        issueDetailsPage.maximizeWindow(DETAILS_WINDOW_ID);
        issueDetailsPage.changeIssueAssignee(SELENIUM_TEST_USER);
        myProblemsPage = new MyProblemsPage(driver, webDriverWait).openView(driver, BASIC_URL);
        Assert.assertFalse(myProblemsPage.isIssueTableEmpty());

        myProblemsPage.filterByTextField(ID_ATTRIBUTE, problemID);
        Assert.assertFalse(myProblemsPage.isIssueTableEmpty());
    }
}

