package com.oss.servicedesk.problemmanagement;

import java.time.LocalDateTime;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.platform.NotificationWrapperPage;
import com.oss.pages.servicedesk.issue.IssueDetailsPage;
import com.oss.pages.servicedesk.issue.problem.ProblemDashboardPage;
import com.oss.pages.servicedesk.issue.tabs.AttachmentsTab;
import com.oss.pages.servicedesk.issue.tabs.ExternalTab;
import com.oss.pages.servicedesk.issue.tabs.ParticipantsTab;
import com.oss.pages.servicedesk.issue.tabs.RelatedProblemsTab;
import com.oss.pages.servicedesk.issue.tabs.RelatedTicketsTab;
import com.oss.pages.servicedesk.issue.tabs.RootCausesTab;
import com.oss.pages.servicedesk.issue.wizard.AttachmentWizardPage;
import com.oss.pages.servicedesk.issue.wizard.ExternalPromptPage;
import com.oss.pages.servicedesk.issue.wizard.ParticipantsPromptPage;
import com.oss.pages.servicedesk.issue.wizard.SDWizardPage;

import io.qameta.allure.Description;

import static com.oss.pages.servicedesk.BaseSDPage.CREATE_DATE_FILTER_DATE_FORMATTER;
import static com.oss.pages.servicedesk.ServiceDeskConstants.PROBLEM_ISSUE_TYPE;

public class ProblemsTest extends BaseTestCase {

    private ProblemDashboardPage problemDashboardPage;
    private SDWizardPage sdWizardPage;
    private IssueDetailsPage issueDetailsPage;
    private AttachmentsTab attachmentsTab;
    private AttachmentWizardPage attachmentWizardPage;
    private ExternalTab externalTab;
    private ExternalPromptPage externalPromptPage;
    private RootCausesTab rootCausesTab;
    private RelatedTicketsTab relatedTicketsTab;
    private ParticipantsTab participantsTab;
    private ParticipantsPromptPage participantsPromptPage;
    private RelatedProblemsTab relatedProblemsTab;
    private NotificationWrapperPage notificationWrapperPage;
    private String problemId;
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
    private static final String PROBLEM_EXTERNAL = "Selenium External Problem";
    private static final String PROBLEM_EXTERNAL_EDITED = "Selenium External Problem_EDITED";
    private static final String EXTERNAL_LIST_ID = "_PMDetailsExternalsListApp";
    private static final String PARTICIPANT_FIRST_NAME = "SeleniumTest";
    private static final String PARTICIPANT_SURNAME = LocalDateTime.now().toString();
    private static final String PARTICIPANT_ROLE = "Contact";
    private static final String COMBOBOX_LINK_PROBLEM_ID = "linkProblem";

    @BeforeMethod
    public void goToProblemDashboardPage() {
        problemDashboardPage = new ProblemDashboardPage(driver, webDriverWait).goToPage(driver, BASIC_URL);
    }

    @Parameters({"MOIdentifier", "ProblemAssignee"})
    @Test(priority = 1, testName = "Create Problem", description = "Create Problem")
    @Description("Create Problem")
    public void createProblem(
            @Optional("CFS_Access_Product_Selenium_1") String MOIdentifier,
            @Optional("ca_kodrobinska") String ProblemAssignee
    ) {
        sdWizardPage = problemDashboardPage.clickCreateProblem();
        sdWizardPage.getMoStep().enterTextIntoSearchComponent(MOIdentifier);
        sdWizardPage.getMoStep().selectRowInMOTable("0");
        sdWizardPage.clickNextButtonInWizard();
        sdWizardPage.insertValueToTextAreaComponent(PROBLEM_NAME_DESCRIPTION_TXT, PROBLEM_NAME_DESCRIPTION_ID);
        sdWizardPage.insertValueToComboBoxComponent(PROBLEM_SEVERITY, SEVERITY_COMBOBOX_ID);
        sdWizardPage.insertValueToSearchComponent(ProblemAssignee, ASSIGNEE_SEARCH_ID);
        sdWizardPage.clickNextButtonInWizard();
        sdWizardPage.clickAcceptButtonInWizard();
        Assert.assertTrue(problemDashboardPage.isProblemCreated(PROBLEM_NAME_DESCRIPTION_TXT));
        problemId = problemDashboardPage.getProblemIdWithProblemName(PROBLEM_NAME_DESCRIPTION_TXT);
    }

    @Parameters({"NewAssignee"})
    @Test(priority = 2, testName = "check overview tab: assignee and status", description = "check possibility to change assignee and status in overview tab")
    @Description("check possibility to change assignee and status in overview tab")
    public void checkOverviewTab(
            @Optional("sd_seleniumtest") String NewAssignee
    ) {
        issueDetailsPage = problemDashboardPage.openIssueDetailsView(problemId, BASIC_URL, PROBLEM_ISSUE_TYPE);
        issueDetailsPage.maximizeWindow(DETAILS_WINDOW_ID);
        issueDetailsPage.changeIssueAssignee(NewAssignee);
        issueDetailsPage.changeProblemStatus(STATUS_IN_PROGRESS);
        goToProblemDashboardPage();

        Assert.assertEquals(problemDashboardPage.getProblemAssignee(PROBLEM_NAME_DESCRIPTION_TXT), NewAssignee);
        Assert.assertEquals(problemDashboardPage.getProblemStatus(PROBLEM_NAME_DESCRIPTION_TXT), STATUS_IN_PROGRESS);
    }

    @Test(priority = 3, testName = "Add attachment to problem", description = "Add attachment to problem")
    @Description("Add attachment to problem")
    public void addAttachment() {
        issueDetailsPage = problemDashboardPage.openIssueDetailsView(problemId, BASIC_URL, PROBLEM_ISSUE_TYPE);
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
        issueDetailsPage = problemDashboardPage.openIssueDetailsView(problemId, BASIC_URL, PROBLEM_ISSUE_TYPE);
        attachmentsTab = issueDetailsPage.selectAttachmentsTab();

        Assert.assertFalse(attachmentsTab.isAttachmentListEmpty());
        attachmentsTab.clickDownloadAttachment();
        issueDetailsPage.attachFileToReport(CSV_FILE);

        Assert.assertTrue(issueDetailsPage.checkIfFileIsNotEmpty(CSV_FILE));
    }

    @Test(priority = 5, testName = "Delete Attachment", description = "Delete the Attachment from Attachment tab in Problem Details")
    @Description("Delete the Attachment from Attachment tab in Problem Details")
    public void deleteAttachment() {
        issueDetailsPage = problemDashboardPage.openIssueDetailsView(problemId, BASIC_URL, PROBLEM_ISSUE_TYPE);
        attachmentsTab = issueDetailsPage.selectAttachmentsTab();

        Assert.assertFalse(attachmentsTab.isAttachmentListEmpty());
        attachmentsTab.clickDeleteAttachment();

        Assert.assertTrue(attachmentsTab.isAttachmentListEmpty());
    }

    @Test(priority = 6, testName = "Add External to Problem", description = "Add External to Problem")
    @Description("Add External to Problem")
    public void addExternalToProblem() {
        issueDetailsPage = problemDashboardPage.openIssueDetailsView(problemId, BASIC_URL, PROBLEM_ISSUE_TYPE);
        externalTab = issueDetailsPage.selectExternalTab();
        Assert.assertTrue(externalTab.isExternalListEmpty(EXTERNAL_LIST_ID));

        externalPromptPage = externalTab.clickAddExternal();
        externalPromptPage.fillExternalName(PROBLEM_EXTERNAL);
        externalPromptPage.clickCreateExternal();
        Assert.assertTrue(externalTab.isExternalCreated(PROBLEM_EXTERNAL, EXTERNAL_LIST_ID));
    }

    @Test(priority = 7, testName = "Edit External in Problem", description = "Edit External in Problem")
    @Description("Edit External in Problem")
    public void editExternalInProblem() {
        issueDetailsPage = problemDashboardPage.openIssueDetailsView(problemId, BASIC_URL, PROBLEM_ISSUE_TYPE);
        externalTab = issueDetailsPage.selectExternalTab();
        Assert.assertTrue(externalTab.isExternalCreated(PROBLEM_EXTERNAL, EXTERNAL_LIST_ID));

        externalPromptPage = externalTab.clickEditExternal(EXTERNAL_LIST_ID);
        externalPromptPage.fillExternalName(PROBLEM_EXTERNAL_EDITED);
        externalPromptPage.clickSave();
        Assert.assertTrue(externalTab.isExternalCreated(PROBLEM_EXTERNAL_EDITED, EXTERNAL_LIST_ID));
    }

    @Test(priority = 8, testName = "Delete External", description = "Delete External in Problem")
    @Description("Delete External in Problem")
    public void deleteExternalInProblem() {
        issueDetailsPage = problemDashboardPage.openIssueDetailsView(problemId, BASIC_URL, PROBLEM_ISSUE_TYPE);
        externalTab = issueDetailsPage.selectExternalTab();
        Assert.assertTrue(externalTab.isExternalCreated(PROBLEM_EXTERNAL_EDITED, EXTERNAL_LIST_ID));

        externalTab.clickDeleteExternal(EXTERNAL_LIST_ID);
        Assert.assertTrue(externalTab.isExternalListEmpty(EXTERNAL_LIST_ID));
    }

    @Parameters({"SecondMOIdentifier"})
    @Test(priority = 9, testName = "Add second MO in Root Causes tab", description = "Add second MO in Root Causes tab")
    @Description("Add second MO in Root Causes tab")
    public void addRootCause(
            @Optional("CFS_Access_Product_Selenium_2") String SecondMOIdentifier
    ) {
        issueDetailsPage = problemDashboardPage.openIssueDetailsView(problemId, BASIC_URL, PROBLEM_ISSUE_TYPE);
        rootCausesTab = issueDetailsPage.selectRootCauseTab();
        sdWizardPage = rootCausesTab.openAddRootCauseWizard();
        sdWizardPage.getMoStep().showAllMOs();
        sdWizardPage.getMoStep().enterTextIntoSearchComponent(SecondMOIdentifier);
        sdWizardPage.getMoStep().selectObjectInMOTable(SecondMOIdentifier);
        sdWizardPage.clickAcceptButtonInWizard();

        Assert.assertTrue(rootCausesTab.checkIfMOIdentifierIsPresentOnRootCauses(SecondMOIdentifier));
    }

    @Parameters({"RelatedTicketID"})
    @Test(priority = 10, testName = "Check Related Tickets Tab - link Ticket", description = "Check Related Tickets Tab - link Ticket")
    @Description("Check Related Tickets Tab - link Ticket")
    public void linkTicketToTicket(
            @Optional("100") String RelatedTicketID
    ) {
        issueDetailsPage = problemDashboardPage.openIssueDetailsView(problemId, BASIC_URL, PROBLEM_ISSUE_TYPE);
        relatedTicketsTab = issueDetailsPage.selectRelatedTicketsTab();
        sdWizardPage = relatedTicketsTab.openLinkIssueWizard();
        sdWizardPage.insertValueToMultiSearchComponent(RelatedTicketID, "issueIdsToLink");
        sdWizardPage.clickLinkButton();

        Assert.assertEquals(relatedTicketsTab.checkRelatedIssueId(0), RelatedTicketID);
    }

    @Test(priority = 11, testName = "Export Related Tickets", description = "Export Related Tickets")
    @Description("Export Related Tickets")
    public void exportRelatedTickets() {
        issueDetailsPage = problemDashboardPage.openIssueDetailsView(problemId, BASIC_URL, PROBLEM_ISSUE_TYPE);
        relatedTicketsTab = issueDetailsPage.selectRelatedTicketsTab();
        notificationWrapperPage = relatedTicketsTab.openNotificationPanel();
        notificationWrapperPage.clearNotifications();
        relatedTicketsTab.clickExport();
        notificationWrapperPage = relatedTicketsTab.openNotificationPanel();
        notificationWrapperPage.waitForExportFinish();
        notificationWrapperPage.clickDownload();
        notificationWrapperPage.waitAndGetFinishedNotificationText();
        notificationWrapperPage.clearNotifications();
        relatedTicketsTab.attachRelatedIssuesFile();

        Assert.assertEquals(notificationWrapperPage.amountOfNotifications(), 0);
        Assert.assertTrue(relatedTicketsTab.isRelatedIssuesFileNotEmpty());
    }

    @Test(priority = 12, testName = "Check Related Tickets Tab - unlink Ticket", description = "Check Related Tickets Tab - unlink Ticket")
    @Description("Check Related Tickets Tab - unlink Ticket")
    public void unlinkTicketFromTicket() {
        issueDetailsPage = problemDashboardPage.openIssueDetailsView(problemId, BASIC_URL, PROBLEM_ISSUE_TYPE);
        relatedTicketsTab = issueDetailsPage.selectRelatedTicketsTab();
        relatedTicketsTab.selectIssue(0);
        relatedTicketsTab.unlinkIssue();
        relatedTicketsTab.confirmUnlinking();

        Assert.assertTrue(relatedTicketsTab.isRelatedIssueTableEmpty());
    }

    @Parameters({"RelatedTicketID"})
    @Test(priority = 13, testName = "Check Related Tickets Tab - show archived switcher", description = "Check Related Tickets Tab - show archived switcher")
    @Description("Check Related Tickets Tab - show archived switcher")
    public void showArchived(
            @Optional("100") String RelatedTicketID
    ) {
        issueDetailsPage = problemDashboardPage.openIssueDetailsView(problemId, BASIC_URL, PROBLEM_ISSUE_TYPE);
        relatedTicketsTab = issueDetailsPage.selectRelatedTicketsTab();
        relatedTicketsTab.turnOnShowArchived();

        Assert.assertEquals(relatedTicketsTab.checkRelatedIssueId(0), RelatedTicketID);
    }

    @Test(priority = 14, testName = "Check Participants", description = "Check Participants Tab - add Participant")
    @Description("Check Participants Tab - add Participant")
    public void addParticipant() {
        issueDetailsPage = problemDashboardPage.openIssueDetailsView(problemId, BASIC_URL, PROBLEM_ISSUE_TYPE);
        participantsTab = issueDetailsPage.selectParticipantsTab();
        participantsPromptPage = participantsTab.clickAddParticipant();
        participantsPromptPage.setParticipantName(PARTICIPANT_FIRST_NAME);
        participantsPromptPage.setParticipantSurname(PARTICIPANT_SURNAME);
        participantsPromptPage.setParticipantRole(PARTICIPANT_ROLE);
        participantsPromptPage.clickAddParticipant();

        Assert.assertEquals(participantsTab.checkParticipantFirstName(0), PARTICIPANT_FIRST_NAME);
        Assert.assertEquals(participantsTab.checkParticipantSurname(0), PARTICIPANT_SURNAME);
        Assert.assertEquals(participantsTab.checkParticipantRole(0), PARTICIPANT_ROLE.toUpperCase());
    }

    @Parameters({"ProblemToLinkId"})
    @Test(priority = 15, testName = "Link Problem to Problem", description = "Link Problem to Problem")
    @Description("Link Problem to Problem")
    public void linkProblem(
            @Optional("35") String ProblemToLinkId
    ) {
        issueDetailsPage = problemDashboardPage.openIssueDetailsView(problemId, BASIC_URL, PROBLEM_ISSUE_TYPE);
        relatedProblemsTab = issueDetailsPage.selectRelatedProblemsTab();
        sdWizardPage = relatedProblemsTab.openLinkIssueWizard();
        sdWizardPage.insertValueToMultiSearchComponent(ProblemToLinkId, COMBOBOX_LINK_PROBLEM_ID);
        sdWizardPage.clickLinkButton();

        Assert.assertEquals(relatedProblemsTab.checkRelatedIssueId(0), ProblemToLinkId);
    }

    @Test(priority = 16, testName = "Export from Related Problems tab", description = "Export from Related Problems tab")
    @Description("Export from Related Problems tab")
    public void exportFromRelatedProblemsTab() {
        issueDetailsPage = problemDashboardPage.openIssueDetailsView(problemId, BASIC_URL, PROBLEM_ISSUE_TYPE);
        relatedProblemsTab = issueDetailsPage.selectRelatedProblemsTab();
        notificationWrapperPage = relatedProblemsTab.openNotificationPanel();
        notificationWrapperPage.clearNotifications();
        relatedProblemsTab.clickExport();
        notificationWrapperPage = relatedProblemsTab.openNotificationPanel();
        notificationWrapperPage.waitForExportFinish();
        notificationWrapperPage.clickDownload();
        notificationWrapperPage.waitAndGetFinishedNotificationText();
        notificationWrapperPage.clearNotifications();
        relatedProblemsTab.attachRelatedIssuesFile();

        Assert.assertEquals(notificationWrapperPage.amountOfNotifications(), 0);
        Assert.assertTrue(relatedProblemsTab.isRelatedIssuesFileNotEmpty());
    }

    @Test(priority = 17, testName = "Unlink Problem from Problem", description = "Unlink Problem from Problem")
    @Description("Unlink Problem from Problem")
    public void unlinkProblem() {
        issueDetailsPage = problemDashboardPage.openIssueDetailsView(problemId, BASIC_URL, PROBLEM_ISSUE_TYPE);
        relatedProblemsTab = issueDetailsPage.selectRelatedProblemsTab();
        relatedProblemsTab.selectIssue(0);
        relatedProblemsTab.unlinkIssue();
        relatedProblemsTab.confirmUnlinking();

        Assert.assertTrue(relatedProblemsTab.isRelatedIssueTableEmpty());
    }
}
