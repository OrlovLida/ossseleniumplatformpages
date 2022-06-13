package com.oss.servicedesk;

import java.time.LocalDateTime;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.servicedesk.BaseSearchPage;
import com.oss.pages.servicedesk.issue.IssueDetailsPage;
import com.oss.pages.servicedesk.issue.MoreDetailsPage;
import com.oss.pages.servicedesk.issue.RemainderForm;
import com.oss.pages.servicedesk.issue.tabs.AffectedTab;
import com.oss.pages.servicedesk.issue.tabs.AttachmentsTab;
import com.oss.pages.servicedesk.issue.tabs.ExternalTab;
import com.oss.pages.servicedesk.issue.tabs.MessagesTab;
import com.oss.pages.servicedesk.issue.tabs.ParticipantsTab;
import com.oss.pages.servicedesk.issue.tabs.RelatedProblemsTab;
import com.oss.pages.servicedesk.issue.tabs.RelatedTicketsTab;
import com.oss.pages.servicedesk.issue.tabs.RootCausesTab;
import com.oss.pages.servicedesk.issue.ticket.TicketDashboardPage;
import com.oss.pages.servicedesk.issue.ticket.TicketOverviewTab;
import com.oss.pages.servicedesk.issue.ticket.TicketSearchPage;
import com.oss.pages.servicedesk.issue.wizard.ExternalPromptPage;
import com.oss.pages.servicedesk.issue.wizard.SDWizardPage;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

import static com.oss.pages.servicedesk.BaseSDPage.DATE_TIME_FORMATTER;
import static com.oss.pages.servicedesk.ServiceDeskConstants.CSV_FILE;
import static com.oss.pages.servicedesk.ServiceDeskConstants.DESCRIPTION_TAB_LABEL;
import static com.oss.pages.servicedesk.ServiceDeskConstants.DETAILS_TABS_CONTAINER_ID;
import static com.oss.pages.servicedesk.ServiceDeskConstants.DICTIONARIES_TAB_LABEL;
import static com.oss.pages.servicedesk.ServiceDeskConstants.FILE_TO_UPLOAD_PATH;
import static com.oss.pages.servicedesk.ServiceDeskConstants.ISSUE_OUT_ASSIGNEE_ATTR;
import static com.oss.pages.servicedesk.ServiceDeskConstants.ISSUE_OUT_STATUS_ATTR;
import static com.oss.pages.servicedesk.ServiceDeskConstants.MOST_IMPORTANT_INFO_TAB_LABEL;
import static com.oss.pages.servicedesk.ServiceDeskConstants.TROUBLE_TICKET_ISSUE_TYPE;
import static com.oss.pages.servicedesk.ServiceDeskConstants.USER_NAME;

@Listeners({TestListener.class})
public class TicketsTest extends BaseTestCase {

    private TicketDashboardPage ticketDashboardPage;
    private TicketSearchPage ticketSearchPage;
    private SDWizardPage sdWizardPage;
    private IssueDetailsPage issueDetailsPage;
    private TicketOverviewTab ticketOverviewTab;
    private MessagesTab messagesTab;
    private MoreDetailsPage moreDetailsPage;
    private RemainderForm remainderForm;
    private MessagesTab mostImportantInfoTab;
    private AttachmentsTab attachmentsTab;
    private ExternalTab externalTab;
    private ExternalPromptPage externalPromptPage;
    private RootCausesTab rootCausesTab;
    private RelatedTicketsTab relatedTicketsTab;
    private ParticipantsTab participantsTab;
    private RelatedProblemsTab relatedProblemsTab;
    private AffectedTab affectedTab;
    private String ticketID = "157";

    private static final String TT_DESCRIPTION = "TestSelenium";
    private static final String TT_DESCRIPTION_EDITED = "TestSelenium_edited";
    private static final String DESCRIPTION_FIELD_ID = "_incidentDescriptionApp";
    private static final String TT_CORRELATION_ID = "12345";
    private static final String TT_REFERENCE_ID = "12345";
    private static final String TT_SEVERITY = "Warning";
    private static final String TT_EXTERNAL = "Selenium test external";
    private static final String TT_EXTERNAL_URL = "http://test.pl";
    private static final String TT_EXTERNAL_EDITED = "Selenium test external_EDITED";
    private static final String TT_LIBRARY_TYPE = "Category";
    private static final String TT_CATEGORY_NAME = "Data Problem";

    private static final String TICKET_OUT_PREFIX = "ticketOut";
    private static final String TICKETS_SEARCH_ASSIGNEE_ATTRIBUTE = TICKET_OUT_PREFIX + "." + ISSUE_OUT_ASSIGNEE_ATTR;
    private static final String TICKETS_SEARCH_STATUS_ATTRIBUTE = TICKET_OUT_PREFIX + "." + ISSUE_OUT_STATUS_ATTR;

    private static final String NOTIFICATION_CHANNEL_INTERNAL = "Internal";
    private static final String NOTIFICATION_CHANNEL_EMAIL = "E-mail";
    private static final String NOTIFICATION_MESSAGE_INTERNAL = "test selenium message internal";
    private static final String NOTIFICATION_MESSAGE_EMAIL = "test selenium message E-mail";
    private static final String NOTIFICATION_MESSAGE_COMMENT = "test selenium message comment";
    private static final String NOTIFICATION_MESSAGE_COMMENT_IMPORTANT = "Selenium message - Most Important";
    private static final String NOTIFICATION_INTERNAL_TO = "ca_kodrobinska";
    private static final String NOTIFICATION_TYPE = "Success";
    private static final String NOTIFICATION_SUBJECT = "Email notification test";

    private static final String TT_WIZARD_ASSIGNEE = "TT_WIZARD_INPUT_ASSIGNEE_LABEL";
    private static final String TT_WIZARD_ESCALATED_TO = "TT_WIZARD_INPUT_ESCALATED_TO_LABEL";
    private static final String TT_WIZARD_SEVERITY = "TT_WIZARD_INPUT_SEVERITY_LABEL";
    private static final String TT_WIZARD_CORRELATION_ID = "ISSUE_CORRELATION_ID";
    private static final String TT_WIZARD_REFERENCE_ID = "TT_WIZARD_INPUT_REFERENCE_ID_LABEL";
    private static final String TT_WIZARD_ISSUE_START_DATE_ID = "IssueStartDate";
    private static final String TT_WIZARD_MESSAGE_DATE_ID = "PleaseProvideTheTimeOnTheHandsetTheTxtMessageArrived";

    private static final String TYPE_ATTRIBUTE = "Type";
    private static final String TICKET_OPENED_LOG = "type CTT has been created and set to status New.";
    private static final String STATUS_ACKNOWLEDGED = "Acknowledged";
    private static final String DICTIONARIES_TAB_ARIA_CONTROLS = "_dictionariesTab";
    private static final String DESCRIPTION_TAB_ARIA_CONTROLS = "_descriptionTab";
    private static final String MOST_IMPORTANT_INFO_TAB_ARIA_CONTROLS = "_mostImportantTab";
    private static final String SAME_MO_TT_TAB_ARIA_CONTROLS = "_sameMOTTTab";

    private static final String EXTERNAL_LIST_ID = "_detailsExternalsListApp";
    private static final String ADD_TO_LIBRARY_LABEL = "Add to Library";
    private static final String ADD_TO_LIBRARY_WIZARD_ID = "addToLibraryPrompt_prompt-card";
    private static final String WIZARD_LIBRARY_TYPE_ID = "{\"identifier\":\"type\",\"parentIdentifier\":\"type\",\"parentValueIdentifier\":\"\",\"groupId\":\"type\"}";
    private static final String WIZARD_CATEGORY_ID = "{\"identifier\":\"Category\",\"parentIdentifier\":\"type\",\"parentValueIdentifier\":\"\",\"groupId\":\"Category\"}";
    private static final String TABLES_WINDOW_ID = "_tablesWindow";

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
    private static final String PARTICIPANT_FIRST_NAME = "SeleniumTest";
    private static final String PARTICIPANT_FIRST_NAME_EDITED = "SeleniumTestEdited";
    private static final String PARTICIPANT_SURNAME = LocalDateTime.now().toString();
    private static final String PARTICIPANT_ROLE = "Contact";

    private static final String CREATE_PROBLEM_WIZARD_NAME_ID = "TT_WIZARD_INPUT_PROBLEM_NAME_DESCRIPTION";
    private static final String CREATE_PROBLEM_WIZARD_ASSIGNEE_ID = "TT_WIZARD_INPUT_ASSIGNEE_LABEL";
    private static final String CREATE_PROBLEM_NAME = "Related Problem - Selenium Test";
    private static final String CREATE_PROBLEM_ASSIGNEE = "sd_seleniumtest";
    private static final String SAME_MO_TT_TAB_LABEL = "Same MO TT";

    @BeforeMethod
    public void goToTicketDashboardPage() {
        ticketDashboardPage = new TicketDashboardPage(driver, webDriverWait).goToPage(driver, BASIC_URL);
    }

    @Parameters({"MOIdentifier", "ttAssignee", "EscalatedTo1"})
    @Test(priority = 1, testName = "Create CTT Ticket", description = "Create CTT Ticket")
    @Description("Create CTT Ticket")
    public void createCTTTicket(
            @Optional("CFS_Access_Product_Selenium_1") String MOIdentifier,
            @Optional("ca_akolczyk") String ttAssignee,
            @Optional("ca_akolczyk") String EscalatedTo1
    ) {
        sdWizardPage = ticketDashboardPage.openCreateTicketWizard("CTT");
        sdWizardPage.getMoStep().enterTextIntoSearchComponent(MOIdentifier);
        sdWizardPage.getMoStep().selectObjectInMOTable(MOIdentifier);
        sdWizardPage.clickNextButtonInWizard();
        sdWizardPage.insertValueToSearchComponent(ttAssignee, TT_WIZARD_ASSIGNEE);
        sdWizardPage.insertValueToSearchComponent(EscalatedTo1, TT_WIZARD_ESCALATED_TO);
        sdWizardPage.insertValueToTextComponent(TT_REFERENCE_ID, TT_WIZARD_REFERENCE_ID);
        sdWizardPage.enterIncidentDescription(TT_DESCRIPTION);
        sdWizardPage.enterExpectedResolutionDate();
        sdWizardPage.insertValueToTextComponent(TT_CORRELATION_ID, TT_WIZARD_CORRELATION_ID);
        sdWizardPage.clickNextButtonInWizard();
        String date = LocalDateTime.now().minusMinutes(5).format(DATE_TIME_FORMATTER);
        sdWizardPage.insertValueToTextComponent(date, TT_WIZARD_ISSUE_START_DATE_ID);
        sdWizardPage.insertValueToTextComponent(date, TT_WIZARD_MESSAGE_DATE_ID);
        sdWizardPage.clickAcceptButtonInWizard();
        ticketID = ticketDashboardPage.getIssueIdWithAssignee(ttAssignee);
        Assert.assertEquals(ticketDashboardPage.getAssigneeForNthTicketInTable(0), ttAssignee);
    }

    @Test(priority = 2, testName = "Check Attributes", description = "Check Attributes")
    @Description("Check Attributes")
    public void checkAttributes() {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        ticketOverviewTab = (TicketOverviewTab) issueDetailsPage.selectOverviewTab(TROUBLE_TICKET_ISSUE_TYPE);
        ticketOverviewTab.maximizeWindow(DETAILS_TABS_CONTAINER_ID);
        moreDetailsPage = ticketOverviewTab.clickMoreDetails();
        Assert.assertEquals(moreDetailsPage.checkValueOfAttribute(TYPE_ATTRIBUTE), "CTT");
    }

    @Test(priority = 3, testName = "Check Logs", description = "Check Logs")
    @Description("Check Logs")
    public void checkLogs() {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        ticketOverviewTab = (TicketOverviewTab) issueDetailsPage.selectOverviewTab(TROUBLE_TICKET_ISSUE_TYPE);
        ticketOverviewTab.maximizeWindow(DETAILS_TABS_CONTAINER_ID);
        moreDetailsPage = ticketOverviewTab.clickMoreDetails();
        Assert.assertTrue(moreDetailsPage.isNoteInLogsTable(TICKET_OPENED_LOG));
    }

    @Parameters({"NewAssignee", "EscalatedTo"})
    @Test(priority = 4, testName = "Edit Ticket Details", description = "Edit Ticket Details")
    @Description("Edit Ticket Details")
    public void editTicketDetails(
            @Optional("Tier2_Mobile") String NewAssignee,
            @Optional("admin oss") String EscalatedTo
    ) {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        issueDetailsPage.maximizeWindow(DETAILS_TABS_CONTAINER_ID);
        ticketID = issueDetailsPage.getOpenedIssueId();
        ticketOverviewTab = (TicketOverviewTab) issueDetailsPage.selectOverviewTab(TROUBLE_TICKET_ISSUE_TYPE);
        ticketOverviewTab.allowEditingTicket();
        sdWizardPage = ticketOverviewTab.openEditIssueWizard();
        sdWizardPage.clickNextButtonInWizard();
        sdWizardPage.insertValueToSearchComponent(NewAssignee, TT_WIZARD_ASSIGNEE);
        sdWizardPage.enterIncidentDescription(TT_DESCRIPTION_EDITED);
        sdWizardPage.insertValueToSearchComponent(EscalatedTo, TT_WIZARD_ESCALATED_TO);
        sdWizardPage.insertValueToComboBoxComponent(TT_SEVERITY, TT_WIZARD_SEVERITY);
        sdWizardPage.clickNextButtonInWizard();
        sdWizardPage.clickAcceptButtonInWizard();
    }

    @Test(priority = 5, testName = "Skip checklist actions and change status", description = "Skip checklist actions and change status")
    @Description("Skip checklist actions and change status")
    public void checkOverview() {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        issueDetailsPage.skipAllActionsOnCheckList();
        Assert.assertTrue(issueDetailsPage.isAllActionsSkipped());
        ticketOverviewTab = (TicketOverviewTab) issueDetailsPage.selectOverviewTab(TROUBLE_TICKET_ISSUE_TYPE);
        ticketOverviewTab.changeTicketStatus(STATUS_ACKNOWLEDGED);
        Assert.assertEquals(ticketOverviewTab.checkTicketStatus(), STATUS_ACKNOWLEDGED);
    }

    @Parameters({"NewAssignee"})
    @Test(priority = 6, testName = "Open Ticket Details from Tickets Search view", description = "Open Ticket Details from Tickets Search view")
    @Description("Open Ticket Details from Tickets Search view")
    public void openTicketDetailsFromTicketsSearchView(
            @Optional("Tier2_Mobile") String NewAssignee
    ) {
        ticketSearchPage = new TicketSearchPage(driver, webDriverWait);
        ticketSearchPage.openView(driver, BASIC_URL);
        ticketSearchPage.filterBy(TICKETS_SEARCH_ASSIGNEE_ATTRIBUTE, NewAssignee);
        String startDate = LocalDateTime.now().minusMinutes(10).format(DATE_TIME_FORMATTER);
        String endDate = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        String date = startDate + " - " + endDate;
        ticketSearchPage.filterByDate(BaseSearchPage.CREATION_TIME_ATTRIBUTE, date);
        ticketSearchPage.filterBy(BaseSearchPage.DESCRIPTION_ATTRIBUTE, TT_DESCRIPTION_EDITED);
        ticketSearchPage.filterBy(TICKETS_SEARCH_STATUS_ATTRIBUTE, STATUS_ACKNOWLEDGED);
        issueDetailsPage = ticketSearchPage.openIssueDetailsViewFromSearchPage("0", BASIC_URL);
        Assert.assertEquals(issueDetailsPage.getOpenedIssueId(), ticketID);
    }

    @Test(priority = 7, testName = "Add External to ticket", description = "Add External to ticket")
    @Description("Add External to ticket")
    public void addExternalToTicket() {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        externalTab = issueDetailsPage.selectExternalTab();
        externalPromptPage = externalTab.clickAddExternal();
        externalPromptPage.fillExternalName(TT_EXTERNAL);
        externalPromptPage.fillExternalUrl(TT_EXTERNAL_URL);
        externalPromptPage.clickCreateExternal();
        Assert.assertTrue(externalTab.isExternalCreated(TT_EXTERNAL, EXTERNAL_LIST_ID));
    }

    @Test(priority = 8, testName = "Edit External in Problem", description = "Edit External in Ticket")
    @Description("Edit External in Ticket")
    public void editExternalInTicket() {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        externalTab = issueDetailsPage.selectExternalTab();
        Assert.assertTrue(externalTab.isExternalCreated(TT_EXTERNAL, EXTERNAL_LIST_ID));

        externalPromptPage = externalTab.clickEditExternal(EXTERNAL_LIST_ID);
        externalPromptPage.fillExternalName(TT_EXTERNAL_EDITED);
        externalPromptPage.clickSave();
        Assert.assertTrue(externalTab.isExternalCreated(TT_EXTERNAL_EDITED, EXTERNAL_LIST_ID));
    }

    @Test(priority = 9, testName = "Delete External", description = "Delete External in Ticket")
    @Description("Delete External in Ticket")
    public void deleteExternalInTicket() {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        externalTab = issueDetailsPage.selectExternalTab();
        Assert.assertTrue(externalTab.isExternalCreated(TT_EXTERNAL_EDITED, EXTERNAL_LIST_ID));

        externalTab.clickDeleteExternal(EXTERNAL_LIST_ID);
        Assert.assertTrue(externalTab.isExternalListEmpty(EXTERNAL_LIST_ID));
    }

    @Test(priority = 10, testName = "Add dictionary to ticket", description = "Add dictionary to ticket")
    @Description("Add dictionary to ticket")
    public void addDictionaryToTicket() {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        issueDetailsPage.selectTabFromDetailsWindow(DICTIONARIES_TAB_ARIA_CONTROLS, DICTIONARIES_TAB_LABEL);
        issueDetailsPage.clickContextAction(ADD_TO_LIBRARY_LABEL);
        SDWizardPage dictionarySDWizardPage = new SDWizardPage(driver, webDriverWait, ADD_TO_LIBRARY_WIZARD_ID);
        dictionarySDWizardPage.insertValueToComboBoxComponent(TT_LIBRARY_TYPE, WIZARD_LIBRARY_TYPE_ID);
        dictionarySDWizardPage.insertValueToComboBoxComponent(TT_CATEGORY_NAME, WIZARD_CATEGORY_ID);
        dictionarySDWizardPage.clickAcceptButtonInWizard();
        Assert.assertEquals(issueDetailsPage.checkExistingDictionary(), TT_CATEGORY_NAME);
    }

    @Test(priority = 11, testName = "Check Description Tab", description = "Check Description Tab")
    @Description("Check Description Tab")
    public void checkDescriptionTab() {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        issueDetailsPage.selectTabFromTablesWindow(DESCRIPTION_TAB_ARIA_CONTROLS, DESCRIPTION_TAB_LABEL);
        Assert.assertEquals(issueDetailsPage.getDisplayedText(TABLES_WINDOW_ID, DESCRIPTION_FIELD_ID), TT_DESCRIPTION_EDITED);
    }

    @Test(priority = 12, testName = "Check Messages Tab - add Internal Notification", description = "Check Messages Tab - add Internal Notification")
    @Description("Check Messages Tab - add Internal Notification")
    public void addInternalNotification() {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        messagesTab = issueDetailsPage.selectMessagesTab();
        sdWizardPage = messagesTab.createNewNotificationOnMessagesTab();
        sdWizardPage.insertValueToComboBoxComponent(NOTIFICATION_CHANNEL_INTERNAL, NOTIFICATION_WIZARD_CHANNEL_ID);
        sdWizardPage.insertValueToTextAreaComponent(NOTIFICATION_MESSAGE_INTERNAL, NOTIFICATION_WIZARD_MESSAGE_ID);
        sdWizardPage.insertValueContainsToMultiComboBox(NOTIFICATION_INTERNAL_TO, NOTIFICATION_WIZARD_INTERNAL_TO_ID);
        sdWizardPage.clickComboBox(NOTIFICATION_WIZARD_TEMPLATE_ID);
        sdWizardPage.insertValueToComboBoxComponent(NOTIFICATION_TYPE, NOTIFICATION_WIZARD_TYPE_ID);
        sdWizardPage.clickAcceptButtonInWizard();

        Assert.assertFalse(messagesTab.isMessagesTabEmpty());
        Assert.assertEquals(messagesTab.getMessageText(0), NOTIFICATION_MESSAGE_INTERNAL);
        Assert.assertEquals(messagesTab.checkMessageType(0), "NOTIFICATION");
        Assert.assertEquals(messagesTab.getBadgeTextFromMessage(0, 0), "New");
    }

    @Parameters({"NotificationEmailTo", "NotificationEmailFrom"})
    @Test(priority = 13, testName = "Check Messages Tab - add Email Notification", description = "Check Messages Tab - add Email Notification")
    @Description("Check Messages Tab - add Email Notification")
    public void addEmailNotification(
            @Optional("kornelia.odrobinska@comarch.com") String NotificationEmailTo,
            @Optional("Test@AIF.pl") String NotificationEmailFrom
    ) {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        messagesTab = issueDetailsPage.selectMessagesTab();
        sdWizardPage = messagesTab.createNewNotificationOnMessagesTab();
        sdWizardPage.insertValueToComboBoxComponent(NOTIFICATION_CHANNEL_EMAIL, NOTIFICATION_WIZARD_CHANNEL_ID);
        sdWizardPage.insertValueToMultiSearchComponent(NotificationEmailTo, NOTIFICATION_WIZARD_TO_ID);
        sdWizardPage.insertValueToComboBoxComponent(NotificationEmailFrom, NOTIFICATION_WIZARD_FROM_ID);
        sdWizardPage.insertValueToTextComponent(NOTIFICATION_SUBJECT, NOTIFICATION_WIZARD_SUBJECT_ID);
        sdWizardPage.enterEmailMessage(NOTIFICATION_MESSAGE_EMAIL);
        sdWizardPage.clickAcceptButtonInWizard();

        Assert.assertFalse(messagesTab.isMessagesTabEmpty());
        Assert.assertEquals(messagesTab.getMessageText(0), NOTIFICATION_MESSAGE_EMAIL);
        Assert.assertEquals(messagesTab.checkMessageType(0), "NOTIFICATION");
    }

    @Test(priority = 14, testName = "Check Messages Tab - add Internal Comment", description = "Check Messages Tab - add Internal Comment")
    @Description("Check Messages Tab - add Internal Comment")
    public void addInternalComment() {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        messagesTab = issueDetailsPage.selectMessagesTab();
        messagesTab.clickCreateNewCommentButton();
        // TODO dodac wybieranie typu komentarza
        messagesTab.enterCommentMessage(NOTIFICATION_MESSAGE_COMMENT);
        messagesTab.clickCreateCommentButton();
        Assert.assertFalse(messagesTab.isMessagesTabEmpty());
        Assert.assertEquals(messagesTab.checkMessageType(0), "COMMENT");
        Assert.assertEquals(messagesTab.getMessageText(0), NOTIFICATION_MESSAGE_COMMENT);
        Assert.assertEquals(messagesTab.checkCommentType(0), "internal");
    }

    @Test(priority = 15, testName = "Add Remainder", description = "Add Remainder")
    @Description("Add Remainder")
    public void addRemainderTest() {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        ticketOverviewTab = (TicketOverviewTab) issueDetailsPage.selectOverviewTab(TROUBLE_TICKET_ISSUE_TYPE);
        ticketOverviewTab.maximizeWindow(DETAILS_TABS_CONTAINER_ID);
        remainderForm = ticketOverviewTab.clickAddRemainder();
        remainderForm.createReminderWithNote(REMAINDER_NOTE);
        moreDetailsPage = ticketOverviewTab.clickMoreDetails();
        Assert.assertTrue(moreDetailsPage.isNoteInLogsTable(REMAINDER_NOTE));
        goToTicketDashboardPage();
        Assert.assertTrue(ticketDashboardPage.isReminderPresent(ticketDashboardPage.getRowForIssueWithID(ticketID), REMAINDER_NOTE));
    }

    @Test(priority = 16, testName = "Edit Reminder", description = "Edit Reminder")
    @Description("Edit Reminder")
    public void editRemainderTest() {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        ticketOverviewTab = (TicketOverviewTab) issueDetailsPage.selectOverviewTab(TROUBLE_TICKET_ISSUE_TYPE);
        ticketOverviewTab.maximizeWindow(DETAILS_TABS_CONTAINER_ID);
        remainderForm = ticketOverviewTab.clickEditRemainder();
        remainderForm.createReminderWithNote(EDITED_REMAINDER_NOTE);
        goToTicketDashboardPage();
        Assert.assertTrue(ticketDashboardPage.isReminderPresent(ticketDashboardPage.getRowForIssueWithID(ticketID), EDITED_REMAINDER_NOTE));
    }

    @Test(priority = 17, testName = "Delete Remainder", description = "Delete Remainder")
    @Description("Delete Remainder")
    public void deleteRemainderTest() {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        ticketOverviewTab = (TicketOverviewTab) issueDetailsPage.selectOverviewTab(TROUBLE_TICKET_ISSUE_TYPE);
        ticketOverviewTab.maximizeWindow(DETAILS_TABS_CONTAINER_ID);
        ticketOverviewTab.clickRemoveRemainder();
        goToTicketDashboardPage();
        Assert.assertFalse(ticketDashboardPage.isReminderPresent(ticketDashboardPage.getRowForIssueWithID(ticketID), EDITED_REMAINDER_NOTE));
    }

    @Parameters({"RelatedTicketID"})
    @Test(priority = 18, testName = "Check Related Tickets Tab - link Ticket", description = "Check Related Tickets Tab - link Ticket")
    @Description("Check Related Tickets Tab - link Ticket")
    public void linkTicketToTicket(@Optional("100") String RelatedTicketID) {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        relatedTicketsTab = issueDetailsPage.selectRelatedTicketsTab();
        relatedTicketsTab.linkIssue(RelatedTicketID, "issueIdsToLink");

        Assert.assertEquals(relatedTicketsTab.checkRelatedIssueId(0), RelatedTicketID);
    }

    @Test(priority = 19, testName = "Export Related Tickets", description = "Export Related Tickets")
    @Description("Export Related Tickets")
    public void exportRelatedTickets() {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        relatedTicketsTab = issueDetailsPage.selectRelatedTicketsTab();
        relatedTicketsTab.exportFromTabTable();
        int amountOfNotifications = relatedTicketsTab.openNotificationPanel().amountOfNotifications();

        Assert.assertEquals(amountOfNotifications, 0);
        Assert.assertTrue(relatedTicketsTab.isRelatedIssuesFileNotEmpty());
    }

    @Test(priority = 20, testName = "Check Related Tickets Tab - unlink Ticket", description = "Check Related Tickets Tab - unlink Ticket")
    @Description("Check Related Tickets Tab - unlink Ticket")
    public void unlinkTicketFromTicket() {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        relatedTicketsTab = issueDetailsPage.selectRelatedTicketsTab();
        relatedTicketsTab.selectIssue(0);
        relatedTicketsTab.unlinkIssue();
        relatedTicketsTab.confirmUnlinking();

        Assert.assertTrue(relatedTicketsTab.isRelatedIssueTableEmpty());
    }

    @Parameters({"RelatedTicketID"})
    @Test(priority = 21, testName = "Check Related Tickets Tab - show archived switcher", description = "Check Related Tickets Tab - show archived switcher")
    @Description("Check Related Tickets Tab - show archived switcher")
    public void showArchived(@Optional("100") String RelatedTicketID) {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        relatedTicketsTab = issueDetailsPage.selectRelatedTicketsTab();
        relatedTicketsTab.turnOnShowArchived();

        Assert.assertEquals(relatedTicketsTab.checkRelatedIssueId(0), RelatedTicketID);
    }

    @Test(priority = 22, testName = "Check Same MO TT Tab", description = "Check Same MO TT Tab")
    @Description("Check Same MO TT Tab")
    public void checkSameMOTTTab() {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        issueDetailsPage.selectTabFromTablesWindow(SAME_MO_TT_TAB_ARIA_CONTROLS, SAME_MO_TT_TAB_LABEL);
        Assert.assertTrue(issueDetailsPage.checkIfSameMOTTTableIsNotEmpty());
    }

    @Test(priority = 23, testName = "Check Root Causes Tree Table", description = "Check Root Causes Tree Table")
    @Description("Check Root Causes Tree Table")
    public void checkRootCause() {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        rootCausesTab = issueDetailsPage.selectRootCauseTab();
        Assert.assertFalse(rootCausesTab.isRootCauseTableEmpty());

        rootCausesTab.selectMOInRootCausesTab(0);
        Assert.assertTrue(rootCausesTab.checkIfRootCausesTreeTableIsNotEmpty());
    }

    @Parameters({"SecondMOIdentifier"})
    @Test(priority = 24, testName = "Add second MO in Root Causes tab", description = "Add second MO in Root Causes tab")
    @Description("Add second MO in Root Causes tab")
    public void addRootCause(@Optional("CFS_Access_Product_Selenium_2") String SecondMOIdentifier) {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        rootCausesTab = issueDetailsPage.selectRootCauseTab();
        sdWizardPage = rootCausesTab.openAddRootCauseWizard();
        sdWizardPage.getMoStep().showAllMOs();
        sdWizardPage.getMoStep().enterTextIntoSearchComponent(SecondMOIdentifier);
        sdWizardPage.getMoStep().selectObjectInMOTable(SecondMOIdentifier);
        sdWizardPage.clickAcceptButtonInWizard();

        Assert.assertTrue(rootCausesTab.checkIfMOIdentifierIsPresentOnRootCauses(SecondMOIdentifier));
    }

    @Test(priority = 25, testName = "Check Participants", description = "Check Participants Tab - add Participant")
    @Description("Check Participants Tab - add Participant")
    public void addParticipant() {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        participantsTab = issueDetailsPage.selectParticipantsTab();
        participantsTab.createParticipantAndLinkToIssue(PARTICIPANT_FIRST_NAME, PARTICIPANT_SURNAME, PARTICIPANT_ROLE);
        int newParticipantRow = participantsTab.participantRow(PARTICIPANT_FIRST_NAME);

        Assert.assertEquals(participantsTab.checkParticipantFirstName(newParticipantRow), PARTICIPANT_FIRST_NAME);
        Assert.assertEquals(participantsTab.checkParticipantSurname(newParticipantRow), PARTICIPANT_SURNAME);
        Assert.assertEquals(participantsTab.checkParticipantRole(newParticipantRow), PARTICIPANT_ROLE.toUpperCase());
    }

    @Test(priority = 26, testName = "Edit participant", description = "Edit participant")
    @Description("Edit participant")
    public void editParticipant() {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        participantsTab = issueDetailsPage.selectParticipantsTab();
        int newParticipantRow = participantsTab.participantRow(PARTICIPANT_FIRST_NAME);
        participantsTab.selectParticipant(newParticipantRow);
        participantsTab.editParticipant(PARTICIPANT_FIRST_NAME_EDITED, PARTICIPANT_SURNAME, PARTICIPANT_ROLE);
        int editedParticipantRow = participantsTab.participantRow(PARTICIPANT_FIRST_NAME_EDITED);

        Assert.assertEquals(participantsTab.checkParticipantFirstName(editedParticipantRow), PARTICIPANT_FIRST_NAME_EDITED);
        Assert.assertEquals(participantsTab.checkParticipantSurname(editedParticipantRow), PARTICIPANT_SURNAME);
        Assert.assertEquals(participantsTab.checkParticipantRole(editedParticipantRow), PARTICIPANT_ROLE.toUpperCase());
    }

    @Test(priority = 27, testName = "Unlink Participant", description = "Unlink Edited Participant")
    @Description("Unlink Edited Participant")
    public void unlinkParticipant() {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        participantsTab = issueDetailsPage.selectParticipantsTab();
        int participantsInTable = participantsTab.countParticipantsInTable();
        participantsTab.selectParticipant(participantsTab.participantRow(PARTICIPANT_FIRST_NAME_EDITED));
        participantsTab.clickUnlinkParticipant();

        Assert.assertEquals(participantsTab.countParticipantsInTable(), participantsInTable - 1);
    }

    @Test(priority = 28, testName = "Remove Participant", description = "Remove Edited Participant")
    @Description("Remove Edited Participant")
    public void removeParticipant() {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        participantsTab = issueDetailsPage.selectParticipantsTab();
        participantsTab.addExistingParticipant(PARTICIPANT_FIRST_NAME_EDITED, PARTICIPANT_ROLE);
        int participantsInTable = participantsTab.countParticipantsInTable();
        participantsTab.selectParticipant(participantsTab.participantRow(PARTICIPANT_FIRST_NAME_EDITED));
        participantsTab.clickRemoveParticipant();
        participantsTab.clickConfirmRemove();

        Assert.assertEquals(participantsTab.countParticipantsInTable(), participantsInTable - 1);
    }

    @Test(priority = 29, testName = "Check Related Problems tab", description = "Check Related Problems tab - Create Problem")
    @Description("Check Related Problems tab - Create Problem")
    public void createProblemRelatedToTicket() {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        relatedProblemsTab = issueDetailsPage.selectRelatedProblemsTab();
        sdWizardPage = relatedProblemsTab.openCreateProblemWizard();
        sdWizardPage.clickNextButtonInWizard();
        sdWizardPage.insertValueToTextAreaComponent(CREATE_PROBLEM_NAME, CREATE_PROBLEM_WIZARD_NAME_ID);
        sdWizardPage.insertValueToSearchComponent(CREATE_PROBLEM_ASSIGNEE, CREATE_PROBLEM_WIZARD_ASSIGNEE_ID);
        sdWizardPage.clickNextButtonInWizard();
        sdWizardPage.clickAcceptButtonInWizard();

        Assert.assertTrue(relatedProblemsTab.checkRelatedProblemName(0).contains(CREATE_PROBLEM_NAME));
        Assert.assertEquals(relatedProblemsTab.checkRelatedProblemAssignee(0), CREATE_PROBLEM_ASSIGNEE);
    }

    @Test(priority = 30, testName = "Export from Related Problems tab", description = "Export from Related Problems tab")
    @Description("Export from Related Problems tab")
    public void exportFromRelatedProblemsTab() {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        relatedProblemsTab = issueDetailsPage.selectRelatedProblemsTab();
        relatedProblemsTab.exportFromTabTable();
        int amountOfNotifications = relatedProblemsTab.openNotificationPanel().amountOfNotifications();

        Assert.assertEquals(amountOfNotifications, 0);
        Assert.assertTrue(relatedProblemsTab.isRelatedIssuesFileNotEmpty());
    }

    @Test(priority = 31, testName = "Add attachment to ticket", description = "Add attachment to ticket")
    @Description("Add attachment to ticket")
    public void addAttachment() {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        issueDetailsPage.maximizeWindow(DETAILS_TABS_CONTAINER_ID);
        attachmentsTab = issueDetailsPage.selectAttachmentsTab(DETAILS_TABS_CONTAINER_ID);

        Assert.assertTrue(attachmentsTab.isAttachmentListEmpty());
        attachmentsTab.addAttachment(FILE_TO_UPLOAD_PATH);

        Assert.assertFalse(attachmentsTab.isAttachmentListEmpty());
        Assert.assertEquals(attachmentsTab.getAttachmentOwner(), USER_NAME);
    }

    @Test(priority = 32, testName = "Download Attachment", description = "Download the Attachment from Attachment tab in Ticket Details")
    @Description("Download the Attachment from Attachment tab in Ticket Details")
    public void downloadAttachment() {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        issueDetailsPage.maximizeWindow(DETAILS_TABS_CONTAINER_ID);
        attachmentsTab = issueDetailsPage.selectAttachmentsTab(DETAILS_TABS_CONTAINER_ID);

        Assert.assertFalse(attachmentsTab.isAttachmentListEmpty());
        attachmentsTab.clickDownloadAttachment();
        issueDetailsPage.attachFileToReport(CSV_FILE);

        Assert.assertTrue(issueDetailsPage.checkIfFileIsNotEmpty(CSV_FILE));
    }

    @Test(priority = 33, testName = "Delete Attachment", description = "Delete the Attachment from Attachment tab in Ticket Details")
    @Description("Delete the Attachment from Attachment tab in Ticket Details")
    public void deleteAttachment() {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        issueDetailsPage.maximizeWindow(DETAILS_TABS_CONTAINER_ID);
        attachmentsTab = issueDetailsPage.selectAttachmentsTab(DETAILS_TABS_CONTAINER_ID);

        Assert.assertFalse(attachmentsTab.isAttachmentListEmpty());
        attachmentsTab.clickDeleteAttachment();

        Assert.assertTrue(attachmentsTab.isAttachmentListEmpty());
    }

    @Test(priority = 34, testName = "Most Important Info test", description = "add comment as important and check if it is displayed in most important info tab")
    @Description("add comment as important and check if it is displayed in most important info tab")
    public void mostImportantInfoTest() {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        messagesTab = issueDetailsPage.selectMessagesTab();
        messagesTab.addInternalComment(NOTIFICATION_MESSAGE_COMMENT_IMPORTANT);
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

    @Parameters({"serviceMOIdentifier"})
    @Test(priority = 35, testName = "Add Affected", description = "Add Affected Service to the Ticket")
    @Description("Add Affected Service to the Ticket")
    public void addAffected(
            @Optional("TEST_MO_ABS_SRV") String serviceMOIdentifier
    ) {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        affectedTab = issueDetailsPage.selectAffectedTab();
        int initialServiceCount = affectedTab.countServicesInTable();
        affectedTab.addServiceToTable(serviceMOIdentifier);

        Assert.assertEquals(affectedTab.countServicesInTable(), initialServiceCount + 1);
    }
}

