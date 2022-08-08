package com.oss.iaa.servicedesk.problemmanagement;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.iaa.servicedesk.issue.IssueDetailsPage;
import com.oss.pages.iaa.servicedesk.issue.MoreDetailsPage;
import com.oss.pages.iaa.servicedesk.issue.problem.ProblemDashboardPage;
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
import com.oss.pages.iaa.servicedesk.issue.tabs.TasksTab;
import com.oss.pages.iaa.servicedesk.issue.wizard.ExternalPromptPage;
import com.oss.pages.iaa.servicedesk.issue.wizard.SDWizardPage;

import io.qameta.allure.Description;

import static com.oss.pages.iaa.servicedesk.BaseSDPage.DATE_TIME_FORMATTER;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.COMBOBOX_LINK_PROBLEM_ID;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.CSV_FILE;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.DETAILS_TABS_CONTAINER_ID;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.EXTERNAL_TYPE;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.FILE_TO_UPLOAD_PATH;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.INTERNAL_TYPE;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.PROBLEM_ISSUE_TYPE;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.SOURCE_TYPE;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.TABS_WIDGET_ID;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.TASK_LABEL;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.TYPE_COMMENT;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.USER_NAME;

public class ProblemsTest extends BaseTestCase {

    private ProblemDashboardPage problemDashboardPage;
    private SDWizardPage sdWizardPage;
    private IssueDetailsPage issueDetailsPage;
    private MoreDetailsPage moreDetailsPage;
    private OverviewTab problemOverviewTab;
    private AttachmentsTab attachmentsTab;
    private ExternalTab externalTab;
    private ExternalPromptPage externalPromptPage;
    private RootCausesTab rootCausesTab;
    private RelatedTicketsTab relatedTicketsTab;
    private ParticipantsTab participantsTab;
    private RelatedProblemsTab relatedProblemsTab;
    private DescriptionTab descriptionTab;
    private ProblemSolutionTab problemSolutionTab;
    private MessagesTab messagesTab;
    private RelatedChangesTab relatedChangesTab;
    private TasksTab tasksTab;
    private AffectedTab affectedTab;
    private String problemId;
    private static final String PROBLEM_NAME_DESCRIPTION_ID = "TT_WIZARD_INPUT_PROBLEM_NAME_DESCRIPTION";
    private static final String PROBLEM_NAME_DESCRIPTION_TXT = "Selenium test Problem " + LocalDateTime.now().format(DATE_TIME_FORMATTER);
    private static final String SEVERITY_COMBOBOX_ID = "TT_WIZARD_INPUT_SEVERITY_LABEL";
    private static final String PROBLEM_SEVERITY = "Critical";
    private static final String ASSIGNEE_SEARCH_ID = "TT_WIZARD_INPUT_ASSIGNEE_LABEL";
    private static final String STATUS_IN_PROGRESS = "In Progress";
    private static final String PROBLEM_EXTERNAL = "Selenium External Problem";
    private static final String PROBLEM_EXTERNAL_EDITED = "Selenium External Problem_EDITED";
    private static final String EXTERNAL_LIST_ID = "_PMDetailsExternalsListApp";
    private static final String PARTICIPANT_FIRST_NAME = "SeleniumTest";
    private static final String PARTICIPANT_SURNAME = LocalDateTime.now().toString();
    private static final String PARTICIPANT_ROLE = "Contact";
    private static final String PARTICIPANT_FIRST_NAME_EDITED = "SeleniumTestEdited";
    private static final String DESCRIPTION_NOTE = "Test description note Selenium";
    private static final String PROBLEM_SOLUTION_NOTE = "Problem Solution Note added by automatic test";
    private static final String NOTIFICATION_EMAIL_MSG = "Test Selenium Email Message in Problems";
    private static final String NOTIFICATION_INTERNAL_MSG = "Test Selenium Internal Message in Problems";
    private static final String INTERNAL_COMMENT_MSG = "Test Selenium Message Comment in Problem";
    private static final String TITLE_ID = "ISSUE_TITLE";
    private static final String TITLE_TEXT = "Change in Related Tab";
    private static final String DESCRIPTION_CHANGE = "Change description in related tab";
    private static final String SOURCE_ID = "TT_WIZARD_INPUT_SOURCE_LABEL";
    private static final String INTERNAL_TEXT = "Internal";
    private static final String TASK_NAME = "Selenium Task" + LocalDateTime.now();
    private static final String EDITED_TASK_NAME = "Edited Selenium Task" + LocalDateTime.now();
    private static final String TASK_WIZARD_NAME = "name";
    private static final String SAVE_EDITED_TASK_BUTTON_ID = "_taskDetailsSubmitId-1";
    private static final String NAME_ATTRIBUTE = "Name";
    private static final String PROBLEM_CREATED_LOG = "Problem has been created";

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
            @Optional("CFS_Access_Product_Selenium_1") String MOIdentifier,
            @Optional("ca_kodrobinska") String ProblemAssignee
    ) {
        sdWizardPage = problemDashboardPage.clickCreateProblem();
        sdWizardPage.getMoStep().enterTextIntoSearchComponent(MOIdentifier);
        sdWizardPage.getMoStep().selectRowInMOTable("0");
        sdWizardPage.clickNextButtonInWizard();
        sdWizardPage.insertValueToComponent(PROBLEM_NAME_DESCRIPTION_TXT, PROBLEM_NAME_DESCRIPTION_ID);
        sdWizardPage.insertValueToComponent(PROBLEM_SEVERITY, SEVERITY_COMBOBOX_ID);
        sdWizardPage.insertValueToComponent(ProblemAssignee, ASSIGNEE_SEARCH_ID);
        sdWizardPage.clickNextButtonInWizard();
        sdWizardPage.clickAcceptButtonInWizard();
        Assert.assertTrue(problemDashboardPage.isProblemCreated(PROBLEM_NAME_DESCRIPTION_TXT));
        problemId = problemDashboardPage.getProblemIdWithProblemName(PROBLEM_NAME_DESCRIPTION_TXT);
    }

    @Test(priority = 2, testName = "Check Attributes", description = "Check Attributes")
    @Description("Check Attributes")
    public void checkAttributes() {
        problemOverviewTab = issueDetailsPage.selectOverviewTab(PROBLEM_ISSUE_TYPE);
        problemOverviewTab.maximizeWindow(DETAILS_TABS_CONTAINER_ID);
        moreDetailsPage = problemOverviewTab.clickMoreDetails();
        Assert.assertEquals(moreDetailsPage.checkValueOfAttribute(NAME_ATTRIBUTE), PROBLEM_NAME_DESCRIPTION_TXT);
    }

    @Test(priority = 3, testName = "Check Logs", description = "Check Logs")
    @Description("Check Logs")
    public void checkLogs() {
        problemOverviewTab = issueDetailsPage.selectOverviewTab(PROBLEM_ISSUE_TYPE);
        problemOverviewTab.maximizeWindow(DETAILS_TABS_CONTAINER_ID);
        moreDetailsPage = problemOverviewTab.clickMoreDetails();
        Assert.assertTrue(moreDetailsPage.isNoteInLogsTable(PROBLEM_CREATED_LOG));
    }

    @Parameters({"NewAssignee"})
    @Test(priority = 4, testName = "check overview tab: assignee and status", description = "check possibility to change assignee and status in overview tab")
    @Description("check possibility to change assignee and status in overview tab")
    public void checkOverviewTab(
            @Optional("sd_seleniumtest") String NewAssignee
    ) {
        issueDetailsPage.maximizeWindow(DETAILS_TABS_CONTAINER_ID);
        problemOverviewTab = issueDetailsPage.selectOverviewTab(PROBLEM_ISSUE_TYPE);
        problemOverviewTab.changeIssueAssignee(NewAssignee);
        problemOverviewTab.changeIssueStatus(STATUS_IN_PROGRESS);
        problemDashboardPage = new ProblemDashboardPage(driver, webDriverWait).goToPage(driver, BASIC_URL);

        Assert.assertEquals(problemDashboardPage.getProblemAssignee(PROBLEM_NAME_DESCRIPTION_TXT), NewAssignee);
        Assert.assertEquals(problemDashboardPage.getProblemStatus(PROBLEM_NAME_DESCRIPTION_TXT), STATUS_IN_PROGRESS);
    }

    @Test(priority = 5, testName = "Add attachment to problem", description = "Add attachment to problem")
    @Description("Add attachment to problem")
    public void addAttachment() {
        attachmentsTab = issueDetailsPage.selectAttachmentsTab(TABS_WIDGET_ID);

        Assert.assertTrue(attachmentsTab.isAttachmentListEmpty());
        attachmentsTab.addAttachment(FILE_TO_UPLOAD_PATH);

        Assert.assertFalse(attachmentsTab.isAttachmentListEmpty());
        Assert.assertEquals(attachmentsTab.getAttachmentOwner(), USER_NAME);
    }

    @Test(priority = 6, testName = "Download Attachment", description = "Download the Attachment from Attachment tab in Problem Details")
    @Description("Download the Attachment from Attachment tab in Problem Details")
    public void downloadAttachment() {
        attachmentsTab = issueDetailsPage.selectAttachmentsTab(TABS_WIDGET_ID);

        Assert.assertFalse(attachmentsTab.isAttachmentListEmpty());
        attachmentsTab.clickDownloadAttachment();
        issueDetailsPage.attachFileToReport(CSV_FILE);

        Assert.assertTrue(issueDetailsPage.checkIfFileIsNotEmpty(CSV_FILE));
    }

    @Test(priority = 7, testName = "Delete Attachment", description = "Delete the Attachment from Attachment tab in Problem Details")
    @Description("Delete the Attachment from Attachment tab in Problem Details")
    public void deleteAttachment() {
        attachmentsTab = issueDetailsPage.selectAttachmentsTab(TABS_WIDGET_ID);

        Assert.assertFalse(attachmentsTab.isAttachmentListEmpty());
        attachmentsTab.clickDeleteAttachment();

        Assert.assertTrue(attachmentsTab.isAttachmentListEmpty());
    }

    @Test(priority = 8, testName = "Add External to Problem", description = "Add External to Problem")
    @Description("Add External to Problem")
    public void addExternalToProblem() {
        externalTab = issueDetailsPage.selectExternalTab();
        Assert.assertTrue(externalTab.isExternalListEmpty(EXTERNAL_LIST_ID));

        externalPromptPage = externalTab.clickAddExternal();
        externalPromptPage.fillExternalName(PROBLEM_EXTERNAL);
        externalPromptPage.clickCreateExternal();
        Assert.assertTrue(externalTab.isExternalCreated(PROBLEM_EXTERNAL, EXTERNAL_LIST_ID));
    }

    @Test(priority = 9, testName = "Edit External in Problem", description = "Edit External in Problem")
    @Description("Edit External in Problem")
    public void editExternalInProblem() {
        externalTab = issueDetailsPage.selectExternalTab();
        Assert.assertTrue(externalTab.isExternalCreated(PROBLEM_EXTERNAL, EXTERNAL_LIST_ID));

        externalPromptPage = externalTab.clickEditExternal(EXTERNAL_LIST_ID);
        externalPromptPage.fillExternalName(PROBLEM_EXTERNAL_EDITED);
        externalPromptPage.clickSave();
        Assert.assertTrue(externalTab.isExternalCreated(PROBLEM_EXTERNAL_EDITED, EXTERNAL_LIST_ID));
    }

    @Test(priority = 10, testName = "Delete External", description = "Delete External in Problem")
    @Description("Delete External in Problem")
    public void deleteExternalInProblem() {
        externalTab = issueDetailsPage.selectExternalTab();
        Assert.assertTrue(externalTab.isExternalCreated(PROBLEM_EXTERNAL_EDITED, EXTERNAL_LIST_ID));

        externalTab.clickDeleteExternal(EXTERNAL_LIST_ID);
        Assert.assertTrue(externalTab.isExternalListEmpty(EXTERNAL_LIST_ID));
    }

    @Parameters({"SecondMOIdentifier"})
    @Test(priority = 11, testName = "Add second MO in Root Causes tab", description = "Add second MO in Root Causes tab")
    @Description("Add second MO in Root Causes tab")
    public void addRootCause(
            @Optional("CFS_Access_Product_Selenium_2") String SecondMOIdentifier
    ) {
        rootCausesTab = issueDetailsPage.selectRootCauseTab();
        sdWizardPage = rootCausesTab.openAddRootCauseWizard();
        sdWizardPage.getMoStep().showAllMOs();
        sdWizardPage.getMoStep().enterTextIntoSearchComponent(SecondMOIdentifier);
        sdWizardPage.getMoStep().selectObjectInMOTable(SecondMOIdentifier);
        sdWizardPage.clickAcceptButtonInWizard();

        Assert.assertTrue(rootCausesTab.checkIfMOIdentifierIsPresentOnRootCauses(SecondMOIdentifier));
    }

    @Parameters({"RelatedTicketID"})
    @Test(priority = 12, testName = "Check Related Tickets Tab - link Ticket", description = "Check Related Tickets Tab - link Ticket")
    @Description("Check Related Tickets Tab - link Ticket")
    public void linkTicketToTicket(
            @Optional("100") String RelatedTicketID
    ) {
        relatedTicketsTab = issueDetailsPage.selectRelatedTicketsTab();
        relatedTicketsTab.linkIssue(RelatedTicketID, "issueIdsToLink");

        Assert.assertEquals(relatedTicketsTab.checkRelatedIssueId(0), RelatedTicketID);
    }

    @Test(priority = 13, testName = "Export Related Tickets", description = "Export Related Tickets")
    @Description("Export Related Tickets")
    public void exportRelatedTickets() {
        relatedTicketsTab = issueDetailsPage.selectRelatedTicketsTab();
        relatedTicketsTab.exportFromTabTable();
        int amountOfNotifications = relatedTicketsTab.openNotificationPanel().amountOfNotifications();

        Assert.assertEquals(amountOfNotifications, 0);
        Assert.assertTrue(relatedTicketsTab.isRelatedIssuesFileNotEmpty());
    }

    @Test(priority = 14, testName = "Check Related Tickets Tab - unlink Ticket", description = "Check Related Tickets Tab - unlink Ticket")
    @Description("Check Related Tickets Tab - unlink Ticket")
    public void unlinkTicketFromTicket() {
        relatedTicketsTab = issueDetailsPage.selectRelatedTicketsTab();
        relatedTicketsTab.selectIssue(0);
        relatedTicketsTab.unlinkIssue();
        relatedTicketsTab.confirmUnlinking();

        Assert.assertTrue(relatedTicketsTab.isRelatedIssueTableEmpty());
    }

    @Parameters({"RelatedTicketID"})
    @Test(priority = 15, testName = "Check Related Tickets Tab - show archived switcher", description = "Check Related Tickets Tab - show archived switcher")
    @Description("Check Related Tickets Tab - show archived switcher")
    public void showArchived(
            @Optional("100") String RelatedTicketID
    ) {
        relatedTicketsTab = issueDetailsPage.selectRelatedTicketsTab();
        relatedTicketsTab.turnOnShowArchived();

        Assert.assertEquals(relatedTicketsTab.checkRelatedIssueId(0), RelatedTicketID);
    }

    @Test(priority = 16, testName = "Check Participants", description = "Check Participants Tab - add Participant")
    @Description("Check Participants Tab - add Participant")
    public void addParticipant() {
        participantsTab = issueDetailsPage.selectParticipantsTab();
        participantsTab.createParticipantAndLinkToIssue(PARTICIPANT_FIRST_NAME, PARTICIPANT_SURNAME, PARTICIPANT_ROLE);
        int newParticipantRow = participantsTab.participantRow(PARTICIPANT_FIRST_NAME);

        Assert.assertEquals(participantsTab.checkParticipantFirstName(newParticipantRow), PARTICIPANT_FIRST_NAME);
        Assert.assertEquals(participantsTab.checkParticipantSurname(newParticipantRow), PARTICIPANT_SURNAME);
        Assert.assertEquals(participantsTab.checkParticipantRole(newParticipantRow), PARTICIPANT_ROLE.toUpperCase());
    }

    @Test(priority = 17, testName = "Edit participant", description = "Edit participant")
    @Description("Edit participant")
    public void editParticipant() {
        participantsTab = issueDetailsPage.selectParticipantsTab();
        int newParticipantRow = participantsTab.participantRow(PARTICIPANT_FIRST_NAME);
        participantsTab.selectParticipant(newParticipantRow);
        participantsTab.editParticipant(PARTICIPANT_FIRST_NAME_EDITED, PARTICIPANT_SURNAME, PARTICIPANT_ROLE);
        int editedParticipantRow = participantsTab.participantRow(PARTICIPANT_FIRST_NAME_EDITED);

        Assert.assertEquals(participantsTab.checkParticipantFirstName(editedParticipantRow), PARTICIPANT_FIRST_NAME_EDITED);
        Assert.assertEquals(participantsTab.checkParticipantSurname(editedParticipantRow), PARTICIPANT_SURNAME);
        Assert.assertEquals(participantsTab.checkParticipantRole(editedParticipantRow), PARTICIPANT_ROLE.toUpperCase());
    }

    @Test(priority = 18, testName = "Unlink Participant", description = "Unlink Edited Participant")
    @Description("Unlink Edited Participant")
    public void unlinkParticipant() {
        participantsTab = issueDetailsPage.selectParticipantsTab();
        int participantsInTable = participantsTab.countParticipantsInTable();
        participantsTab.selectParticipant(participantsTab.participantRow(PARTICIPANT_FIRST_NAME_EDITED));
        participantsTab.clickUnlinkParticipant();

        Assert.assertEquals(participantsTab.countParticipantsInTable(), participantsInTable - 1);
    }

    @Test(priority = 19, testName = "Remove Participant", description = "Remove Edited Participant")
    @Description("Remove Edited Participant")
    public void removeParticipant() {
        participantsTab = issueDetailsPage.selectParticipantsTab();
        participantsTab.addExistingParticipant(PARTICIPANT_FIRST_NAME_EDITED, PARTICIPANT_ROLE);
        int participantsInTable = participantsTab.countParticipantsInTable();
        participantsTab.selectParticipant(participantsTab.participantRow(PARTICIPANT_FIRST_NAME_EDITED));
        participantsTab.clickRemoveParticipant();
        participantsTab.clickConfirmRemove();

        Assert.assertEquals(participantsTab.countParticipantsInTable(), participantsInTable - 1);
    }

    @Parameters({"ProblemToLinkId"})
    @Test(priority = 20, testName = "Link Problem to Problem", description = "Link Problem to Problem")
    @Description("Link Problem to Problem")
    public void linkProblem(
            @Optional("35") String ProblemToLinkId
    ) {
        relatedProblemsTab = issueDetailsPage.selectRelatedProblemsTab();
        relatedProblemsTab.linkIssue(ProblemToLinkId, COMBOBOX_LINK_PROBLEM_ID);

        Assert.assertEquals(relatedProblemsTab.checkRelatedIssueId(0), ProblemToLinkId);
    }

    @Test(priority = 21, testName = "Export from Related Problems tab", description = "Export from Related Problems tab")
    @Description("Export from Related Problems tab")
    public void exportFromRelatedProblemsTab() {
        relatedProblemsTab = issueDetailsPage.selectRelatedProblemsTab();
        relatedProblemsTab.exportFromTabTable();
        int amountOfNotifications = relatedProblemsTab.openNotificationPanel().amountOfNotifications();

        Assert.assertEquals(amountOfNotifications, 0);
        Assert.assertTrue(relatedProblemsTab.isRelatedIssuesFileNotEmpty());
    }

    @Test(priority = 22, testName = "Unlink Problem from Problem", description = "Unlink Problem from Problem")
    @Description("Unlink Problem from Problem")
    public void unlinkProblem() {
        relatedProblemsTab = issueDetailsPage.selectRelatedProblemsTab();
        relatedProblemsTab.selectIssue(0);
        relatedProblemsTab.unlinkIssue();
        relatedProblemsTab.confirmUnlinking();

        Assert.assertTrue(relatedProblemsTab.isRelatedIssueTableEmpty());
    }

    @Test(priority = 23, testName = "Add Description To Problem", description = "Add Description To Problem in Description Tab")
    @Description("Add Description To Problem in Description Tab")
    public void addDescription() {
        descriptionTab = issueDetailsPage.selectDescriptionTab();
        descriptionTab.addTextNote(DESCRIPTION_NOTE);

        Assert.assertEquals(descriptionTab.getTextMessage(), DESCRIPTION_NOTE);
    }

    @Test(priority = 24, testName = "Add Problem Solution", description = "Add Problem Solution")
    @Description("Add Problem Solution")
    public void addProblemSolution() {
        problemSolutionTab = issueDetailsPage.selectProblemSolutionTab();
        problemSolutionTab.addTextNote(PROBLEM_SOLUTION_NOTE);

        Assert.assertEquals(problemSolutionTab.getTextMessage(), PROBLEM_SOLUTION_NOTE);
    }

    @Parameters({"messageTo"})
    @Test(priority = 25, testName = "Add Internal Notification in Change", description = "Add Internal Notification in Change")
    @Description("Add Internal Notification in Change")
    public void addInternalNotification(
            @Optional("ca_kodrobinska") String messageTo
    ) {
        issueDetailsPage.maximizeWindow(TABS_WIDGET_ID);
        messagesTab = issueDetailsPage.selectMessagesTab();
        sdWizardPage = messagesTab.createNewNotificationOnMessagesTab();
        sdWizardPage.createInternalNotification(NOTIFICATION_INTERNAL_MSG, messageTo);

        Assert.assertFalse(messagesTab.isMessagesTabEmpty());
        Assert.assertEquals(messagesTab.getMessageText(0), NOTIFICATION_INTERNAL_MSG);
        Assert.assertEquals(messagesTab.checkMessageType(0), "NOTIFICATION");
        Assert.assertEquals(messagesTab.getBadgeTextFromMessage(0, 0), "New");
    }

    @Parameters({"notificationEmailTo", "notificationEmailFrom"})
    @Test(priority = 26, testName = "Add Email Notification in Change", description = "Add Email Notification in Change")
    @Description("Add Email Notification in Change")
    public void addEmailNotification(
            @Optional("kornelia.odrobinska@comarch.com") String notificationEmailTo,
            @Optional("switch.ticket@comarch.com") String notificationEmailFrom
    ) {
        issueDetailsPage.maximizeWindow(TABS_WIDGET_ID);
        messagesTab = issueDetailsPage.selectMessagesTab();
        sdWizardPage = messagesTab.createNewNotificationOnMessagesTab();
        sdWizardPage.createEmailNotification(notificationEmailTo, notificationEmailFrom, NOTIFICATION_EMAIL_MSG);

        Assert.assertFalse(messagesTab.isMessagesTabEmpty());
        Assert.assertEquals(messagesTab.getMessageText(0), NOTIFICATION_EMAIL_MSG);
        Assert.assertEquals(messagesTab.checkMessageType(0), "NOTIFICATION");
    }

    @Test(priority = 27, testName = "Add Internal Comment", description = "Add Internal Comment")
    @Description("Add Internal Comment")
    public void addInternalComment() {
        issueDetailsPage.maximizeWindow(TABS_WIDGET_ID);
        messagesTab = issueDetailsPage.selectMessagesTab();
        messagesTab.addComment(INTERNAL_COMMENT_MSG, INTERNAL_TYPE);

        Assert.assertFalse(messagesTab.isMessagesTabEmpty());
        Assert.assertEquals(messagesTab.checkMessageType(0), "COMMENT");
        Assert.assertEquals(messagesTab.getMessageText(0), INTERNAL_COMMENT_MSG);
        Assert.assertEquals(messagesTab.checkCommentType(0), INTERNAL_TYPE.toLowerCase());
    }

    @Test(priority = 28, testName = "Add External Comment", description = "Add External Comment")
    @Description("Add External Comment")
    public void addExternalComment() {
        issueDetailsPage.maximizeWindow(TABS_WIDGET_ID);
        messagesTab = issueDetailsPage.selectMessagesTab();
        messagesTab.addComment(INTERNAL_COMMENT_MSG + EXTERNAL_TYPE, EXTERNAL_TYPE);

        Assert.assertFalse(messagesTab.isMessagesTabEmpty());
        Assert.assertEquals(messagesTab.checkMessageType(0), TYPE_COMMENT);
        Assert.assertEquals(messagesTab.getMessageText(0), INTERNAL_COMMENT_MSG + EXTERNAL_TYPE);
        Assert.assertEquals(messagesTab.checkCommentType(0), EXTERNAL_TYPE.toLowerCase());
    }

    @Test(priority = 29, testName = "Add Source Comment", description = "Add Source Comment")
    @Description("Add Source Comment")
    public void addSourceComment() {
        issueDetailsPage.maximizeWindow(TABS_WIDGET_ID);
        messagesTab = issueDetailsPage.selectMessagesTab();
        messagesTab.addComment(INTERNAL_COMMENT_MSG + SOURCE_TYPE, SOURCE_TYPE);

        Assert.assertFalse(messagesTab.isMessagesTabEmpty());
        Assert.assertEquals(messagesTab.checkMessageType(0), TYPE_COMMENT);
        Assert.assertEquals(messagesTab.getMessageText(0), INTERNAL_COMMENT_MSG + SOURCE_TYPE);
        Assert.assertEquals(messagesTab.checkCommentType(0), SOURCE_TYPE.toLowerCase());
    }

    @Test(priority = 30, testName = "Filtering Comments", description = "Filtering Comments")
    @Description("Filtering Comments")
    public void filterComments() {
        issueDetailsPage.maximizeWindow(TABS_WIDGET_ID);
        messagesTab = issueDetailsPage.selectMessagesTab();
        Assert.assertFalse(messagesTab.isMessagesTabEmpty());
        messagesTab.filterMessages(TYPE_COMMENT);
        messagesTab.filterComments(INTERNAL_TYPE);
        Assert.assertEquals(messagesTab.checkCommentType(0), INTERNAL_TYPE.toLowerCase());
    }

    @Parameters({"userName"})
    @Test(priority = 31, testName = "Add related Change to Problem", description = "Add related Change to Problem")
    @Description("Add related Change to Problem")
    public void addChangeToProblem(
            @Optional("sd_seleniumtest") String userName
    ) {
        relatedChangesTab = issueDetailsPage.selectRelatedChangesTab();
        Assert.assertTrue(relatedChangesTab.isRelatedIssueTableEmpty());

        sdWizardPage = relatedChangesTab.openCreateChangeWizard();
        sdWizardPage.insertValueToComponent(TITLE_TEXT, TITLE_ID);
        sdWizardPage.insertValueToComponent(INTERNAL_TEXT, SOURCE_ID);
        sdWizardPage.createChange(userName, userName, DESCRIPTION_CHANGE);
        Assert.assertFalse(relatedChangesTab.isRelatedIssueTableEmpty());
        Assert.assertEquals(relatedChangesTab.getIncidentDescriptionFromTable(), DESCRIPTION_CHANGE);
    }

    @Test(priority = 32, testName = "Export Related Changes", description = "Export Related Changes")
    @Description("Export Related Changes")
    public void exportRelatedChanges() {
        relatedChangesTab = issueDetailsPage.selectRelatedChangesTab();
        relatedChangesTab.exportFromTabTable();
        int amountOfNotifications = relatedChangesTab.openNotificationPanel().amountOfNotifications();

        Assert.assertEquals(amountOfNotifications, 0);
        Assert.assertTrue(relatedChangesTab.isRelatedIssuesFileNotEmpty());
    }

    @Parameters({"taskAssignee"})
    @Test(priority = 33, testName = "Add task in Problem Detail View", description = "Add task in Problem Detail View")
    @Description("Add task in Problem Detail View")
    public void addTaskInProblemView(
            @Optional("sd_seleniumtest") String taskAssignee
    ) {
        tasksTab = issueDetailsPage.selectTasksTab();
        sdWizardPage = tasksTab.clickAddTaskInProblemView();
        sdWizardPage.createTask(TASK_NAME, taskAssignee, TASK_LABEL);

        Assert.assertEquals(tasksTab.getTaskName(), TASK_NAME);
    }

    @Test(priority = 34, testName = "Edit task in Problem Detail View", description = "Edit task in Problem Detail View")
    @Description("Edit task in Problem Detail View")
    public void editTaskInProblemView() {
        tasksTab = issueDetailsPage.selectTasksTab();
        sdWizardPage = tasksTab.clickDetailsButtonInFirstTask();
        sdWizardPage.insertValueToComponent(EDITED_TASK_NAME, TASK_WIZARD_NAME);
        sdWizardPage.clickButton(SAVE_EDITED_TASK_BUTTON_ID);

        Assert.assertEquals(tasksTab.getTaskName(), EDITED_TASK_NAME);
    }

    @Parameters({"serviceMOIdentifier"})
    @Test(priority = 35, testName = "Add Affected", description = "Add Affected Service to the Problem")
    @Description("Add Affected Service to the Problem")
    public void addAffected(
            @Optional("TEST_MO_ABS_SRV") String serviceMOIdentifier
    ) {
        affectedTab = issueDetailsPage.selectAffectedTab();
        int initialServiceCount = affectedTab.countServicesInTable();
        affectedTab.addServiceToTable(serviceMOIdentifier);

        Assert.assertEquals(affectedTab.countServicesInTable(), initialServiceCount + 1);
    }
}
