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
import com.oss.pages.servicedesk.ticket.tabs.ExternalTab;
import com.oss.pages.servicedesk.ticket.tabs.ParticipantsTab;
import com.oss.pages.servicedesk.ticket.tabs.RelatedProblemsTab;
import com.oss.pages.servicedesk.ticket.tabs.RelatedTicketsTab;
import com.oss.pages.servicedesk.ticket.tabs.RootCausesTab;
import com.oss.pages.servicedesk.ticket.wizard.AttachmentWizardPage;
import com.oss.pages.servicedesk.ticket.wizard.ExternalPromptPage;
import com.oss.pages.servicedesk.ticket.wizard.ParticipantsPromptPage;
import com.oss.pages.servicedesk.ticket.wizard.SDWizardPage;

import io.qameta.allure.Description;

import static com.oss.pages.servicedesk.BaseSDPage.CREATE_DATE_FILTER_DATE_FORMATTER;

public class ProblemsVFNZTest extends BaseTestCase {

    private BaseDashboardPage baseDashboardPage;
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
    private static final String PROBLEM_EXTERNAL = "Selenium External Problem";
    private static final String PROBLEM_EXTERNAL_EDITED = "Selenium External Problem_EDITED";
    private static final String EXTERNAL_LIST_ID = "_PMDetailsExternalsListApp";
    private static final String PARTICIPANT_FIRST_NAME = "SeleniumTest";
    private static final String PARTICIPANT_SURNAME = LocalDateTime.now().toString();
    private static final String PARTICIPANT_ROLE = "Contact";
    private static final String COMBOBOX_LINK_PROBLEM_ID = "linkProblem";

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

    @Test(priority = 6, testName = "Add External to Problem", description = "Add External to Problem")
    @Description("Add External to Problem")
    public void addExternalToProblem() {
        issueDetailsPage = baseDashboardPage.openIssueDetailsView(problemId, BASIC_URL, PROBLEM_ISSUE_TYPE);
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
        issueDetailsPage = baseDashboardPage.openIssueDetailsView(problemId, BASIC_URL, PROBLEM_ISSUE_TYPE);
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
        issueDetailsPage = baseDashboardPage.openIssueDetailsView(problemId, BASIC_URL, PROBLEM_ISSUE_TYPE);
        externalTab = issueDetailsPage.selectExternalTab();
        Assert.assertTrue(externalTab.isExternalCreated(PROBLEM_EXTERNAL_EDITED, EXTERNAL_LIST_ID));

        externalTab.clickDeleteExternal(EXTERNAL_LIST_ID);
        Assert.assertTrue(externalTab.isExternalListEmpty(EXTERNAL_LIST_ID));
    }

    @Test(priority = 9, testName = "Check Root Causes Tree Table in Problem", description = "Check Root Causes Tree Table in Problem")
    @Description("Check Root Causes Tree Table in Problem")
    public void checkRootCauseTreeTable() {
        issueDetailsPage = baseDashboardPage.openIssueDetailsView(problemId, BASIC_URL, PROBLEM_ISSUE_TYPE);
        rootCausesTab = issueDetailsPage.selectRootCauseTab();
        Assert.assertFalse(rootCausesTab.isRootCauseTableEmpty());

        rootCausesTab.selectMOInRootCausesTab(0);
        Assert.assertTrue(rootCausesTab.checkIfRootCausesTreeTableIsNotEmpty());
    }

    @Parameters({"SecondMOIdentifier"})
    @Test(priority = 10, testName = "Add second MO in Root Causes tab", description = "Add second MO in Root Causes tab")
    @Description("Add second MO in Root Causes tab")
    public void addRootCause(
            @Optional("CFS_Access_Product_Selenium_2") String SecondMOIdentifier
    ) {
        issueDetailsPage = baseDashboardPage.openIssueDetailsView(problemId, BASIC_URL, PROBLEM_ISSUE_TYPE);
        rootCausesTab = issueDetailsPage.selectRootCauseTab();
        sdWizardPage = rootCausesTab.openAddRootCauseWizard();
        sdWizardPage.getMoStep().showAllMOs();
        sdWizardPage.getMoStep().enterTextIntoSearchComponent(SecondMOIdentifier);
        sdWizardPage.getMoStep().selectObjectInMOTable(SecondMOIdentifier);
        sdWizardPage.clickAcceptButtonInWizard();

        Assert.assertTrue(rootCausesTab.checkIfMOIdentifierIsPresentOnRootCauses(SecondMOIdentifier));
    }

    @Parameters({"RelatedTicketID"})
    @Test(priority = 11, testName = "Check Related Tickets Tab - link Ticket", description = "Check Related Tickets Tab - link Ticket")
    @Description("Check Related Tickets Tab - link Ticket")
    public void linkTicketToTicket(
            @Optional("100") String RelatedTicketID
    ) {
        issueDetailsPage = baseDashboardPage.openIssueDetailsView(problemId, BASIC_URL, PROBLEM_ISSUE_TYPE);
        relatedTicketsTab = issueDetailsPage.selectRelatedTicketsTab();
        sdWizardPage = relatedTicketsTab.openLinkTicketWizard();
        sdWizardPage.insertValueToMultiSearchComponent(RelatedTicketID, "issueIdsToLink");
        sdWizardPage.clickLinkButton();

        Assert.assertEquals(relatedTicketsTab.checkRelatedTicketsId(0), RelatedTicketID);
    }

    @Test(priority = 12, testName = "Check Related Tickets Tab - unlink Ticket", description = "Check Related Tickets Tab - unlink Ticket")
    @Description("Check Related Tickets Tab - unlink Ticket")
    public void unlinkTicketFromTicket() {
        issueDetailsPage = baseDashboardPage.openIssueDetailsView(problemId, BASIC_URL, PROBLEM_ISSUE_TYPE);
        relatedTicketsTab = issueDetailsPage.selectRelatedTicketsTab();
        relatedTicketsTab.selectTicket(0);
        relatedTicketsTab.unlinkTicket();
        relatedTicketsTab.confirmUnlinking();

        Assert.assertTrue(relatedTicketsTab.isRelatedTicketsTableEmpty());
    }

    @Parameters({"RelatedTicketID"})
    @Test(priority = 13, testName = "Check Related Tickets Tab - show archived switcher", description = "Check Related Tickets Tab - show archived switcher")
    @Description("Check Related Tickets Tab - show archived switcher")
    public void showArchived(
            @Optional("100") String RelatedTicketID
    ) {
        issueDetailsPage = baseDashboardPage.openIssueDetailsView(problemId, BASIC_URL, PROBLEM_ISSUE_TYPE);
        relatedTicketsTab = issueDetailsPage.selectRelatedTicketsTab();
        relatedTicketsTab.turnOnShowArchived();

        Assert.assertEquals(relatedTicketsTab.checkRelatedTicketsId(0), RelatedTicketID);
    }

    @Test(priority = 14, testName = "Check Participants", description = "Check Participants Tab - add Participant")
    @Description("Check Participants Tab - add Participant")
    public void addParticipant() {
        issueDetailsPage = baseDashboardPage.openIssueDetailsView(problemId, BASIC_URL, PROBLEM_ISSUE_TYPE);
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
            @Optional("100") String ProblemToLinkId
    ) {
        issueDetailsPage = baseDashboardPage.openIssueDetailsView(problemId, BASIC_URL, PROBLEM_ISSUE_TYPE);
        relatedProblemsTab = issueDetailsPage.selectRelatedProblemsTab();
        sdWizardPage = relatedProblemsTab.clickLinkProblem();
        sdWizardPage.insertValueToMultiSearchComponent(ProblemToLinkId, COMBOBOX_LINK_PROBLEM_ID);
        sdWizardPage.clickLinkButton();

        Assert.assertEquals(relatedProblemsTab.checkRelatedProblemId(0), ProblemToLinkId);
    }

    @Test(priority = 15, testName = "Unlink Problem from Problem", description = "Unlink Problem from Problem")
    @Description("Unlink Problem from Problem")
    public void unlinkProblem() {
        issueDetailsPage = baseDashboardPage.openIssueDetailsView(problemId, BASIC_URL, PROBLEM_ISSUE_TYPE);
        relatedProblemsTab = issueDetailsPage.selectRelatedProblemsTab();
        relatedProblemsTab.selectProblem(0);
        sdWizardPage = relatedProblemsTab.clickUnlinkProblem();
        sdWizardPage.clickUnlinkConfirmationButton();

        Assert.assertTrue(relatedProblemsTab.isRelatedProblemsTableEmpty());
    }
}
