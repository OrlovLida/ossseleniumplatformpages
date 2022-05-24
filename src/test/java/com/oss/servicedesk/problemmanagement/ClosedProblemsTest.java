package com.oss.servicedesk.problemmanagement;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.platform.NotificationWrapperPage;
import com.oss.pages.servicedesk.issue.IssueDetailsPage;
import com.oss.pages.servicedesk.issue.problem.ClosedProblemsPage;
import com.oss.pages.servicedesk.issue.problem.ProblemDashboardPage;
import com.oss.pages.servicedesk.issue.wizard.SDWizardPage;

import io.qameta.allure.Description;

import static com.oss.pages.servicedesk.ServiceDeskConstants.DOWNLOAD_FILE;
import static com.oss.pages.servicedesk.ServiceDeskConstants.EXPORT_WIZARD_ID;
import static com.oss.pages.servicedesk.ServiceDeskConstants.ID_ATTRIBUTE;
import static com.oss.pages.servicedesk.ServiceDeskConstants.PROBLEM_ISSUE_TYPE;

public class ClosedProblemsTest extends BaseTestCase {

    private ProblemDashboardPage problemDashboardPage;
    private SDWizardPage sdWizardPage;
    private IssueDetailsPage issueDetailsPage;
    private ClosedProblemsPage closedProblemsPage;
    private NotificationWrapperPage notificationWrapperPage;
    private String problemID;
    private static final String STATUS_IN_PROGRESS = "In Progress";
    private static final String STATUS_RESOLVED = "Resolved";
    private static final String STATUS_CLOSED = "Closed";
    private static final String PROBLEM_DESCRIPTION = "Selenium Test - Closed Problem";

    @BeforeClass
    public void goToProblemDashboard() {
        problemDashboardPage = new ProblemDashboardPage(driver, webDriverWait).goToPage(driver, BASIC_URL);
    }

    @Parameters({"MOIdentifier", "problemAssignee"})
    @Test(priority = 1, testName = "Close Problem", description = "Create problem and go through all necessary steps to close it")
    @Description("Create problem and go through all necessary steps to close it")
    public void createAndCloseProblem(
            @Optional("CFS_Access_Product_Selenium_1") String MOIdentifier,
            @Optional("Tier 2 Mobile") String problemAssignee
    ) {
        sdWizardPage = problemDashboardPage.clickCreateProblem();
        sdWizardPage.createProblem(MOIdentifier, problemAssignee, PROBLEM_DESCRIPTION);
        problemID = problemDashboardPage.getProblemIdWithProblemName(PROBLEM_DESCRIPTION);
        issueDetailsPage = problemDashboardPage.openIssueDetailsView(problemID, BASIC_URL, PROBLEM_ISSUE_TYPE);
        issueDetailsPage.changeIssueStatus(STATUS_IN_PROGRESS);
        issueDetailsPage.changeIssueStatus(STATUS_RESOLVED);
        issueDetailsPage.changeIssueStatus(STATUS_CLOSED);
        Assert.assertEquals(issueDetailsPage.checkIssueStatus(), STATUS_CLOSED);
    }

    @Test(priority = 2, testName = "Check Closed Problems View", description = "Refresh, search and check if problem is shown in the closed problems table")
    @Description("Refresh, search and check if problem is shown in the closed problems table")
    public void checkClosedProblemsView() {
        closedProblemsPage = new ClosedProblemsPage(driver, webDriverWait).openView(driver, BASIC_URL);
        closedProblemsPage.clickRefresh();
        closedProblemsPage.filterByTextField(ID_ATTRIBUTE, problemID);
        Assert.assertEquals(closedProblemsPage.getIdForNthTicketInTable(0), problemID);
    }

    @Test(priority = 3, testName = "Export Closed Problems", description = "Export Closed Problems")
    @Description("Export Closed Problems")
    public void ExportClosedProblems() {
        closedProblemsPage = new ClosedProblemsPage(driver, webDriverWait).openView(driver, BASIC_URL);
        try {
            closedProblemsPage.exportFromSearchViewTable(EXPORT_WIZARD_ID);
        } catch (RuntimeException e) {
            Assert.fail(e.getMessage());
        }
        notificationWrapperPage = new NotificationWrapperPage(driver);

        Assert.assertEquals(notificationWrapperPage.amountOfNotifications(), 0);
        Assert.assertTrue(closedProblemsPage.checkIfFileIsNotEmpty(DOWNLOAD_FILE));
    }
}

