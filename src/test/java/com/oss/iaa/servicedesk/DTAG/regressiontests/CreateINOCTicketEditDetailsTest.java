package com.oss.iaa.servicedesk.DTAG.regressiontests;

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
import com.oss.pages.iaa.servicedesk.issue.RemainderForm;
import com.oss.pages.iaa.servicedesk.issue.tabs.AttachmentsTab;
import com.oss.pages.iaa.servicedesk.issue.tabs.DescriptionTab;
import com.oss.pages.iaa.servicedesk.issue.tabs.MessagesTab;
import com.oss.pages.iaa.servicedesk.issue.tabs.ParticipantsTab;
import com.oss.pages.iaa.servicedesk.issue.tabs.RelatedTicketsTab;
import com.oss.pages.iaa.servicedesk.issue.tabs.RootCausesTab;
import com.oss.pages.iaa.servicedesk.issue.ticket.TicketDashboardPage;
import com.oss.pages.iaa.servicedesk.issue.ticket.TicketOverviewTab;
import com.oss.pages.iaa.servicedesk.issue.ticket.TicketSearchPage;
import com.oss.pages.iaa.servicedesk.issue.wizard.ParticipantsPromptPage;
import com.oss.pages.iaa.servicedesk.issue.wizard.SDWizardPage;

import io.qameta.allure.Description;

import static com.oss.pages.iaa.servicedesk.BaseSDPage.DATE_TIME_FORMATTER;
import static com.oss.pages.iaa.servicedesk.BaseSearchPage.CREATION_TIME_ATTRIBUTE;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.DETAILS_TABS_CONTAINER_ID;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.DICTIONARIES_TAB_LABEL;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.EXTERNAL_TYPE;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.INTERNAL_TYPE;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.MOST_IMPORTANT_INFO_TAB_LABEL;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.TROUBLE_TICKET_ISSUE_TYPE;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.TYPE_COMMENT;

public class CreateINOCTicketEditDetailsTest extends BaseTestCase {

    private TicketDashboardPage ticketDashboardPage;
    private TicketOverviewTab ticketOverviewTab;
    private TicketSearchPage ticketSearchPage;
    private SDWizardPage sdWizardPage;
    private IssueDetailsPage issueDetailsPage;
    private MessagesTab messagesTab;
    private MoreDetailsPage moreDetailsPage;
    private RemainderForm remainderForm;
    private MessagesTab mostImportantInfoTab;
    private AttachmentsTab attachmentsTab;
    private RootCausesTab rootCausesTab;
    private RelatedTicketsTab relatedTicketsTab;
    private ParticipantsTab participantsTab;
    private ParticipantsPromptPage participantsPromptPage;
    private String ticketID;

    private final static String INOC_DASHBOARD_SUFFIX = "ForInoc";

    private final static String TT_WIZARD_PRODUCT = "TT_WIZARD_INPUT_PRODUCT_LABEL";
    private final static String TT_WIZARD_REPORTED_INCIDENT_TYPE = "TT_WIZARD_INPUT_REPORTED_INCIDENT_TYPE_LABEL";
    private static final String TT_WIZARD_INPUT_INCIDENT_TYPE = "TT_WIZARD_INPUT_INCIDENT_TITLE_LABEL";
    private static final String TT_WIZARD_SLA_CONTRACT = "TT_WIZARD_INPUT_SLA_CONTRACT_LABEL";

    private final static String TT_PRODUCT = "GTEL";
    private final static String TT_INCIDENT_DESCRIPTION = "Test selenium";
    private final static String TT_REPORTED_INCIDENT_TYPE = "Erroring";
    private final static String TT_SLA_CONTRACT = "BEST EFFORT";

    private final static String TT_MANAGED_OBJECTS = "Managed Objects";
    private static final String TT_DESCRIPTION_EDITED = "TestSelenium_edited";
    private static final String TT_LIBRARY_TYPE = "INOC Failure Causes";
    private static final String TT_INOC_FAILURE_CAUSES = "OnNet";
    private static final String TT_FAILURE_SPECIFICATION = "Bad weather conditions";
    private static final String TICKETS_SEARCH_ASSIGNEE_ATTRIBUTE = "ticketOut.issueOut.assignee.name";

    private static final String NOTIFICATION_CHANNEL_INTERNAL = "Internal";
    private static final String NOTIFICATION_CHANNEL_EMAIL = "E-mail";
    private static final String NOTIFICATION_MESSAGE_INTERNAL = "test selenium message internal";
    private static final String NOTIFICATION_MESSAGE_EMAIL = "test selenium message E-mail";
    private static final String NOTIFICATION_MESSAGE_COMMENT = "test selenium message comment";
    private static final String NOTIFICATION_MESSAGE_COMMENT_IMPORTANT = "Selenium message - Most Important";
    private static final String NOTIFICATION_TYPE = "Success";
    private static final String NOTIFICATION_SUBJECT = "Email notification test";

    private static final String DETAILS_WINDOW_ID = "_detailsWindow";
    private static final String DICTIONARIES_TAB_ARIA_CONTROLS = "_dictionariesTab";
    private static final String MOST_IMPORTANT_INFO_TAB_ARIA_CONTROLS = "_mostImportantTab";

    private static final String ADD_TO_LIBRARY_LABEL = "Add to Library";
    private static final String ADD_TO_LIBRARY_WIZARD_ID = "addToLibraryPrompt_prompt-card";
    private static final String WIZARD_LIBRARY_TYPE_ID = "{\"identifier\":\"type\",\"parentIdentifier\":\"type\",\"parentValueIdentifier\":\"\",\"groupId\":\"type\"}";
    private static final String WIZARD_INOC_FAILURE_CAUSES = "{\"identifier\":\"INOC_FAILURE_CAUSES\",\"parentIdentifier\":\"type\",\"parentValueIdentifier\":\"\",\"groupId\":\"INOC_FAILURE_CAUSES\"}";
    private static final String WIZARD_FAILURE_SPECIFICATION = "{\"identifier\":\"FAILURE_SPECIFICATION\",\"parentIdentifier\":\"INOC_FAILURE_CAUSES\",\"parentValueIdentifier\":\"ONNET\",\"groupId\":\"INOC_FAILURE_CAUSES\"}";

    private static final String NOTIFICATION_WIZARD_CHANNEL_ID = "channel-component";
    private static final String NOTIFICATION_WIZARD_MESSAGE_ID = "message-component";
    private static final String NOTIFICATION_WIZARD_INTERNAL_TO_ID = "internal-to-component";
    private static final String NOTIFICATION_WIZARD_TO_ID = "to-component";
    private static final String NOTIFICATION_WIZARD_FROM_ID = "from-component";
    private static final String NOTIFICATION_WIZARD_TYPE_ID = "internal-type-component";
    private static final String NOTIFICATION_WIZARD_TEMPLATE_ID = "template-component";
    private static final String NOTIFICATION_WIZARD_SUBJECT_ID = "subject-component";

    private static final String REMAINDER_NOTE = "Selenium Description Note";
    private static final String EDITED_REMAINDER_NOTE = "Edited Remainder Text";
    private static final String PARTICIPANT_FIRST_NAME = "Selenium_Test";
    private static final String PARTICIPANT_NAME_ID = "Name";
    private static final String PARTICIPANT_EMAIL = "Seleniumtest@comarch.com";
    private static final String PARTICIPANT_ROLE = "Notification recipient";
    private static final String PARTICIPANT_ROLE_TABLE = "NOTIFICATION_RECIPIENT";

    private static final String USER_NAME = "sd_selenium_inoc";
    private static final String FILE_TO_UPLOAD_PATH = "DataSourceCSV/CPU_USAGE_INFO_RAW-MAP.xlsx";
    private static final String CSV_FILE = "*CPU_USAGE_INFO_RAW-MAP*.*";

     private static final String STATUS_IN_PROGRESS = "In Progress";

    @BeforeMethod
    public void goToTicketDashboardPage(Method method) {
        ticketDashboardPage = new TicketDashboardPage(driver, webDriverWait).goToPage(driver, BASIC_URL, INOC_DASHBOARD_SUFFIX);
        if (ticketID != null) {
            issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        } else if (!method.getName().equals("createINOCTicket")) {
            Assert.fail("Ticket has not been created.");
        }
    }
    
    @Parameters({"MOIdentifier"})
    @Test(priority = 1, testName = "Create 'INOC Service' Ticket", description = "Create 'INOC Service' Ticket")
    @Description("Create 'INOC Service' Ticket")
    public void createINOCTicket(
            @Optional("TEST_MO_PATH") String MOIdentifier
    ) {
        sdWizardPage = ticketDashboardPage.openCreateTicketWizard("INOC_Service");
        sdWizardPage.getMoStep().enterTextIntoMOIdentifierField(MOIdentifier);
        sdWizardPage.getMoStep().selectRowInMOTable("0");
        sdWizardPage.clickNextButtonInWizard();
        sdWizardPage.insertValueContainsToComponent(TT_PRODUCT, TT_WIZARD_PRODUCT);
        sdWizardPage.insertValueContainsToComponent(TT_SLA_CONTRACT, TT_WIZARD_SLA_CONTRACT);
        sdWizardPage.insertValueContainsToComponent("Test Selenium",TT_WIZARD_INPUT_INCIDENT_TYPE);
        sdWizardPage.enterIncidentDescription(TT_INCIDENT_DESCRIPTION);
        sdWizardPage.insertValueContainsToComponent(TT_REPORTED_INCIDENT_TYPE, TT_WIZARD_REPORTED_INCIDENT_TYPE);
        sdWizardPage.clickNextButtonInWizard();
        sdWizardPage.clickAcceptButtonInWizard();
        ticketID = ticketDashboardPage.getIdFromMessage();
        Assert.assertEquals(ticketDashboardPage.getAttributeFromTable(0, TT_MANAGED_OBJECTS), MOIdentifier);
    }

    @Test(priority = 2, testName = "Edit Ticket Details and check edited description", description = "Edit Ticket Details and check edited description")
    @Description("Edit Ticket Details and check edited description")
    public void editTicketDetailsCheckDescription(
    ) {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        issueDetailsPage.maximizeWindow(DETAILS_WINDOW_ID);
        sdWizardPage = issueDetailsPage.openEditDetailsWizard();
        sdWizardPage.clickNextButtonInWizard();
        sdWizardPage.enterIncidentDescription(TT_DESCRIPTION_EDITED);
        sdWizardPage.clickNextButtonInWizard();
        sdWizardPage.clickNextButtonInWizard();
        sdWizardPage.clickAcceptButtonInWizard();
        issueDetailsPage.minimizeWindow(DETAILS_WINDOW_ID);
        DescriptionTab descriptionTab = issueDetailsPage.selectDescriptionTab();
        Assert.assertEquals(descriptionTab.getTextMessage(), TT_DESCRIPTION_EDITED);
    }

    @Test(priority = 3, testName = "Skip checklist actions and change status", description = "Skip checklist actions and change status")
    @Description("Skip checklist actions and change status")
    public void checkOverview() {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        issueDetailsPage.skipAllActionsOnCheckList();
        Assert.assertTrue(issueDetailsPage.isAllActionsSkipped());
        ticketOverviewTab = (TicketOverviewTab) issueDetailsPage.selectOverviewTab(TROUBLE_TICKET_ISSUE_TYPE);
        ticketOverviewTab.changeTicketStatus(STATUS_IN_PROGRESS);
        Assert.assertEquals(ticketOverviewTab.checkTicketStatus(), STATUS_IN_PROGRESS);
    }

    @Test(priority = 4, testName = "Open Ticket Details from Tickets Search view", description = "Open Ticket Details from Tickets Search view")
    @Description("Open Ticket Details from Tickets Search view")
    public void openTicketDetailsFromTicketsSearchView(
    ) {
        ticketSearchPage = new TicketSearchPage(driver, webDriverWait);
        ticketSearchPage.openView(driver, BASIC_URL);
        ticketSearchPage.filterBy(TICKETS_SEARCH_ASSIGNEE_ATTRIBUTE, "INOC FD");
        String startDate = LocalDateTime.now().minusMinutes(90).format(DATE_TIME_FORMATTER);
        String endDate = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        String date = startDate + " - " + endDate;
        ticketSearchPage.filterBy(CREATION_TIME_ATTRIBUTE, date);
        issueDetailsPage = ticketSearchPage.openIssueDetailsViewFromSearchPage("0", BASIC_URL);
        Assert.assertEquals(issueDetailsPage.getOpenedIssueId(), ticketID);
    }

    @Test(priority = 5, testName = "Add dictionary to ticket", description = "Add dictionary to ticket")
    @Description("Add dictionary to ticket")
    public void addDictionaryToTicket() {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        issueDetailsPage.selectTabFromDetailsWindow(DICTIONARIES_TAB_ARIA_CONTROLS, DICTIONARIES_TAB_LABEL);
        issueDetailsPage.clickContextAction(ADD_TO_LIBRARY_LABEL);
        SDWizardPage dictionarySDWizardPage = new SDWizardPage(driver, webDriverWait, ADD_TO_LIBRARY_WIZARD_ID);
        dictionarySDWizardPage.insertValueToComponent(TT_LIBRARY_TYPE, WIZARD_LIBRARY_TYPE_ID);
        dictionarySDWizardPage.insertValueToComponent(TT_INOC_FAILURE_CAUSES, WIZARD_INOC_FAILURE_CAUSES);
        dictionarySDWizardPage.insertValueToComponent(TT_FAILURE_SPECIFICATION, WIZARD_FAILURE_SPECIFICATION);
        dictionarySDWizardPage.clickAcceptButtonInWizard();
        Assert.assertTrue(issueDetailsPage.checkDictionaryPresence(TT_LIBRARY_TYPE));
    }

    @Parameters("InternalNotificationTo")
    @Test(priority = 6, testName = "Check Messages Tab - add Internal Notification", description = "Check Messages Tab - add Internal Notification")
    @Description("Check Messages Tab - add Internal Notification")
    public void addInternalNotification(
            @Optional("Assurance Test Group") String InternalNotificationTo
    ) {
        messagesTab = issueDetailsPage.selectMessagesTab();
        sdWizardPage = messagesTab.createNewNotificationOnMessagesTab();
        sdWizardPage.insertValueToComponent(NOTIFICATION_CHANNEL_INTERNAL, NOTIFICATION_WIZARD_CHANNEL_ID);
        sdWizardPage.insertValueToComponent(NOTIFICATION_MESSAGE_INTERNAL, NOTIFICATION_WIZARD_MESSAGE_ID);
        sdWizardPage.insertValueToComponent(InternalNotificationTo, NOTIFICATION_WIZARD_INTERNAL_TO_ID);
        sdWizardPage.clickComboBox(NOTIFICATION_WIZARD_TEMPLATE_ID);
        sdWizardPage.insertValueToComponent(NOTIFICATION_TYPE, NOTIFICATION_WIZARD_TYPE_ID);
        sdWizardPage.clickAcceptButtonInWizard();

        Assert.assertFalse(messagesTab.isMessagesTabEmpty());
        Assert.assertEquals(messagesTab.getMessageText(0), NOTIFICATION_MESSAGE_INTERNAL);
        Assert.assertEquals(messagesTab.checkMessageType(0), "NOTIFICATION");
        Assert.assertEquals(messagesTab.getBadgeTextFromMessage(0, 0), "New");
    }

    @Parameters({"NotificationEmailTo", "NotificationEmailFrom"})
    @Test(priority = 7, testName = "Check Messages Tab - add Email Notification", description = "Check Messages Tab - add Email Notification")
    @Description("Check Messages Tab - add Email Notification")
    public void addEmailNotification(
            @Optional("kornelia.odrobinska@comarch.com") String NotificationEmailTo,
            @Optional("iaa.switch2@dresden.cadc.pl") String NotificationEmailFrom
    ) {
        messagesTab = issueDetailsPage.selectMessagesTab();
        sdWizardPage = messagesTab.createNewNotificationOnMessagesTab();
        sdWizardPage.insertValueToComponent(NOTIFICATION_CHANNEL_EMAIL, NOTIFICATION_WIZARD_CHANNEL_ID);
        sdWizardPage.insertValueContainsToComponent(NotificationEmailTo, NOTIFICATION_WIZARD_TO_ID);
        sdWizardPage.insertValueToComponent(NotificationEmailFrom, NOTIFICATION_WIZARD_FROM_ID);
        sdWizardPage.insertValueToComponent(NOTIFICATION_SUBJECT, NOTIFICATION_WIZARD_SUBJECT_ID);
        sdWizardPage.enterEmailMessage(NOTIFICATION_MESSAGE_EMAIL);
        sdWizardPage.clickAcceptButtonInWizard();

        Assert.assertFalse(messagesTab.isMessagesTabEmpty());
        Assert.assertEquals(messagesTab.getMessageText(0), NOTIFICATION_MESSAGE_EMAIL);
        Assert.assertEquals(messagesTab.checkMessageType(0), "NOTIFICATION");
    }

    @Test(priority = 8, testName = "Check Messages Tab - add Internal Comment", description = "Check Messages Tab - add Internal Comment")
    @Description("Check Messages Tab - add Internal Comment")
    public void addInternalComment() {
        messagesTab = issueDetailsPage.selectMessagesTab();
        messagesTab.addComment(NOTIFICATION_MESSAGE_COMMENT + EXTERNAL_TYPE, EXTERNAL_TYPE);
        Assert.assertFalse(messagesTab.isMessagesTabEmpty());
        Assert.assertEquals(messagesTab.checkMessageType(0), TYPE_COMMENT);
        Assert.assertEquals(messagesTab.getMessageText(0), NOTIFICATION_MESSAGE_COMMENT + EXTERNAL_TYPE);
        Assert.assertEquals(messagesTab.checkCommentType(0), EXTERNAL_TYPE.toLowerCase());
    }

    @Test(priority = 9, testName = "Add Remainder", description = "Add Remainder")
    @Description("Add Remainder")
    public void addRemainderTest() {
        ticketOverviewTab = (TicketOverviewTab) issueDetailsPage.selectOverviewTab(TROUBLE_TICKET_ISSUE_TYPE);
        ticketOverviewTab.maximizeWindow(DETAILS_TABS_CONTAINER_ID);
        remainderForm = ticketOverviewTab.clickAddRemainder();
        remainderForm.createReminderWithNote(REMAINDER_NOTE);
        moreDetailsPage = ticketOverviewTab.clickMoreDetails();
        Assert.assertTrue(moreDetailsPage.isNoteInLogsTable(REMAINDER_NOTE));
        ticketDashboardPage = new TicketDashboardPage(driver, webDriverWait).goToPage(driver, BASIC_URL);
        Assert.assertTrue(ticketDashboardPage.isReminderPresent(ticketDashboardPage.getRowForIssueWithID(ticketID), REMAINDER_NOTE));
    }

    @Test(priority = 10, testName = "Edit Reminder", description = "Edit Reminder")
    @Description("Edit Reminder")
    public void editRemainderTest() {
        ticketOverviewTab = (TicketOverviewTab) issueDetailsPage.selectOverviewTab(TROUBLE_TICKET_ISSUE_TYPE);
        ticketOverviewTab.maximizeWindow(DETAILS_TABS_CONTAINER_ID);
        remainderForm = ticketOverviewTab.clickEditRemainder();
        remainderForm.createReminderWithNote(EDITED_REMAINDER_NOTE);
        ticketDashboardPage = new TicketDashboardPage(driver, webDriverWait).goToPage(driver, BASIC_URL);
        Assert.assertTrue(ticketDashboardPage.isReminderPresent(ticketDashboardPage.getRowForIssueWithID(ticketID), EDITED_REMAINDER_NOTE));
    }

    @Test(priority = 11, testName = "Delete Remainder", description = "Delete Remainder")
    @Description("Delete Remainder")
    public void deleteRemainderTest() {
        ticketOverviewTab = (TicketOverviewTab) issueDetailsPage.selectOverviewTab(TROUBLE_TICKET_ISSUE_TYPE);
        ticketOverviewTab.maximizeWindow(DETAILS_TABS_CONTAINER_ID);
        ticketOverviewTab.clickRemoveRemainder();
        ticketDashboardPage = new TicketDashboardPage(driver, webDriverWait).goToPage(driver, BASIC_URL);
        Assert.assertFalse(ticketDashboardPage.isReminderPresent(ticketDashboardPage.getRowForIssueWithID(ticketID), EDITED_REMAINDER_NOTE));
    }

    @Parameters({"RelatedTicketID"})
    @Test(priority = 12, testName = "Check Related Tickets Tab - link Ticket", description = "Check Related Tickets Tab - link Ticket")
    @Description("Check Related Tickets Tab - link Ticket")
    public void linkTicketToTicket(@Optional("10594") String RelatedTicketID) {
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
    public void showArchived(@Optional("10594") String RelatedTicketID) {
        relatedTicketsTab = issueDetailsPage.selectRelatedTicketsTab();
        relatedTicketsTab.turnOnShowArchived();

        Assert.assertEquals(relatedTicketsTab.checkRelatedIssueId(0), RelatedTicketID);
    }

    @Parameters({"SecondMOIdentifier"})
    @Test(priority = 16, testName = "Add second MO in Root Causes tab", description = "Add second MO in Root Causes tab")
    @Description("Add second MO in Root Causes tab")
    public void addRootCause(@Optional("OL3//1/Duesseldorf 293 - Dortmund 116 (05-027440)") String SecondMOIdentifier) {
        rootCausesTab = issueDetailsPage.selectRootCauseTab();
        sdWizardPage = rootCausesTab.openAddRootCauseWizard();
        sdWizardPage.getMoStep().showAllMOs();
        sdWizardPage.getMoStep().enterTextIntoMOIdentifierField(SecondMOIdentifier);
        sdWizardPage.getMoStep().selectObjectInMOTable(SecondMOIdentifier);
        sdWizardPage.clickAcceptButtonInWizard();

        Assert.assertTrue(rootCausesTab.checkIfMOIdentifierIsPresentOnRootCauses(SecondMOIdentifier));
    }

    @Test(priority = 17, testName = "Check Participants", description = "Check Participants Tab - add Participant")
    @Description("Check Participants Tab - add Participant")
    public void addParticipant() {
        participantsTab = issueDetailsPage.selectParticipantsTab();
        participantsPromptPage = participantsTab.clickAddParticipant();
        participantsPromptPage.setParticipantNameDTAG(PARTICIPANT_FIRST_NAME);
        participantsPromptPage.setParticipantEmail(PARTICIPANT_EMAIL);
        participantsPromptPage.setParticipantRole(PARTICIPANT_ROLE);
        participantsPromptPage.clickAddParticipantInPrompt();
        int newParticipantRow = participantsTab.participantRowByAttribute(PARTICIPANT_FIRST_NAME, PARTICIPANT_NAME_ID);

        Assert.assertEquals(participantsTab.checkParticipantRole(newParticipantRow), PARTICIPANT_ROLE_TABLE);
    }

    @Test(priority = 18, testName = "Add attachment to ticket", description = "Add attachment to ticket")
    @Description("Add attachment to ticket")
    public void addAttachment() {
        issueDetailsPage.maximizeWindow(DETAILS_TABS_CONTAINER_ID);
        attachmentsTab = issueDetailsPage.selectAttachmentsTab(DETAILS_TABS_CONTAINER_ID);

        Assert.assertTrue(attachmentsTab.isAttachmentListEmpty());
        attachmentsTab.addAttachment(FILE_TO_UPLOAD_PATH);

        Assert.assertFalse(attachmentsTab.isAttachmentListEmpty());
        Assert.assertEquals(attachmentsTab.getAttachmentOwner(), USER_NAME);
    }

    @Test(priority = 19, testName = "Download Attachment", description = "Download the Attachment from Attachment tab in Ticket Details")
    @Description("Download the Attachment from Attachment tab in Ticket Details")
    public void downloadAttachment() {
        issueDetailsPage.maximizeWindow(DETAILS_TABS_CONTAINER_ID);
        attachmentsTab = issueDetailsPage.selectAttachmentsTab(DETAILS_TABS_CONTAINER_ID);

        Assert.assertFalse(attachmentsTab.isAttachmentListEmpty());
        attachmentsTab.clickDownloadAttachment();
        issueDetailsPage.attachFileToReport(CSV_FILE);

        Assert.assertTrue(issueDetailsPage.checkIfFileIsNotEmpty(CSV_FILE));
    }

    @Test(priority = 20, testName = "Delete Attachment", description = "Delete the Attachment from Attachment tab in Ticket Details")
    @Description("Delete the Attachment from Attachment tab in Ticket Details")
    public void deleteAttachment() {
        issueDetailsPage.maximizeWindow(DETAILS_TABS_CONTAINER_ID);
        attachmentsTab = issueDetailsPage.selectAttachmentsTab(DETAILS_TABS_CONTAINER_ID);

        Assert.assertFalse(attachmentsTab.isAttachmentListEmpty());
        attachmentsTab.clickDeleteAttachment();

        Assert.assertTrue(attachmentsTab.isAttachmentListEmpty());
    }

    @Test(priority = 21, testName = "Most Important Info test", description = "Mark comment as important and check if it's displayed in most important info tab")
    @Description("Mark comment as important and check if it's displayed in most important info tab")
    public void mostImportantInfoTest() {
        messagesTab = issueDetailsPage.selectMessagesTab();
        messagesTab.addComment(NOTIFICATION_MESSAGE_COMMENT_IMPORTANT, INTERNAL_TYPE);
        Assert.assertFalse(messagesTab.isMessagesTabEmpty());
        Assert.assertEquals(messagesTab.getMessageText(0), NOTIFICATION_MESSAGE_COMMENT_IMPORTANT);

        messagesTab.markAsImportant(0);
        Assert.assertEquals(messagesTab.getBadgeTextFromMessage(0, 1), "IMPORTANT");

        issueDetailsPage.selectTabFromTablesWindow(MOST_IMPORTANT_INFO_TAB_ARIA_CONTROLS, MOST_IMPORTANT_INFO_TAB_LABEL);
        mostImportantInfoTab = new MessagesTab(driver, webDriverWait);
        Assert.assertFalse(mostImportantInfoTab.isMessagesTabEmpty());
        Assert.assertEquals(mostImportantInfoTab.getMessageText(0), NOTIFICATION_MESSAGE_COMMENT_IMPORTANT);
        Assert.assertEquals(mostImportantInfoTab.getBadgeTextFromMessage(0, 1), "IMPORTANT");
    }
}