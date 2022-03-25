package com.oss.servicedesk;

import java.time.LocalDateTime;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.servicedesk.ticket.BaseDashboardPage;
import com.oss.pages.servicedesk.ticket.IssueDetailsPage;
import com.oss.pages.servicedesk.ticket.tabs.AttachmentsTab;
import com.oss.pages.servicedesk.ticket.wizard.AttachmentWizardPage;
import com.oss.pages.servicedesk.ticket.wizard.SDWizardPage;

import io.qameta.allure.Description;

import static com.oss.pages.servicedesk.BaseSDPage.CREATE_DATE_FILTER_DATE_FORMATTER;

public class ProblemsVFNZTest extends BaseTestCase {

    private BaseDashboardPage baseDashboardPage;
    private SDWizardPage sdWizardPage;
    private IssueDetailsPage issueDetailsPage;
    private AttachmentsTab attachmentsTab;
    private AttachmentWizardPage attachmentWizardPage;
    private String problemId;
    private static final String PROBLEMS_DASHBOARD = "_ProblemManagement";
    private static final String PROBLEM_ISSUE_TYPE = "problem";
    private static final String PROBLEM_NAME_DESCRIPTION_ID = "TT_WIZARD_INPUT_PROBLEM_NAME_DESCRIPTION";
    private static final String PROBLEM_NAME_DESCRIPTION_TXT = "Selenium test Problem " + LocalDateTime.now().format(CREATE_DATE_FILTER_DATE_FORMATTER);
    private static final String SEVERITY_COMBOBOX_ID = "TT_WIZARD_INPUT_SEVERITY_LABEL-input";
    private static final String PROBLEM_SEVERITY = "Critical";
    private static final String ASSIGNEE_SEARCH_ID = "TT_WIZARD_INPUT_ASSIGNEE_LABEL";
    private static final String DETAILS_WINDOW_ID = "_detailsWindow";
    private static final String STATUS_IN_PROGRESS = "In Progress";
    private static final String USER_NAME = "sd_seleniumtest";
    private static final String FILE_TO_UPLOAD_PATH = "DataSourceCSV/CPU_USAGE_INFO_RAW-MAP.xlsx";
    private static final String CSV_FILE = "*CPU_USAGE_INFO_RAW-MAP*.*";

    @BeforeMethod
    public void goToProblemDashboardPage() {
        baseDashboardPage = new BaseDashboardPage(driver, webDriverWait).goToPage(driver, BASIC_URL, PROBLEMS_DASHBOARD);
    }

    @Parameters({"MOIdentifier", "ProblemAssignee"})
    @Test(priority = 1, testName = "Create Problem", description = "Create Problem")
    @Description("Create Problem")
    public void createProblem(
            @Optional("CFS_Access_Product_Selenium_1") String MOIdentifier,
            @Optional("ca_kodrobinska") String ProblemAssignee
    ) {
        sdWizardPage = baseDashboardPage.clickCreateProblem();
        sdWizardPage.getMoStep().enterTextIntoSearchComponent(MOIdentifier);
        sdWizardPage.getMoStep().selectRowInMOTable("0");
        sdWizardPage.clickNextButtonInWizard();
        sdWizardPage.insertValueToTextAreaComponent(PROBLEM_NAME_DESCRIPTION_TXT, PROBLEM_NAME_DESCRIPTION_ID);
        sdWizardPage.insertValueToComboBoxComponent(PROBLEM_SEVERITY, SEVERITY_COMBOBOX_ID);
        sdWizardPage.insertValueToSearchComponent(ProblemAssignee, ASSIGNEE_SEARCH_ID);
        sdWizardPage.clickNextButtonInWizard();
        sdWizardPage.clickAcceptButtonInWizard();
        Assert.assertTrue(baseDashboardPage.isProblemCreated(PROBLEM_NAME_DESCRIPTION_TXT));
        problemId = baseDashboardPage.getProblemIdWithProblemName(PROBLEM_NAME_DESCRIPTION_TXT);
    }

    @Parameters({"NewAssignee"})
    @Test(priority = 2, testName = "check overview tab: assignee and status", description = "check possibility to change assignee and status in overview tab")
    @Description("check possibility to change assignee and status in overview tab")
    public void checkOverviewTab(
            @Optional("sd_seleniumtest") String NewAssignee
    ) {
        issueDetailsPage = baseDashboardPage.openIssueDetailsView(problemId, BASIC_URL, PROBLEM_ISSUE_TYPE);
        issueDetailsPage.maximizeWindow(DETAILS_WINDOW_ID);
        issueDetailsPage.changeProblemAssignee(NewAssignee);
        issueDetailsPage.changeProblemStatus(STATUS_IN_PROGRESS);
        goToProblemDashboardPage();

        Assert.assertEquals(baseDashboardPage.getProblemAssignee(PROBLEM_NAME_DESCRIPTION_TXT), NewAssignee);
        Assert.assertEquals(baseDashboardPage.getProblemStatus(PROBLEM_NAME_DESCRIPTION_TXT), STATUS_IN_PROGRESS);
    }

    @Test(priority = 3, testName = "Add attachment to problem", description = "Add attachment to problem")
    @Description("Add attachment to problem")
    public void addAttachment() {
        issueDetailsPage = baseDashboardPage.openIssueDetailsView(problemId, BASIC_URL, PROBLEM_ISSUE_TYPE);
        attachmentsTab = issueDetailsPage.selectAttachmentsTab();

        Assert.assertTrue(attachmentsTab.isAttachmentListEmpty());
        attachmentWizardPage = attachmentsTab.clickAttachFile();
        attachmentWizardPage.uploadAttachmentFile(FILE_TO_UPLOAD_PATH);
        issueDetailsPage = attachmentWizardPage.clickAccept();

        Assert.assertFalse(attachmentsTab.isAttachmentListEmpty());
        Assert.assertEquals(attachmentsTab.getAttachmentOwner(), USER_NAME);
    }

    @Test(priority = 4, testName = "Download Attachment", description = "Download the Attachment from Attachment tab in Problem Details")
    @Description("Download the Attachment from Attachment tab in Problem Details")
    public void downloadAttachment() {
        issueDetailsPage = baseDashboardPage.openIssueDetailsView(problemId, BASIC_URL, PROBLEM_ISSUE_TYPE);
        attachmentsTab = issueDetailsPage.selectAttachmentsTab();

        Assert.assertFalse(attachmentsTab.isAttachmentListEmpty());
        attachmentsTab.clickDownloadAttachment();
        issueDetailsPage.attachFileToReport(CSV_FILE);

        Assert.assertTrue(issueDetailsPage.checkIfFileIsNotEmpty(CSV_FILE));
    }

    @Test(priority = 5, testName = "Delete Attachment", description = "Delete the Attachment from Attachment tab in Problem Details")
    @Description("Delete the Attachment from Attachment tab in Problem Details")
    public void deleteAttachment() {
        issueDetailsPage = baseDashboardPage.openIssueDetailsView(problemId, BASIC_URL, PROBLEM_ISSUE_TYPE);
        attachmentsTab = issueDetailsPage.selectAttachmentsTab();

        Assert.assertFalse(attachmentsTab.isAttachmentListEmpty());
        attachmentsTab.clickDeleteAttachment();

        Assert.assertTrue(attachmentsTab.isAttachmentListEmpty());
    }
}
