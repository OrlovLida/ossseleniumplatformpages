package com.oss.servicedesk;

import com.oss.BaseTestCase;
import com.oss.pages.servicedesk.ticket.TicketDashboardPage;
import com.oss.pages.servicedesk.ticket.TicketDetailsPage;
import com.oss.pages.servicedesk.ticket.wizard.WizardPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static com.oss.servicedesk.CreateTroubleTicketTestVFNZ.TT_DESCRIPTION_EDITED;

@Listeners({TestListener.class})
public class TicketDetailsViewTestVFNZ extends BaseTestCase {

    private TicketDetailsPage ticketDetailsPage;
    private TicketDashboardPage ticketDashboardPage;

    private final static String TT_EXTERNAL = "Selenium test external";
    private final static String TT_LIBRARY_TYPE = "Category";
    private final static String TT_CATEGORY_NAME = "Data Problem";
    private final static String NOTIFICATION_CHANNEL = "Internal";
    private final static String NOTIFICATION_MESSAGE = "test selenium message";
    private final static String NOTIFICATION_TO = "ca_kodrobinska";
    private final static String NOTIFICATION_TYPE= "Success";

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


    @BeforeClass
    public void goToTicketDetailsPage() {
        ticketDashboardPage = TicketDashboardPage.goToPage(driver, BASIC_URL);
        //TODO do zmiany na przejście z Ticket Search Page -> Details, gdy będą tam pojawiać się aktualne tickety - wyszukać po assignee - znaleźć stworzony TT na potrzeby Selenium
        ticketDetailsPage = ticketDashboardPage.openTicketDetailsView("14", BASIC_URL);
    }

    @Test(priority = 1, testName = "Add external to ticket", description = "Add external to ticket")
    @Description("Add external to ticket")
    public void addExternalToTicket() {
        ticketDetailsPage.selectTab(driver, EXTERNAL_TAB_ARIA_CONTROLS);
        ticketDetailsPage.clickContextAction(ADD_EXTERNAL_LABEL);
        WizardPage wizardPage = new WizardPage(driver);
        wizardPage.insertValueToTextComponent(TT_EXTERNAL, WIZARD_EXTERNAL_NAME);
        wizardPage.clickCreateExternalButtonInWizard(driver);
        Assert.assertTrue(ticketDetailsPage.checkExistingExternal(TT_EXTERNAL));
    }

    @Test(priority = 2, testName = "Add dictionary to ticket", description = "Add dictionary to ticket")
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

    @Test(priority = 3, testName = "Check Description Tab", description = "Check Description Tab")
    @Description("Check Description Tab")
    public void checkDescriptionTab() {
        ticketDetailsPage.selectTab(driver, DESCRIPTION_TAB_ARIA_CONTROLS);
        Assert.assertTrue(ticketDetailsPage.checkDisplayedText(TT_DESCRIPTION_EDITED, TABLES_WINDOW_ID));
    }

    @Test(priority = 4, testName = "Check Messages Tab - add Notification", description = "Check Messages Tab - add Notification")
    @Description("Check Messages Tab - add Notification")
    public void checkMessagesTab() {
        ticketDetailsPage.selectTab(driver, MESSAGES_TAB_ARIA_CONTROLS);
        ticketDetailsPage.createNewNotificationOnMessagesTab();
        WizardPage notificationWizardPage = new WizardPage(driver);
        notificationWizardPage.insertValueToComboBoxComponent(NOTIFICATION_CHANNEL, NOTIFICATION_WIZARD_CHANNEL_ID );
        notificationWizardPage.insertValueToTextAreaComponent(NOTIFICATION_MESSAGE, NOTIFICATION_WIZARD_MESSAGE_ID);
        notificationWizardPage.insertValueToMultiSearchComponent(NOTIFICATION_TO, NOTIFICATION_WIZARD_TO_ID);
        notificationWizardPage.clickComboBox(NOTIFICATION_WIZARD_TEMPLATE_ID);
        notificationWizardPage.insertValueToComboBoxComponent(NOTIFICATION_TYPE, NOTIFICATION_WIZARD_TYPE_ID);
        notificationWizardPage.clickAcceptButtonInWizard(driver);
    }
}
