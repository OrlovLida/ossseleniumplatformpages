package com.oss.iaa.servicedesk.problemmanagement;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.comarch.oss.web.pages.NotificationWrapperPage;
import com.oss.BaseTestCase;
import com.oss.pages.iaa.servicedesk.issue.IssueDetailsPage;
import com.oss.pages.iaa.servicedesk.issue.MoreDetailsPage;
import com.oss.pages.iaa.servicedesk.issue.problem.ClosedProblemsPage;
import com.oss.pages.iaa.servicedesk.issue.problem.MyGroupProblemsPage;
import com.oss.pages.iaa.servicedesk.issue.problem.MyProblemsPage;
import com.oss.pages.iaa.servicedesk.issue.problem.ProblemDashboardPage;
import com.oss.pages.iaa.servicedesk.issue.problem.ProblemSearchPage;
import com.oss.pages.iaa.servicedesk.issue.tabs.AffectedTab;
import com.oss.pages.iaa.servicedesk.issue.tabs.AttachmentsTab;
import com.oss.pages.iaa.servicedesk.issue.tabs.DescriptionTab;
import com.oss.pages.iaa.servicedesk.issue.tabs.ExternalTab;
import com.oss.pages.iaa.servicedesk.issue.tabs.MessagesTab;
import com.oss.pages.iaa.servicedesk.issue.tabs.OverviewTab;
import com.oss.pages.iaa.servicedesk.issue.tabs.ParticipantsTab;
import com.oss.pages.iaa.servicedesk.issue.tabs.ProblemSolutionTab;
import com.oss.pages.iaa.servicedesk.issue.tabs.RelatedChangesTab;
import com.oss.pages.iaa.servicedesk.issue.tabs.RelatedProblemsTab;
import com.oss.pages.iaa.servicedesk.issue.tabs.RelatedTicketsTab;
import com.oss.pages.iaa.servicedesk.issue.tabs.RootCausesTab;
import com.oss.pages.iaa.servicedesk.issue.wizard.ExternalPromptPage;
import com.oss.pages.iaa.servicedesk.issue.wizard.SDWizardPage;

import io.qameta.allure.Description;

import static com.oss.pages.iaa.servicedesk.BaseSDPage.DATE_TIME_FORMATTER;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.COMBOBOX_LINK_PROBLEM_ID;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.CSV_FILE;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.DETAILS_TABS_CONTAINER_ID;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.DOWNLOAD_FILE;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.EXPORT_WIZARD_ID;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.EXTERNAL_TYPE;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.FILE_TO_UPLOAD_PATH;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.ID_ATTRIBUTE;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.INTERNAL_TYPE;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.PROBLEM_ISSUE_TYPE;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.SOURCE_TYPE;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.TABS_WIDGET_ID;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.TYPE_COMMENT;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.USER_NAME;

public class CreateProblemAndProblemSearchTest extends BaseTestCase {

    private ProblemDashboardPage problemDashboardPage;
    private SDWizardPage sdWizardPage;
    private IssueDetailsPage issueDetailsPage;
    private OverviewTab problemOverviewTab;
    private String problemId;

    private MyProblemsPage myProblemsPage;
    private MyGroupProblemsPage myGroupProblemsPage;
    private ProblemSearchPage problemSearchPage;
    private NotificationWrapperPage notificationWrapperPage;
    private ClosedProblemsPage closedProblemsPage;

    private static final String PROBLEM_NAME_DESCRIPTION_ID = "TT_WIZARD_INPUT_PROBLEM_NAME_DESCRIPTION";
    private static final String PROBLEM_NAME_DESCRIPTION_TXT = "Selenium test Problem " + LocalDateTime.now().format(DATE_TIME_FORMATTER);
    private static final String SEVERITY_COMBOBOX_ID = "TT_WIZARD_INPUT_SEVERITY_LABEL";
    private static final String PROBLEM_SEVERITY = "Critical";
    private static final String ASSIGNEE_SEARCH_ID = "TT_WIZARD_INPUT_ASSIGNEE_LABEL";
    private static final String STATUS_IN_PROGRESS = "In Progress";
    private static final String STATUS_CLOSED = "Closed";

    @BeforeMethod
    public void goToProblemDashboardPage(Method method) {
        problemDashboardPage = new ProblemDashboardPage(driver, webDriverWait).goToPage(driver, BASIC_URL);
        if (problemId != null) {
            issueDetailsPage = problemDashboardPage.openIssueDetailsView(problemId, BASIC_URL, PROBLEM_ISSUE_TYPE);
        } else if (!method.getName().equals("createProblem")) {
            Assert.fail("Problem has not been created.");
        }
    }

    @Parameters({"MOIdentifier", "ProblemAssignee"})
    @Test(priority = 1, testName = "Create Problem", description = "Create Problem")
    @Description("Create Problem")
    public void createProblem(
            @Optional("TEST_MO") String MOIdentifier,
            @Optional("sd_seleniumtest") String ProblemAssignee
    ) {
        sdWizardPage = problemDashboardPage.clickCreateProblem();
        sdWizardPage.getMoStep().enterTextIntoMOIdentifierField(MOIdentifier);
        sdWizardPage.getMoStep().selectRowInMOTable("0");
        sdWizardPage.clickNextButtonInWizard();
        sdWizardPage.insertValueToComponent(PROBLEM_NAME_DESCRIPTION_TXT, PROBLEM_NAME_DESCRIPTION_ID);
        sdWizardPage.insertValueToComponent(PROBLEM_SEVERITY, SEVERITY_COMBOBOX_ID);
        sdWizardPage.insertValueToComponent(ProblemAssignee, ASSIGNEE_SEARCH_ID);
        sdWizardPage.clickNextButtonInWizard();
        sdWizardPage.clickAcceptButtonInWizard();
        problemId = problemDashboardPage.getProblemIdWithProblemName(PROBLEM_NAME_DESCRIPTION_TXT);
        Assert.assertTrue(problemDashboardPage.isProblemCreated(PROBLEM_NAME_DESCRIPTION_TXT));

    }

    @Test(priority = 2, testName = "Check My Group Problems", description = "Check My Group Problems")
    @Description("Check My Group Problems")
    public void checkMyGroupProblems() {
        myGroupProblemsPage = new MyGroupProblemsPage(driver, webDriverWait).openView(driver, BASIC_URL);
        myGroupProblemsPage.filterBy(ID_ATTRIBUTE, problemId);
        myGroupProblemsPage.getIdForNthTicketInTable(0);
        Assert.assertFalse(myGroupProblemsPage.isIssueTableEmpty());
        Assert.assertEquals(myGroupProblemsPage.getIdForNthTicketInTable(0), problemId);
    }

    @Test(priority = 3, testName = "Export from My Group Problems", description = "Export from My Group Problems")
    @Description("Export from My Group Problems")
    public void exportFromMyGroupProblems() {
        myGroupProblemsPage = new MyGroupProblemsPage(driver, webDriverWait).openView(driver, BASIC_URL);
        try {
            myGroupProblemsPage.exportFromSearchViewTable(EXPORT_WIZARD_ID);
        } catch (RuntimeException e) {
            Assert.fail(e.getMessage());
        }
        notificationWrapperPage = new NotificationWrapperPage(driver);

        Assert.assertEquals(notificationWrapperPage.amountOfNotifications(), 0);
        Assert.assertTrue(myGroupProblemsPage.checkIfFileIsNotEmpty(DOWNLOAD_FILE));
    }

    @Test(priority = 4, testName = "Check My Problems", description = "Check My Problems")
    @Description("Check My Problems")
    public void checkMyProblems() {
        myGroupProblemsPage = new MyGroupProblemsPage(driver, webDriverWait).openView(driver, BASIC_URL);
        myGroupProblemsPage.filterBy(ID_ATTRIBUTE, problemId);
        myGroupProblemsPage.getIdForNthTicketInTable(0);
        Assert.assertFalse(myGroupProblemsPage.isIssueTableEmpty());
        Assert.assertEquals(myGroupProblemsPage.getIdForNthTicketInTable(0), problemId);
    }

    @Test(priority = 5, testName = "Check Problems Search", description = "Check Problems Search")
    @Description("Check Problems Search")
    public void checkProblemsSearch() {
        problemSearchPage = new ProblemSearchPage(driver, webDriverWait).openView(driver, BASIC_URL);
        problemSearchPage.filterBy(ID_ATTRIBUTE, problemId);
        problemSearchPage.getIdForNthTicketInTable(0);
        Assert.assertFalse(problemSearchPage.isIssueTableEmpty());
        Assert.assertEquals(problemSearchPage.getIdForNthTicketInTable(0), problemId);
    }

    @Test(priority = 6, testName = "Close Problem", description = "Close problem")
    @Description("Close Problem")
    public void CloseProblem(
    ) {
        problemOverviewTab = issueDetailsPage.selectOverviewTab(PROBLEM_ISSUE_TYPE);
        problemOverviewTab.skipAllActionsOnCheckList();
        problemOverviewTab.changeIssueStatus(STATUS_IN_PROGRESS);
        problemOverviewTab.skipAllActionsOnCheckList();
        problemOverviewTab.changeIssueStatus(STATUS_CLOSED);
        Assert.assertEquals(problemOverviewTab.checkIssueStatus(), STATUS_CLOSED);
    }

    @Test(priority = 7, testName = "Check Closed Problems View", description = "Refresh, search and check if problem is shown in the closed problems table")
    @Description("Refresh, search and check if problem is shown in the closed problems table")
    public void checkClosedProblemsView() {
        closedProblemsPage = new ClosedProblemsPage(driver, webDriverWait).openView(driver, BASIC_URL);
        closedProblemsPage.clickRefresh();
        closedProblemsPage.filterBy(ID_ATTRIBUTE, problemId);
        Assert.assertEquals(closedProblemsPage.getIdForNthTicketInTable(0), problemId);
    }
}





