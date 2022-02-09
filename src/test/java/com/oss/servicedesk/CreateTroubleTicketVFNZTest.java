package com.oss.servicedesk;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.servicedesk.ticket.MoreDetailsPage;
import com.oss.pages.servicedesk.ticket.RemainderForm;
import com.oss.pages.servicedesk.ticket.TicketDashboardPage;
import com.oss.pages.servicedesk.ticket.TicketDetailsPage;
import com.oss.pages.servicedesk.ticket.TicketSearchPage;
import com.oss.pages.servicedesk.ticket.tabs.MessagesTab;
import com.oss.pages.servicedesk.ticket.wizard.SDWizardPage;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

@Listeners({TestListener.class})
public class CreateTroubleTicketVFNZTest extends BaseTestCase {

    private TicketDashboardPage ticketDashboardPage;
    private TicketSearchPage ticketSearchPage;
    private SDWizardPage SDWizardPage;
    private TicketDetailsPage ticketDetailsPage;
    private MessagesTab messagesTab;
    private MoreDetailsPage moreDetailsPage;
    private RemainderForm remainderForm;
    private String ticketID;

    private final static String TT_DESCRIPTION = "TestSelenium";
    private final static String TT_DESCRIPTION_EDITED = "TestSelenium_edited";
    private final static String TT_CORRELATION_ID = "12345";
    private final static String TT_REFERENCE_ID = "12345";
    private final static String TT_SEVERITY = "Warning";
    private final static String TT_EXTERNAL = "Selenium test external";
    private final static String TT_LIBRARY_TYPE = "Category";
    private final static String TT_CATEGORY_NAME = "Data Problem";

    private final static String NOTIFICATION_CHANNEL_INTENRAL = "Internal";
    private final static String NOTIFICATION_CHANNEL_EMAIL = "E-mail";
    private final static String NOTIFICATION_MESSAGE_INTERNAL = "test selenium message internal";
    private final static String NOTIFICATION_MESSAGE_EMAIL = "test selenium message E-mail";
    private final static String NOTIFICATION_MESSAGE_COMMENT = "test selenium message comment";
    private final static String NOTIFICATION_INTERNAL_TO = "ca_kodrobinska";
    private final static String NOTIFICATION_TYPE = "Success";
    private final static String NOTIFICATION_SUBJECT = "Email notification test";

    private final static String TT_WIZARD_ASSIGNEE = "TT_WIZARD_INPUT_ASSIGNEE_LABEL";
    private final static String TT_WIZARD_ESCALATED_TO = "TT_WIZARD_INPUT_ESCALATED_TO_LABEL";
    private final static String TT_WIZARD_SEVERITY = "TT_WIZARD_INPUT_SEVERITY_LABEL";
    private final static String CREATE_DATE_FILTER_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private final static String TT_WIZARD_CORRELATION_ID = "ISSUE_CORRELATION_ID";
    private final static String TT_WIZARD_REFERENCE_ID = "TT_WIZARD_INPUT_REFERENCE_ID_LABEL";
    private final static String TT_WIZARD_ISSUE_START_DATE_ID = "IssueStartDate";
    private final static String TT_WIZARD_MESSAGE_DATE_ID = "PleaseProvideTheTimeOnTheHandsetTheTxtMessageArrived";

    private final static String DETAILS_WINDOW_ID = "_detailsWindow";
    private final static String STATUS_ACKNOWLEDGED = "Acknowledged";
    private final static String WIZARD_EXTERNAL_NAME = "_name";
    private final static String OVERVIEW_TAB_ARIA_CONTROLS = "most-wanted";
    private final static String EXTERNAL_TAB_ARIA_CONTROLS = "_detailsExternalTab";
    private final static String DICTIONARIES_TAB_ARIA_CONTROLS = "_dictionariesTab";
    private final static String DESCRIPTION_TAB_ARIA_CONTROLS = "_descriptionTab";
    private final static String MESSAGES_TAB_ARIA_CONTROLS = "_messagesTab";
    private final static String ADD_EXTERNAL_LABEL = "Add External";
    private final static String ADD_TO_LIBRARY_LABEL = "Add to Library";
    private final static String WIZARD_LIBRARY_TYPE_ID = "{\"identifier\":\"type\",\"parentIdentifier\":\"type\",\"parentValueIdentifier\":\"\",\"groupId\":\"type\"}";
    private final static String WIZARD_CATEGORY_ID = "{\"identifier\":\"Category\",\"parentIdentifier\":\"type\",\"parentValueIdentifier\":\"\",\"groupId\":\"Category\"}-input";
    private final static String TABLES_WINDOW_ID = "_tablesWindow";

    private final static String NOTIFICATION_WIZARD_CHANNEL_ID = "channel-component-input";
    private final static String NOTIFICATION_WIZARD_MESSAGE_ID = "message-component";
    private final static String NOTIFICATION_WIZARD_INTERNAL_TO_ID = "internal-to-component";
    private final static String NOTIFICATION_WIZARD_TO_ID = "to-component";
    private final static String NOTIFICATION_WIZARD_FROM_ID = "from-component";
    private final static String NOTIFICATION_WIZARD_TYPE_ID = "internal-type-component";
    private final static String NOTIFICATION_WIZARD_TEMPLATE_ID = "template-component";
    private final static String NOTIFICATION_WIZARD_SUBJECT_ID = "subject-component";

    private final static String REMAINDER_NOTE = "Selenium Description Note";
    private final static String EDITED_REMAINDER_NOTE = "Edited Remainder Text";

    public static final DateTimeFormatter CREATE_DATE_FILTER_DATE_FORMATTER = DateTimeFormatter.ofPattern(CREATE_DATE_FILTER_DATE_PATTERN);

    @BeforeClass
    public void goToTicketDashboardPage() {
        ticketDashboardPage = TicketDashboardPage.goToPage(driver, BASIC_URL);
    }

    @Parameters({"MOIdentifier", "ttAssignee"})
    @Test(priority = 1, testName = "Create CTT Ticket", description = "Create CTT Ticket")
    @Description("Create CTT Ticket")
    public void createCTTTicket(
            @Optional("CFS_SOM_01_901") String MOIdentifier,
            @Optional("ca_kodrobinska") String ttAssignee
    ) {
        SDWizardPage = ticketDashboardPage.openCreateTicketWizard("CTT");
        SDWizardPage.getMoStep().enterTextIntoSearchComponent(MOIdentifier);
        SDWizardPage.getMoStep().selectRowInMOTable("0");
        SDWizardPage.clickNextButtonInWizard();
        SDWizardPage.insertValueToSearchComponent(ttAssignee, TT_WIZARD_ASSIGNEE);
        SDWizardPage.insertValueToTextComponent(TT_CORRELATION_ID, TT_WIZARD_CORRELATION_ID);
        SDWizardPage.insertValueToTextComponent(TT_REFERENCE_ID, TT_WIZARD_REFERENCE_ID);
        SDWizardPage.enterIncidentDescription(TT_DESCRIPTION);
        SDWizardPage.enterExpectedResolutionDate();
        SDWizardPage.clickNextButtonInWizard();
        String date = LocalDateTime.now().minusMinutes(5).format(CREATE_DATE_FILTER_DATE_FORMATTER);
        SDWizardPage.insertValueToTextComponent(date, TT_WIZARD_ISSUE_START_DATE_ID);
        SDWizardPage.insertValueToTextComponent(date, TT_WIZARD_MESSAGE_DATE_ID);
        SDWizardPage.clickAcceptButtonInWizard();
        Assert.assertEquals(ticketDashboardPage.getAssigneeForNthTicketInTTTable(0), ttAssignee);
    }

    @Parameters({"NewAssignee", "EscalatedTo"})
    @Test(priority = 2, testName = "Edit Ticket Details", description = "Edit Ticket Details")
    @Description("Edit Ticket Details")
    public void editTicketDetails(
            @Optional("Tier2_Mobile") String NewAssignee,
            @Optional("admin oss") String EscalatedTo
    ) {
        ticketDetailsPage = ticketDashboardPage.openTicketDetailsView("0", BASIC_URL);
        ticketDetailsPage.maximizeWindow(DETAILS_WINDOW_ID);
        ticketID = ticketDetailsPage.getOpenedTicketId();
        ticketDetailsPage.allowEditingTicket();
        SDWizardPage = ticketDetailsPage.openEditTicketWizard();
        SDWizardPage.clickNextButtonInWizard();
        SDWizardPage.insertValueToSearchComponent(NewAssignee, TT_WIZARD_ASSIGNEE);
        SDWizardPage.enterIncidentDescription(TT_DESCRIPTION_EDITED);
        SDWizardPage.insertValueToSearchComponent(EscalatedTo, TT_WIZARD_ESCALATED_TO);
        SDWizardPage.insertValueToMultiComboBoxComponent(TT_SEVERITY, TT_WIZARD_SEVERITY);
        SDWizardPage.clickNextButtonInWizard();
        SDWizardPage.clickAcceptButtonInWizard();
    }

    @Test(priority = 3, testName = "Skipp checklist actions and change status", description = "Skipp checklist actions and change status")
    @Description("Skipp checklist actions and change status")
    public void checkOverview() {
        ticketDetailsPage.minimizeWindow(DETAILS_WINDOW_ID);
        //do odkomentowania w razie zmiany flow
        //ticketDetailsPage.allowEditingTicket();
        ticketDetailsPage.skipAllActionsOnCheckList();
        Assert.assertTrue(ticketDetailsPage.isAllActionsSkipped());
        ticketDetailsPage.changeStatus(STATUS_ACKNOWLEDGED);
    }

    @Test(priority = 4, testName = "Open Ticket Details from Tickets Search view", description = "Open Ticket Details from Tickets Search view")
    @Description("Open Ticket Details from Tickets Search view")
    public void openTicketDetailsFromTicketsSearchView() {
        ticketSearchPage = new TicketSearchPage(driver, webDriverWait);
        ticketSearchPage.goToPage(driver, BASIC_URL);
        ticketSearchPage.clickFilterButton();
        //TODO do odkomentowania linia 146 i 153 po naprawie buga OSSSD-3243
        //ticketSearchPage.filterByTextField(TicketSearchPage.ASSIGNEE_ATTRIBUTE, TT_NEW_ASSIGNEE);
//      TODO Do odkomentowania po naprawieniu aktualizacji widoku Ticket Search (OSSSD-2605)
//        String startDate = LocalDateTime.now().minusMinutes(10).format(CREATE_DATE_FILTER_DATE_FORMATTER);
//        String endDate = LocalDateTime.now().minusMinutes(0).format(CREATE_DATE_FILTER_DATE_FORMATTER);
//        String date = startDate + " - " + endDate;
//        ticketSearchPage.clickFilterButton();
//        ticketSearchPage.filterByTextField(TicketSearchPage.CREATION_TIME_ATTRIBUTE, date );
        ticketSearchPage.filterByTextField(TicketSearchPage.DESCRIPTION_ATTRIBUTE, TT_DESCRIPTION_EDITED);
        ticketSearchPage.filterByComboBox(TicketSearchPage.STATUS_ATTRIBUTE, STATUS_ACKNOWLEDGED);
        ticketDetailsPage = ticketSearchPage.openTicketDetailsView("0", BASIC_URL);
    }

    @Test(priority = 5, testName = "Add external to ticket", description = "Add external to ticket")
    @Description("Add external to ticket")
    public void addExternalToTicket() {
        ticketDetailsPage.selectTab(EXTERNAL_TAB_ARIA_CONTROLS);
        ticketDetailsPage.clickContextAction(ADD_EXTERNAL_LABEL);
        SDWizardPage SDWizardPage = new SDWizardPage(driver, webDriverWait);
        SDWizardPage.insertValueToTextComponent(TT_EXTERNAL, WIZARD_EXTERNAL_NAME);
        SDWizardPage.clickCreateExternalButtonInWizard();
        Assert.assertTrue(ticketDetailsPage.checkExistingExternal(TT_EXTERNAL));
    }

    @Test(priority = 6, testName = "Add dictionary to ticket", description = "Add dictionary to ticket")
    @Description("Add dictionary to ticket")
    public void addDictionaryToTicket() {
        ticketDetailsPage.selectTab(DICTIONARIES_TAB_ARIA_CONTROLS);
        ticketDetailsPage.clickContextAction(ADD_TO_LIBRARY_LABEL);
        SDWizardPage dictionarySDWizardPage = new SDWizardPage(driver, webDriverWait);
        dictionarySDWizardPage.insertValueToComboBoxComponent(TT_LIBRARY_TYPE, WIZARD_LIBRARY_TYPE_ID);
        dictionarySDWizardPage.insertValueToComboBoxComponent(TT_CATEGORY_NAME, WIZARD_CATEGORY_ID);
        dictionarySDWizardPage.clickAcceptButtonInWizard();
        Assert.assertEquals(ticketDetailsPage.checkExistingDictionary(), TT_CATEGORY_NAME);
    }

    @Test(priority = 7, testName = "Check Description Tab", description = "Check Description Tab")
    @Description("Check Description Tab")
    public void checkDescriptionTab() {
        ticketDetailsPage.selectTab(DESCRIPTION_TAB_ARIA_CONTROLS);
        Assert.assertTrue(ticketDetailsPage.checkDisplayedText(TT_DESCRIPTION_EDITED, TABLES_WINDOW_ID));
    }

    @Test(priority = 8, testName = "Check Messages Tab - add Internal Notification", description = "Check Messages Tab - add Internal Notification")
    @Description("Check Messages Tab - add Internal Notification")
    public void addInternalNotification() {
        ticketDetailsPage.selectTab(MESSAGES_TAB_ARIA_CONTROLS);
        messagesTab = new MessagesTab(driver, webDriverWait);
        messagesTab.createNewNotificationOnMessagesTab();
        SDWizardPage notificationSDWizardPage = new SDWizardPage(driver, webDriverWait);
        notificationSDWizardPage.insertValueToComboBoxComponent(NOTIFICATION_CHANNEL_INTENRAL, NOTIFICATION_WIZARD_CHANNEL_ID);
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
    @Test(priority = 9, testName = "Check Messages Tab - add Email Notification", description = "Check Messages Tab - add Email Notification")
    @Description("Check Messages Tab - add Email Notification")
    public void addEmailNotification(
            @Optional("kornelia.odrobinska@comarch.com") String NotificationEmailTo,
            @Optional("Test@AIF.pl") String NotificationEmailFrom
    ) {
        ticketDetailsPage.selectTab(MESSAGES_TAB_ARIA_CONTROLS);
        messagesTab = new MessagesTab(driver, webDriverWait);
        messagesTab.createNewNotificationOnMessagesTab();
        SDWizardPage notificationSDWizardPage = new SDWizardPage(driver, webDriverWait);
        notificationSDWizardPage.insertValueToComboBoxComponent(NOTIFICATION_CHANNEL_EMAIL, NOTIFICATION_WIZARD_CHANNEL_ID);
        notificationSDWizardPage.insertValueToMultiSearchComponent(NotificationEmailTo, NOTIFICATION_WIZARD_TO_ID);
        notificationSDWizardPage.insertValueToMultiComboBoxComponent(NotificationEmailFrom, NOTIFICATION_WIZARD_FROM_ID);
        notificationSDWizardPage.insertValueToTextComponent(NOTIFICATION_SUBJECT, NOTIFICATION_WIZARD_SUBJECT_ID);
        notificationSDWizardPage.enterEmailMessage(NOTIFICATION_MESSAGE_EMAIL);
        notificationSDWizardPage.clickAcceptButtonInWizard();

        Assert.assertFalse(messagesTab.isMessagesTabEmpty());
        Assert.assertEquals(messagesTab.getMessageText(0), NOTIFICATION_MESSAGE_EMAIL);
        Assert.assertEquals(messagesTab.checkMessageType(0), "NOTIFICATION");
        Assert.assertEquals(messagesTab.getBadgeTextFromMessage(0, 0), "New");
    }

    @Test(priority = 10, testName = "Check Messages Tab - add Internal Comment", description = "Check Messages Tab - add Internal Comment")
    @Description("Check Messages Tab - add Internal Comment")
    public void addInternalComment() {
        ticketDetailsPage.selectTab(MESSAGES_TAB_ARIA_CONTROLS);
        messagesTab = new MessagesTab(driver, webDriverWait);
        messagesTab.clickCreateNewCommentButton();
        // TODO dodac wybieranie typu komentarza
        messagesTab.enterCommentMessage(NOTIFICATION_MESSAGE_COMMENT);
        messagesTab.clickCreateCommentButton();
        Assert.assertEquals(messagesTab.checkMessageType(0), "COMMENT");
        Assert.assertEquals(messagesTab.getMessageText(0), NOTIFICATION_MESSAGE_COMMENT);
        Assert.assertEquals(messagesTab.checkCommentType(0), "internal");
    }

    @Test(priority = 11, testName = "Add Remainder", description = "Add Remainder")
    @Description("Add Remainder")
    public void addRemainderTest() {
        goToTicketDashboardPage();
        ticketDashboardPage.openTicketDetailsView(String.valueOf(ticketDashboardPage.getRowForTicketWithID(ticketID)), BASIC_URL);
        ticketDetailsPage = new TicketDetailsPage(driver, webDriverWait);
        ticketDetailsPage.maximizeWindow(DETAILS_WINDOW_ID);
        ticketDetailsPage.clickAddRemainder();
        remainderForm = new RemainderForm(driver, webDriverWait);
        remainderForm.createReminderWithNote(REMAINDER_NOTE);
        ticketDetailsPage.clickMoreDetails();
        moreDetailsPage = new MoreDetailsPage(driver, webDriverWait);
        Assert.assertTrue(moreDetailsPage.isReminderNoteInLogsTable(REMAINDER_NOTE));
        goToTicketDashboardPage();
        Assert.assertTrue(ticketDashboardPage.isReminderPresent(ticketDashboardPage.getRowForTicketWithID(ticketID), REMAINDER_NOTE));
    }

    @Test(priority = 12, testName = "Edit Reminder", description = "Edit Reminder")
    @Description("Edit Reminder")
    public void editRemainderTest() {
        ticketDashboardPage.openTicketDetailsView(String.valueOf(ticketDashboardPage.getRowForTicketWithID(ticketID)), BASIC_URL);
        ticketDetailsPage = new TicketDetailsPage(driver, webDriverWait);
        ticketDetailsPage.maximizeWindow(DETAILS_WINDOW_ID);
        ticketDetailsPage.clickEditRemainder();
        remainderForm = new RemainderForm(driver, webDriverWait);
        remainderForm.createReminderWithNote(EDITED_REMAINDER_NOTE);
        goToTicketDashboardPage();
        Assert.assertTrue(ticketDashboardPage.isReminderPresent(ticketDashboardPage.getRowForTicketWithID(ticketID), EDITED_REMAINDER_NOTE));
    }

    @Test(priority = 13, testName = "Delete Remainder", description = "Delete Remainder")
    @Description("Delete Remainder")
    public void deleteRemainderTest() {
        ticketDashboardPage.openTicketDetailsView(String.valueOf(ticketDashboardPage.getRowForTicketWithID(ticketID)), BASIC_URL);
        ticketDetailsPage = new TicketDetailsPage(driver, webDriverWait);
        ticketDetailsPage.maximizeWindow(DETAILS_WINDOW_ID);
        ticketDetailsPage.clickRemoveRemainder();
        goToTicketDashboardPage();
        Assert.assertFalse(ticketDashboardPage.isReminderPresent(ticketDashboardPage.getRowForTicketWithID(ticketID), EDITED_REMAINDER_NOTE));
    }
}