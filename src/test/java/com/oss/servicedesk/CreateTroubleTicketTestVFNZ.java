package com.oss.servicedesk;

import com.oss.BaseTestCase;
import com.oss.pages.servicedesk.ticket.TicketDashboardPage;
import com.oss.pages.servicedesk.ticket.TicketDetailsPage;
import com.oss.pages.servicedesk.ticket.TicketSearchPage;
import com.oss.pages.servicedesk.ticket.wizard.WizardPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Listeners({TestListener.class})
public class CreateTroubleTicketTestVFNZ extends BaseTestCase {

    private TicketDashboardPage ticketDashboardPage;
    private TicketSearchPage ticketSearchPage;
    private WizardPage wizardPage;
    private TicketDetailsPage ticketDetailsPage;

    private final static String MO_IDENTIFIER = "CFS_SOM_01_901";
    private final static String TT_ASSIGNEE = "ca_kodrobinska";
    private final static String TT_DESCRIPTION = "TestSelenium";
    private final static String TT_DESCRIPTION_EDITED = "TestSelenium_edited";
    private final static String TT_NEW_ASSIGNEE = "admin oss";
    private final static String TT_CORRELATION_ID = "12345";
    private final static String TT_REFERENCE_ID = "12345";
    private final static String TT_ESCALATED_TO = "admin oss";
    private final static String TT_SEVERITY = "Warning";
    private final static String TT_EXTERNAL = "Selenium test external";
    private final static String TT_LIBRARY_TYPE = "Category";
    private final static String TT_CATEGORY_NAME = "Data Problem";

    private final static String NOTIFICATION_CHANNEL = "Internal";
    private final static String NOTIFICATION_MESSAGE = "test selenium message";
    private final static String NOTIFICATION_TO = "ca_kodrobinska";
    private final static String NOTIFICATION_TYPE = "Success";

    private final static String TT_WIZARD_ASSIGNEE = "TT_WIZARD_INPUT_ASSIGNEE_LABEL";
    private final static String TT_WIZARD_ESCALATED_TO = "TT_WIZARD_INPUT_ESCALATED_TO_LABEL";
    private final static String TT_WIZARD_SEVERITY = "TT_WIZARD_INPUT_SEVERITY_LABEL";
    private final static String CREATE_DATE_FILTER_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private final static String TT_WIZARD_CORRELATION_ID = "ISSUE_CORRELATION_ID";
    private final static String TT_WIZARD_REFERENCE_ID = "TT_WIZARD_INPUT_REFERENCE_ID_LABEL";
    private final static String TT_WIZARD_ISSUE_START_DATE_ID = "IssueStartDate";
    private final static String TT_WIZARD_MESSAGE_DATE_ID = "PleaseProvideTheTimeOnTheHandsetTheTxtMessageArrived";

    private final static String DETAILS_WINDOW_ID = "_detailsWindow";
    private final static String WIZARD_EXTERNAL_NAME = "_name";
    private final static String EXTERNAL_TAB_ARIA_CONTROLS = "_detailsExternalTab";
    private final static String DICTIONARIES_TAB_ARIA_CONTROLS = "_dictionariesTab";
    private final static String DESCRIPTION_TAB_ARIA_CONTROLS = "_descriptionTab";
    private final static String MESSAGES_TAB_ARIA_CONTROLS = "_messagesTab";
    private final static String ADD_EXTERNAL_LABEL = "Add External";
    private final static String ADD_TO_LIBRARY_LABEL = "Add to Library";
    private final static String WIZARD_LIBRARY_TYPE_ID = "{\"identifier\":\"type\",\"parentIdentifier\":\"type\",\"parentValueIdentifier\":\"\",\"groupId\":\"type\"}"; //  TODO Do zmiany po rozwiązaniu OSSWEB-14814
    private final static String WIZARD_CATEGORY_ID = "{\"identifier\":\"Category\",\"parentIdentifier\":\"type\",\"parentValueIdentifier\":\"\",\"groupId\":\"Category\"}-input"; //  TODO Do zmiany po rozwiązaniu OSSWEB-14814
    private final static String TABLES_WINDOW_ID = "_tablesWindow";

    private final static String NOTIFICATION_WIZARD_CHANNEL_ID = "channel-component-input";
    private final static String NOTIFICATION_WIZARD_MESSAGE_ID = "message-component";
    private final static String NOTIFICATION_WIZARD_TO_ID = "internal-to-component";
    private final static String NOTIFICATION_WIZARD_TYPE_ID = "internal-type-component";
    private final static String NOTIFICATION_WIZARD_TEMPLATE_ID = "template-component";

    public static final DateTimeFormatter CREATE_DATE_FILTER_DATE_FORMATTER = DateTimeFormatter.ofPattern(CREATE_DATE_FILTER_DATE_PATTERN);

    @BeforeClass
    public void goToTicketDashboardPage() {
        ticketDashboardPage = TicketDashboardPage.goToPage(driver, BASIC_URL);
    }

    @Test(priority = 1, testName = "Create CTT Ticket", description = "Create CTT Ticket")
    @Description("Create CTT Ticket")
    public void createCTTTicket() {
        wizardPage = ticketDashboardPage.openCreateTicketWizard(driver, "CTT");
        wizardPage.getMoStep().enterTextIntoSearchComponent(driver, MO_IDENTIFIER);
        wizardPage.getMoStep().selectRowInMOTable(driver, "0");
        wizardPage.clickNextButtonInWizard(driver);
        wizardPage.insertValueToSearchComponent(TT_ASSIGNEE, TT_WIZARD_ASSIGNEE);
        wizardPage.insertValueToTextComponent(TT_CORRELATION_ID, TT_WIZARD_CORRELATION_ID);
        wizardPage.insertValueToTextComponent(TT_REFERENCE_ID, TT_WIZARD_REFERENCE_ID);
        wizardPage.enterIncidentDescription(TT_DESCRIPTION);
        wizardPage.clickNextButtonInWizard(driver);
        String date = LocalDateTime.now().minusMinutes(5).format(CREATE_DATE_FILTER_DATE_FORMATTER);
        wizardPage.insertValueToTextComponent(date, TT_WIZARD_ISSUE_START_DATE_ID);
        wizardPage.insertValueToTextComponent(date, TT_WIZARD_MESSAGE_DATE_ID);
        wizardPage.clickAcceptButtonInWizard(driver);
        Assert.assertEquals(ticketDashboardPage.getAssigneeForNthTicketInTTTable(0), TT_ASSIGNEE);
    }

    @Test(priority = 2, testName = "Edit Ticket Details", description = "Edit Ticket Details")
    @Description("Edit Ticket Details")
    public void editTicketDetails() {
        ticketDetailsPage = ticketDashboardPage.openTicketDetailsView("0", BASIC_URL);
        ticketDetailsPage.maximizeWindow(DETAILS_WINDOW_ID);
        wizardPage = ticketDetailsPage.openEditTicketWizard(driver);
        wizardPage.clickNextButtonInWizard(driver);
        wizardPage.insertValueToSearchComponent(TT_NEW_ASSIGNEE, TT_WIZARD_ASSIGNEE);
        wizardPage.enterIncidentDescription(TT_DESCRIPTION_EDITED);
        wizardPage.insertValueToSearchComponent(TT_ESCALATED_TO, TT_WIZARD_ESCALATED_TO);
        wizardPage.insertValueToComboBoxComponent(TT_SEVERITY, TT_WIZARD_SEVERITY);
        wizardPage.clickNextButtonInWizard(driver);
        wizardPage.clickAcceptButtonInWizard(driver);
    }

    @Test(priority = 3, testName = "Open Ticket Details from Tickets Search view", description = "Open Ticket Details from Tickets Search view")
    @Description("Open Ticket Details from Tickets Search view")
    public void openTicketDetailsFromTicketsSearchView() {
        ticketSearchPage = new TicketSearchPage(driver);
        ticketSearchPage.goToPage(driver, BASIC_URL);
        ticketSearchPage.clickFilterButton();
        ticketSearchPage.filterByTextField(TicketSearchPage.ASSIGNEE_ATTRIBUTE, TT_ASSIGNEE);
//      TODO Do odkomentowania po naprawieniu aktualizacji widoku Ticket Search (OSSSD-2605)
//        String date = LocalDateTime.now().minusMinutes(10).format(CREATE_DATE_FILTER_DATE_FORMATTER);
//        ticketSearchPage.clickFilterButton();
//        ticketSearchPage.filterByTextField(TicketSearchPage.CREATION_TIME_ATTRIBUTE, date);
        ticketSearchPage.clickFilterButton();
        ticketSearchPage.filterByTextField(TicketSearchPage.DESCRIPTION_ATTRIBUTE, TT_DESCRIPTION);
        ticketSearchPage.clickFilterButton();
        ticketSearchPage.filterByComboBox(TicketSearchPage.STATUS_ATTRIBUTE, "New");
        ticketSearchPage.openTicketDetailsView("0", BASIC_URL);
    }

    @Test(priority = 4, testName = "Add external to ticket", description = "Add external to ticket")
    @Description("Add external to ticket")
    public void addExternalToTicket() {
        ticketDashboardPage = TicketDashboardPage.goToPage(driver, BASIC_URL);
        //TODO do zmiany na przejście z Ticket Search Page -> Details, gdy będą tam pojawiać się aktualne tickety - wyszukać po assignee - znaleźć stworzony TT na potrzeby Selenium
        ticketDetailsPage = ticketDashboardPage.openTicketDetailsView("6", BASIC_URL);
        ticketDetailsPage.selectTab(driver, EXTERNAL_TAB_ARIA_CONTROLS);
        ticketDetailsPage.clickContextAction(ADD_EXTERNAL_LABEL);
        WizardPage wizardPage = new WizardPage(driver);
        wizardPage.insertValueToTextComponent(TT_EXTERNAL, WIZARD_EXTERNAL_NAME);
        wizardPage.clickCreateExternalButtonInWizard(driver);
        Assert.assertTrue(ticketDetailsPage.checkExistingExternal(TT_EXTERNAL));
    }

    @Test(priority = 5, testName = "Add dictionary to ticket", description = "Add dictionary to ticket")
    @Description("Add dictionary to ticket")
    public void addDictionaryToTicket() {
        ticketDetailsPage.selectTab(driver, DICTIONARIES_TAB_ARIA_CONTROLS);
        ticketDetailsPage.clickContextAction(ADD_TO_LIBRARY_LABEL);
        WizardPage dictionaryWizardPage = new WizardPage(driver);
        dictionaryWizardPage.insertValueToComboBoxComponent(TT_LIBRARY_TYPE, WIZARD_LIBRARY_TYPE_ID);
        dictionaryWizardPage.insertValueToComboBoxComponent(TT_CATEGORY_NAME, WIZARD_CATEGORY_ID);
        dictionaryWizardPage.clickAcceptButtonInWizard(driver);
        Assert.assertEquals(ticketDetailsPage.checkExistingDictionary(), TT_CATEGORY_NAME);
    }

    @Test(priority = 6, testName = "Check Description Tab", description = "Check Description Tab")
    @Description("Check Description Tab")
    public void checkDescriptionTab() {
        ticketDetailsPage.selectTab(driver, DESCRIPTION_TAB_ARIA_CONTROLS);
        Assert.assertTrue(ticketDetailsPage.checkDisplayedText(TT_DESCRIPTION_EDITED, TABLES_WINDOW_ID));
    }

    @Test(priority = 7, testName = "Check Messages Tab - add Notification", description = "Check Messages Tab - add Notification")
    @Description("Check Messages Tab - add Notification")
    public void checkMessagesTab() {
        ticketDetailsPage.selectTab(driver, MESSAGES_TAB_ARIA_CONTROLS);
        ticketDetailsPage.createNewNotificationOnMessagesTab();
        WizardPage notificationWizardPage = new WizardPage(driver);
        notificationWizardPage.insertValueToComboBoxComponent(NOTIFICATION_CHANNEL, NOTIFICATION_WIZARD_CHANNEL_ID);
        notificationWizardPage.insertValueToTextAreaComponent(NOTIFICATION_MESSAGE, NOTIFICATION_WIZARD_MESSAGE_ID);
        notificationWizardPage.insertValueToMultiSearchComponent(NOTIFICATION_TO, NOTIFICATION_WIZARD_TO_ID);
        notificationWizardPage.clickComboBox(NOTIFICATION_WIZARD_TEMPLATE_ID);
        notificationWizardPage.insertValueToComboBoxComponent(NOTIFICATION_TYPE, NOTIFICATION_WIZARD_TYPE_ID);
        notificationWizardPage.clickAcceptButtonInWizard(driver);
    }
}