package com.oss.servicedesk;

import java.time.LocalDateTime;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.servicedesk.GraphQLSearchPage;
import com.oss.pages.servicedesk.ticket.BaseDashboardPage;
import com.oss.pages.servicedesk.ticket.IssueDetailsPage;
import com.oss.pages.servicedesk.ticket.MoreDetailsPage;
import com.oss.pages.servicedesk.ticket.RemainderForm;
import com.oss.pages.servicedesk.ticket.TicketSearchPage;
import com.oss.pages.servicedesk.ticket.tabs.AttachmentsTab;
import com.oss.pages.servicedesk.ticket.tabs.ExternalTab;
import com.oss.pages.servicedesk.ticket.tabs.MessagesTab;
import com.oss.pages.servicedesk.ticket.tabs.ParticipantsTab;
import com.oss.pages.servicedesk.ticket.tabs.RelatedProblemsTab;
import com.oss.pages.servicedesk.ticket.tabs.RelatedTicketsTab;
import com.oss.pages.servicedesk.ticket.tabs.RootCausesTab;
import com.oss.pages.servicedesk.ticket.wizard.AttachmentWizardPage;
import com.oss.pages.servicedesk.ticket.wizard.ExternalPromptPage;
import com.oss.pages.servicedesk.ticket.wizard.ParticipantsPromptPage;
import com.oss.pages.servicedesk.ticket.wizard.SDWizardPage;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

import static com.oss.pages.servicedesk.BaseSDPage.CREATE_DATE_FILTER_DATE_FORMATTER;
import static com.oss.pages.servicedesk.GraphQLIssueTableConstants.ISSUE_OUT_ASSIGNEE_ATTR;
import static com.oss.pages.servicedesk.GraphQLIssueTableConstants.ISSUE_OUT_STATUS_ATTR;

@Listeners({TestListener.class})
public class CreateTroubleTicketVFNZTest extends BaseTestCase {

    private BaseDashboardPage baseDashboardPage;
    private TicketSearchPage ticketSearchPage;
    private SDWizardPage sdWizardPage;
    private IssueDetailsPage issueDetailsPage;
    private MessagesTab messagesTab;
    private MoreDetailsPage moreDetailsPage;
    private RemainderForm remainderForm;
    private AttachmentWizardPage attachmentWizardPage;
    private MessagesTab mostImportantInfoTab;
    private AttachmentsTab attachmentsTab;
    private ExternalTab externalTab;
    private ExternalPromptPage externalPromptPage;
    private RootCausesTab rootCausesTab;
    private RelatedTicketsTab relatedTicketsTab;
    private ParticipantsTab participantsTab;
    private ParticipantsPromptPage participantsPromptPage;
    private RelatedProblemsTab relatedProblemsTab;
    private String ticketID;

    private static final String TROUBLE_TICKET_ISSUE_TYPE = "trouble-ticket";
    private static final String TICKET_DASHBOARD = "_TroubleTickets";
    private static final String TT_DESCRIPTION = "TestSelenium";
    private static final String TT_DESCRIPTION_EDITED = "TestSelenium_edited";
    private static final String TT_CORRELATION_ID = "12345";
    private static final String TT_REFERENCE_ID = "12345";
    private static final String TT_SEVERITY = "Warning";
    private static final String TT_EXTERNAL = "Selenium test external";
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

    private static final String DETAILS_WINDOW_ID = "_detailsWindow";
    private static final String STATUS_ACKNOWLEDGED = "Acknowledged";
    private static final String OVERVIEW_TAB_ARIA_CONTROLS = "most-wanted";
    private static final String DICTIONARIES_TAB_ARIA_CONTROLS = "_dictionariesTab";
    private static final String DESCRIPTION_TAB_ARIA_CONTROLS = "_descriptionTab";
    private static final String MOST_IMPORTANT_INFO_TAB_ARIA_CONTROLS = "_mostImportantTab";
    private static final String SAME_MO_TT_TAB_ARIA_CONTROLS = "_sameMOTTTab";

    private static final String EXTERNAL_LIST_ID = "_detailsExternalsListApp";
    private static final String ADD_TO_LIBRARY_LABEL = "Add to Library";
    private static final String WIZARD_LIBRARY_TYPE_ID = "{\"identifier\":\"type\",\"parentIdentifier\":\"type\",\"parentValueIdentifier\":\"\",\"groupId\":\"type\"}";
    private static final String WIZARD_CATEGORY_ID = "{\"identifier\":\"Category\",\"parentIdentifier\":\"type\",\"parentValueIdentifier\":\"\",\"groupId\":\"Category\"}-input";
    private static final String TABLES_WINDOW_ID = "_tablesWindow";

    private static final String NOTIFICATION_WIZARD_CHANNEL_ID = "channel-component-input";
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
    private static final String PARTICIPANT_SURNAME = LocalDateTime.now().toString();
    private static final String PARTICIPANT_ROLE = "Contact";

    private static final String CREATE_PROBLEM_WIZARD_NAME_ID = "name";
    private static final String CREATE_PROBLEM_WIZARD_ASSIGNEE_ID = "assignee";
    private static final String CREATE_PROBLEM_WIZARD_LABEL_ID = "label";
    private static final String CREATE_PROBLEM_WIZARD_CREATE_PROBLEM_ID = "_createProblemSubmitId-1";
    private static final String CREATE_PROBLEM_NAME = "Related Problem - Selenium Test";
    private static final String CREATE_PROBLEM_ASSIGNEE = "sd_seleniumtest";
    private static final String CREATE_PROBLEM_LABEL = LocalDateTime.now().toString();

    private static final String USER_NAME = "sd_seleniumtest";
    private static final String FILE_TO_UPLOAD_PATH = "DataSourceCSV/CPU_USAGE_INFO_RAW-MAP.xlsx";
    private static final String CSV_FILE = "*CPU_USAGE_INFO_RAW-MAP*.*";

    private static final String EMPTY_SEARCH_FILTER = "";

    @BeforeMethod
    public void goToTicketDashboardPage() {
        baseDashboardPage = new BaseDashboardPage(driver, webDriverWait).goToPage(driver, BASIC_URL, TICKET_DASHBOARD);
    }

    @Parameters({"MOIdentifier", "ttAssignee"})
    @Test(priority = 1, testName = "Create CTT Ticket", description = "Create CTT Ticket")
    @Description("Create CTT Ticket")
    public void createCTTTicket(
            @Optional("CFS_Access_Product_Selenium_1") String MOIdentifier,
            @Optional("ca_kodrobinska") String ttAssignee
    ) {
        sdWizardPage = baseDashboardPage.openCreateTicketWizard("CTT");
        sdWizardPage.getMoStep().enterTextIntoSearchComponent(MOIdentifier);
        sdWizardPage.getMoStep().selectObjectInMOTable(MOIdentifier);
        sdWizardPage.clickNextButtonInWizard();
        sdWizardPage.insertValueToSearchComponent(ttAssignee, TT_WIZARD_ASSIGNEE);
        sdWizardPage.insertValueToTextComponent(TT_REFERENCE_ID, TT_WIZARD_REFERENCE_ID);
        sdWizardPage.enterIncidentDescription(TT_DESCRIPTION);
        sdWizardPage.enterExpectedResolutionDate();
        sdWizardPage.insertValueToTextComponent(TT_CORRELATION_ID, TT_WIZARD_CORRELATION_ID);
        sdWizardPage.clickNextButtonInWizard();
        String date = LocalDateTime.now().minusMinutes(5).format(CREATE_DATE_FILTER_DATE_FORMATTER);
        sdWizardPage.insertValueToTextComponent(date, TT_WIZARD_ISSUE_START_DATE_ID);
        sdWizardPage.insertValueToTextComponent(date, TT_WIZARD_MESSAGE_DATE_ID);
        sdWizardPage.clickAcceptButtonInWizard();
        ticketID = baseDashboardPage.getTicketIdWithAssignee(ttAssignee);
        Assert.assertEquals(baseDashboardPage.getAssigneeForNthTicketInTTTable(0), ttAssignee);
    }

    @Parameters({"NewAssignee", "EscalatedTo"})
    @Test(priority = 2, testName = "Edit Ticket Details", description = "Edit Ticket Details")
    @Description("Edit Ticket Details")
    public void editTicketDetails(
            @Optional("Tier2_Mobile") String NewAssignee,
            @Optional("admin oss") String EscalatedTo
    ) {
        issueDetailsPage = baseDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        issueDetailsPage.maximizeWindow(DETAILS_WINDOW_ID);
        ticketID = issueDetailsPage.getOpenedIssueId();
        issueDetailsPage.allowEditingTicket();
        sdWizardPage = issueDetailsPage.openEditTicketWizard();
        sdWizardPage.clickNextButtonInWizard();
        sdWizardPage.insertValueToSearchComponent(NewAssignee, TT_WIZARD_ASSIGNEE);
        sdWizardPage.enterIncidentDescription(TT_DESCRIPTION_EDITED);
        sdWizardPage.insertValueToSearchComponent(EscalatedTo, TT_WIZARD_ESCALATED_TO);
        sdWizardPage.insertValueToComboBoxComponent(TT_SEVERITY, TT_WIZARD_SEVERITY);
        sdWizardPage.clickNextButtonInWizard();
        sdWizardPage.clickAcceptButtonInWizard();
    }

    @Test(priority = 3, testName = "Skip checklist actions and change status", description = "Skip checklist actions and change status")
    @Description("Skip checklist actions and change status")
    public void checkOverview() {
        issueDetailsPage = baseDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        issueDetailsPage.skipAllActionsOnCheckList();
        Assert.assertTrue(issueDetailsPage.isAllActionsSkipped());
        issueDetailsPage.changeTicketStatus(STATUS_ACKNOWLEDGED);
        Assert.assertEquals(issueDetailsPage.checkTicketStatus(), STATUS_ACKNOWLEDGED);
    }

    @Parameters({"NewAssignee"})
    @Test(priority = 4, testName = "Open Ticket Details from Tickets Search view", description = "Open Ticket Details from Tickets Search view")
    @Description("Open Ticket Details from Tickets Search view")
    public void openTicketDetailsFromTicketsSearchView(
            @Optional("Tier2_Mobile") String NewAssignee
    ) {
        ticketSearchPage = new TicketSearchPage(driver, webDriverWait);
        ticketSearchPage.goToTicketSearchPage(driver, BASIC_URL, EMPTY_SEARCH_FILTER);
        ticketSearchPage.filterByTextField(TICKETS_SEARCH_ASSIGNEE_ATTRIBUTE, NewAssignee);
        String startDate = LocalDateTime.now().minusMinutes(10).format(CREATE_DATE_FILTER_DATE_FORMATTER);
        String endDate = LocalDateTime.now().format(CREATE_DATE_FILTER_DATE_FORMATTER);
        String date = startDate + " - " + endDate;
        ticketSearchPage.filterByTextField(GraphQLSearchPage.CREATION_TIME_ATTRIBUTE, date);
        ticketSearchPage.filterByTextField(GraphQLSearchPage.DESCRIPTION_ATTRIBUTE, TT_DESCRIPTION_EDITED);
        ticketSearchPage.filterByComboBox(TICKETS_SEARCH_STATUS_ATTRIBUTE, STATUS_ACKNOWLEDGED);
        issueDetailsPage = ticketSearchPage.openTicketDetailsView("0", BASIC_URL);
        Assert.assertEquals(issueDetailsPage.getOpenedIssueId(), ticketID);
    }

    @Test(priority = 5, testName = "Add External to ticket", description = "Add External to ticket")
    @Description("Add External to ticket")
    public void addExternalToTicket() {
        issueDetailsPage = baseDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        externalTab = issueDetailsPage.selectExternalTab();
        externalPromptPage = externalTab.clickAddExternal();
        externalPromptPage.fillExternalName(TT_EXTERNAL);
        externalPromptPage.clickCreateExternal();
        Assert.assertTrue(externalTab.isExternalCreated(TT_EXTERNAL, EXTERNAL_LIST_ID));
    }

    @Test(priority = 6, testName = "Edit External in Problem", description = "Edit External in Ticket")
    @Description("Edit External in Ticket")
    public void editExternalInTicket() {
        issueDetailsPage = baseDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        externalTab = issueDetailsPage.selectExternalTab();
        Assert.assertTrue(externalTab.isExternalCreated(TT_EXTERNAL, EXTERNAL_LIST_ID));

        externalPromptPage = externalTab.clickEditExternal(EXTERNAL_LIST_ID);
        externalPromptPage.fillExternalName(TT_EXTERNAL_EDITED);
        externalPromptPage.clickSave();
        Assert.assertTrue(externalTab.isExternalCreated(TT_EXTERNAL_EDITED, EXTERNAL_LIST_ID));
    }

    @Test(priority = 7, testName = "Delete External", description = "Delete External in Ticket")
    @Description("Delete External in Ticket")
    public void deleteExternalInTicket() {
        issueDetailsPage = baseDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        externalTab = issueDetailsPage.selectExternalTab();
        Assert.assertTrue(externalTab.isExternalCreated(TT_EXTERNAL_EDITED, EXTERNAL_LIST_ID));

        externalTab.clickDeleteExternal(EXTERNAL_LIST_ID);
        Assert.assertTrue(externalTab.isExternalListEmpty(EXTERNAL_LIST_ID));
    }

    @Test(priority = 8, testName = "Add dictionary to ticket", description = "Add dictionary to ticket")
    @Description("Add dictionary to ticket")
    public void addDictionaryToTicket() {
        issueDetailsPage = baseDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        issueDetailsPage.selectTab(DICTIONARIES_TAB_ARIA_CONTROLS);
        issueDetailsPage.clickContextAction(ADD_TO_LIBRARY_LABEL);
        SDWizardPage dictionarySDWizardPage = new SDWizardPage(driver, webDriverWait);
        dictionarySDWizardPage.insertValueToComboBoxComponent(TT_LIBRARY_TYPE, WIZARD_LIBRARY_TYPE_ID);
        dictionarySDWizardPage.insertValueToComboBoxComponent(TT_CATEGORY_NAME, WIZARD_CATEGORY_ID);
        dictionarySDWizardPage.clickAcceptButtonInWizard();
        Assert.assertEquals(issueDetailsPage.checkExistingDictionary(), TT_CATEGORY_NAME);
    }

    @Test(priority = 9, testName = "Check Description Tab", description = "Check Description Tab")
    @Description("Check Description Tab")
    public void checkDescriptionTab() {
        issueDetailsPage = baseDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        issueDetailsPage.selectTab(DESCRIPTION_TAB_ARIA_CONTROLS);
        Assert.assertTrue(issueDetailsPage.checkDisplayedText(TT_DESCRIPTION_EDITED, TABLES_WINDOW_ID));
    }

    @Test(priority = 10, testName = "Check Messages Tab - add Internal Notification", description = "Check Messages Tab - add Internal Notification")
    @Description("Check Messages Tab - add Internal Notification")
    public void addInternalNotification() {
        issueDetailsPage = baseDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        messagesTab = issueDetailsPage.selectMessagesTab();
        messagesTab.createNewNotificationOnMessagesTab();
        SDWizardPage notificationSDWizardPage = new SDWizardPage(driver, webDriverWait);
        notificationSDWizardPage.insertValueToComboBoxComponent(NOTIFICATION_CHANNEL_INTERNAL, NOTIFICATION_WIZARD_CHANNEL_ID);
        notificationSDWizardPage.insertValueToTextAreaComponent(NOTIFICATION_MESSAGE_INTERNAL, NOTIFICATION_WIZARD_MESSAGE_ID);
        notificationSDWizardPage.insertValueToMultiSearchComponent(NOTIFICATION_INTERNAL_TO, NOTIFICATION_WIZARD_INTERNAL_TO_ID);
        notificationSDWizardPage.clickComboBox(NOTIFICATION_WIZARD_TEMPLATE_ID);
        notificationSDWizardPage.insertValueToComboBoxComponent(NOTIFICATION_TYPE, NOTIFICATION_WIZARD_TYPE_ID);
        notificationSDWizardPage.clickAcceptButtonInWizard();

        Assert.assertFalse(messagesTab.isMessagesTabEmpty());
        Assert.assertEquals(messagesTab.getMessageText(0), NOTIFICATION_MESSAGE_INTERNAL);
        Assert.assertEquals(messagesTab.checkMessageType(0), "NOTIFICATION");
        Assert.assertEquals(messagesTab.getBadgeTextFromMessage(0, 0), "New");
    }

    @Parameters({"NotificationEmailTo", "NotificationEmailFrom"})
    @Test(priority = 11, testName = "Check Messages Tab - add Email Notification", description = "Check Messages Tab - add Email Notification")
    @Description("Check Messages Tab - add Email Notification")
    public void addEmailNotification(
            @Optional("kornelia.odrobinska@comarch.com") String NotificationEmailTo,
            @Optional("Test@AIF.pl") String NotificationEmailFrom
    ) {
        issueDetailsPage = baseDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        messagesTab = issueDetailsPage.selectMessagesTab();
        messagesTab.createNewNotificationOnMessagesTab();
        SDWizardPage notificationSDWizardPage = new SDWizardPage(driver, webDriverWait);
        notificationSDWizardPage.insertValueToComboBoxComponent(NOTIFICATION_CHANNEL_EMAIL, NOTIFICATION_WIZARD_CHANNEL_ID);
        notificationSDWizardPage.insertValueToMultiSearchComponent(NotificationEmailTo, NOTIFICATION_WIZARD_TO_ID);
        notificationSDWizardPage.insertValueToComboBoxComponent(NotificationEmailFrom, NOTIFICATION_WIZARD_FROM_ID);
        notificationSDWizardPage.insertValueToTextComponent(NOTIFICATION_SUBJECT, NOTIFICATION_WIZARD_SUBJECT_ID);
        notificationSDWizardPage.enterEmailMessage(NOTIFICATION_MESSAGE_EMAIL);
        notificationSDWizardPage.clickAcceptButtonInWizard();

        Assert.assertFalse(messagesTab.isMessagesTabEmpty());
        Assert.assertEquals(messagesTab.getMessageText(0), NOTIFICATION_MESSAGE_EMAIL);
        Assert.assertEquals(messagesTab.checkMessageType(0), "NOTIFICATION");
    }

    @Test(priority = 12, testName = "Check Messages Tab - add Internal Comment", description = "Check Messages Tab - add Internal Comment")
    @Description("Check Messages Tab - add Internal Comment")
    public void addInternalComment() {
        issueDetailsPage = baseDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
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

    @Test(priority = 13, testName = "Add Remainder", description = "Add Remainder")
    @Description("Add Remainder")
    public void addRemainderTest() {
        issueDetailsPage = baseDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        issueDetailsPage.selectTab(OVERVIEW_TAB_ARIA_CONTROLS);
        issueDetailsPage.maximizeWindow(DETAILS_WINDOW_ID);
        remainderForm = issueDetailsPage.clickAddRemainder();
        remainderForm.createReminderWithNote(REMAINDER_NOTE);
        moreDetailsPage = issueDetailsPage.clickMoreDetails();
        Assert.assertTrue(moreDetailsPage.isReminderNoteInLogsTable(REMAINDER_NOTE));
        goToTicketDashboardPage();
        Assert.assertTrue(baseDashboardPage.isReminderPresent(baseDashboardPage.getRowForTicketWithID(ticketID), REMAINDER_NOTE));
    }

    @Test(priority = 14, testName = "Edit Reminder", description = "Edit Reminder")
    @Description("Edit Reminder")
    public void editRemainderTest() {
        issueDetailsPage = baseDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        issueDetailsPage.selectTab(OVERVIEW_TAB_ARIA_CONTROLS);
        issueDetailsPage.maximizeWindow(DETAILS_WINDOW_ID);
        remainderForm = issueDetailsPage.clickEditRemainder();
        remainderForm.createReminderWithNote(EDITED_REMAINDER_NOTE);
        goToTicketDashboardPage();
        Assert.assertTrue(baseDashboardPage.isReminderPresent(baseDashboardPage.getRowForTicketWithID(ticketID), EDITED_REMAINDER_NOTE));
    }

    @Test(priority = 15, testName = "Delete Remainder", description = "Delete Remainder")
    @Description("Delete Remainder")
    public void deleteRemainderTest() {
        issueDetailsPage = baseDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        issueDetailsPage.selectTab(OVERVIEW_TAB_ARIA_CONTROLS);
        issueDetailsPage.maximizeWindow(DETAILS_WINDOW_ID);
        issueDetailsPage.clickRemoveRemainder();
        goToTicketDashboardPage();
        Assert.assertFalse(baseDashboardPage.isReminderPresent(baseDashboardPage.getRowForTicketWithID(ticketID), EDITED_REMAINDER_NOTE));
    }

    @Parameters({"RelatedTicketID"})
    @Test(priority = 16, testName = "Check Related Tickets Tab - link Ticket", description = "Check Related Tickets Tab - link Ticket")
    @Description("Check Related Tickets Tab - link Ticket")
    public void linkTicketToTicket(@Optional("100") String RelatedTicketID) {
        issueDetailsPage = baseDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        relatedTicketsTab = issueDetailsPage.selectRelatedTicketsTab();
        sdWizardPage = relatedTicketsTab.openLinkTicketWizard();
        sdWizardPage.insertValueToMultiSearchComponent(RelatedTicketID, "issueIdsToLink");
        sdWizardPage.clickLinkButton();

        Assert.assertEquals(relatedTicketsTab.checkRelatedTicketsId(0), RelatedTicketID);
    }

    @Test(priority = 17, testName = "Check Related Tickets Tab - unlink Ticket", description = "Check Related Tickets Tab - unlink Ticket")
    @Description("Check Related Tickets Tab - unlink Ticket")
    public void unlinkTicketFromTicket() {
        issueDetailsPage = baseDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        relatedTicketsTab = issueDetailsPage.selectRelatedTicketsTab();
        relatedTicketsTab.selectTicket(0);
        relatedTicketsTab.unlinkTicket();
        relatedTicketsTab.confirmUnlinking();

        Assert.assertTrue(relatedTicketsTab.isRelatedTicketsTableEmpty());
    }

    @Parameters({"RelatedTicketID"})
    @Test(priority = 18, testName = "Check Related Tickets Tab - show archived switcher", description = "Check Related Tickets Tab - show archived switcher")
    @Description("Check Related Tickets Tab - show archived switcher")
    public void showArchived(@Optional("100") String RelatedTicketID) {
        issueDetailsPage = baseDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        relatedTicketsTab = issueDetailsPage.selectRelatedTicketsTab();
        relatedTicketsTab.turnOnShowArchived();

        Assert.assertEquals(relatedTicketsTab.checkRelatedTicketsId(0), RelatedTicketID);
    }

    @Test(priority = 19, testName = "Check Same MO TT Tab", description = "Check Same MO TT Tab")
    @Description("Check Same MO TT Tab")
    public void checkSameMOTTTab() {
        issueDetailsPage = baseDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        issueDetailsPage.selectTab(SAME_MO_TT_TAB_ARIA_CONTROLS);
        Assert.assertTrue(issueDetailsPage.checkIfSameMOTTTableIsNotEmpty());
    }

    @Test(priority = 20, testName = "Check Root Causes Tree Table", description = "Check Root Causes Tree Table")
    @Description("Check Root Causes Tree Table")
    public void checkRootCause() {
        issueDetailsPage = baseDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        rootCausesTab = issueDetailsPage.selectRootCauseTab();
        Assert.assertFalse(rootCausesTab.isRootCauseTableEmpty());

        rootCausesTab.selectMOInRootCausesTab(0);
        Assert.assertTrue(rootCausesTab.checkIfRootCausesTreeTableIsNotEmpty());
    }

    @Parameters({"SecondMOIdentifier"})
    @Test(priority = 21, testName = "Add second MO in Root Causes tab", description = "Add second MO in Root Causes tab")
    @Description("Add second MO in Root Causes tab")
    public void addRootCause(@Optional("CFS_Access_Product_Selenium_2") String SecondMOIdentifier) {
        issueDetailsPage = baseDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        rootCausesTab = issueDetailsPage.selectRootCauseTab();
        sdWizardPage = rootCausesTab.openAddRootCauseWizard();
        sdWizardPage.getMoStep().showAllMOs();
        sdWizardPage.getMoStep().enterTextIntoSearchComponent(SecondMOIdentifier);
        sdWizardPage.getMoStep().selectObjectInMOTable(SecondMOIdentifier);
        sdWizardPage.clickAcceptButtonInWizard();

        Assert.assertTrue(rootCausesTab.checkIfMOIdentifierIsPresentOnRootCauses(SecondMOIdentifier));
    }

    @Test(priority = 22, testName = "Check Participants", description = "Check Participants Tab - add Participant")
    @Description("Check Participants Tab - add Participant")
    public void addParticipant() {
        issueDetailsPage = baseDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
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

    @Test(priority = 23, testName = "Check Related Problems tab", description = "Check Related Problems tab - Create Problem")
    @Description("Check Related Problems tab - Create Problem")
    public void createProblemRelatedToTicket() {
        issueDetailsPage = baseDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        relatedProblemsTab = issueDetailsPage.selectRelatedProblemsTab();
        sdWizardPage = relatedProblemsTab.openCreateProblemWizard();
        sdWizardPage.insertValueToTextComponent(CREATE_PROBLEM_NAME, CREATE_PROBLEM_WIZARD_NAME_ID);
        sdWizardPage.insertValueToSearchComponent(CREATE_PROBLEM_ASSIGNEE, CREATE_PROBLEM_WIZARD_ASSIGNEE_ID);
        sdWizardPage.insertValueToTextComponent(CREATE_PROBLEM_LABEL, CREATE_PROBLEM_WIZARD_LABEL_ID);
        sdWizardPage.clickButton(CREATE_PROBLEM_WIZARD_CREATE_PROBLEM_ID);

        Assert.assertEquals(relatedProblemsTab.checkRelatedProblemName(0), CREATE_PROBLEM_NAME);
        Assert.assertEquals(relatedProblemsTab.checkRelatedProblemAssignee(0), CREATE_PROBLEM_ASSIGNEE);
        Assert.assertEquals(relatedProblemsTab.checkRelatedProblemLabel(0), CREATE_PROBLEM_LABEL);
    }

    @Test(priority = 24, testName = "Add attachment to ticket", description = "Add attachment to ticket")
    @Description("Add attachment to ticket")
    public void addAttachment() {
        issueDetailsPage = baseDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        issueDetailsPage.maximizeWindow(DETAILS_WINDOW_ID);
        attachmentsTab = issueDetailsPage.selectAttachmentsTab();

        Assert.assertTrue(attachmentsTab.isAttachmentListEmpty());
        attachmentWizardPage = attachmentsTab.clickAttachFile();
        attachmentWizardPage.uploadAttachmentFile(FILE_TO_UPLOAD_PATH);
        issueDetailsPage = attachmentWizardPage.clickAccept();

        Assert.assertFalse(attachmentsTab.isAttachmentListEmpty());
        Assert.assertEquals(attachmentsTab.getAttachmentOwner(), USER_NAME);
    }

    @Test(priority = 25, testName = "Download Attachment", description = "Download the Attachment from Attachment tab in Ticket Details")
    @Description("Download the Attachment from Attachment tab in Ticket Details")
    public void downloadAttachment() {
        issueDetailsPage = baseDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        issueDetailsPage.maximizeWindow(DETAILS_WINDOW_ID);
        attachmentsTab = issueDetailsPage.selectAttachmentsTab();

        Assert.assertFalse(attachmentsTab.isAttachmentListEmpty());
        attachmentsTab.clickDownloadAttachment();
        issueDetailsPage.attachFileToReport(CSV_FILE);

        Assert.assertTrue(issueDetailsPage.checkIfFileIsNotEmpty(CSV_FILE));
    }

    @Test(priority = 26, testName = "Delete Attachment", description = "Delete the Attachment from Attachment tab in Ticket Details")
    @Description("Delete the Attachment from Attachment tab in Ticket Details")
    public void deleteAttachment() {
        issueDetailsPage = baseDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        issueDetailsPage.maximizeWindow(DETAILS_WINDOW_ID);
        attachmentsTab = issueDetailsPage.selectAttachmentsTab();

        Assert.assertFalse(attachmentsTab.isAttachmentListEmpty());
        attachmentsTab.clickDeleteAttachment();

        Assert.assertTrue(attachmentsTab.isAttachmentListEmpty());
    }

    @Test(priority = 27, testName = "Most Important Info test", description = "add comment as important and check if it is displayed in most important info tab")
    @Description("add comment as important and check if it is displayed in most important info tab")
    public void mostImportantInfoTest() {
        issueDetailsPage = baseDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        messagesTab = issueDetailsPage.selectMessagesTab();
        messagesTab.clickCreateNewCommentButton();
        messagesTab.enterCommentMessage(NOTIFICATION_MESSAGE_COMMENT_IMPORTANT);
        messagesTab.clickCreateCommentButton();
        Assert.assertFalse(messagesTab.isMessagesTabEmpty());
        Assert.assertEquals(messagesTab.getMessageText(0), NOTIFICATION_MESSAGE_COMMENT_IMPORTANT);

        messagesTab.markAsImportant(0);
        Assert.assertEquals(messagesTab.getBadgeTextFromMessage(0, 1), "IMPORTANT");
        issueDetailsPage.selectTab(MOST_IMPORTANT_INFO_TAB_ARIA_CONTROLS);
        mostImportantInfoTab = new MessagesTab(driver, webDriverWait);
        Assert.assertFalse(mostImportantInfoTab.isMessagesTabEmpty());
        Assert.assertEquals(mostImportantInfoTab.getMessageText(0), NOTIFICATION_MESSAGE_COMMENT_IMPORTANT);
        Assert.assertEquals(mostImportantInfoTab.getBadgeTextFromMessage(0, 1), "IMPORTANT");
    }
}

