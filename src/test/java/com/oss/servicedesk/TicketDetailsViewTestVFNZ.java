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

@Listeners({TestListener.class})
public class TicketDetailsViewTestVFNZ extends BaseTestCase {

    private TicketDetailsPage ticketDetailsPage;
    private TicketDashboardPage ticketDashboardPage;

    private final static String TT_EXTERNAL = "Selenium test external";

    private final static String WIZARD_EXTERNAL_NAME = "_name";
    private final static String EXTERNAL_TAB_LABEL = "External";
    private final static String ADD_EXTERNAL_LABEL = "Add External";
    private final static String DICTIONARIES_TAB_LABEL = "Dictionaries";
    private final static String ADD_TO_LIBRARY_LABEL = "Add to Library";

    @BeforeClass
    public void goToTicketDetailsPage() {
        ticketDashboardPage = TicketDashboardPage.goToPage(driver, BASIC_URL);
        ticketDetailsPage = ticketDashboardPage.openTicketDetailsView("0", BASIC_URL);
    }

    @Test(priority = 1, testName = "Add external to ticket", description = "Add external to ticket")
    @Description("Add external to ticket")
    public void addExternalToTicket() {
        ticketDetailsPage.selectTab(driver, EXTERNAL_TAB_LABEL);
        ticketDetailsPage.clickContextAction(ADD_EXTERNAL_LABEL);
        WizardPage wizardPage = new WizardPage(driver);
        wizardPage.insertValueToTextComponent(TT_EXTERNAL, WIZARD_EXTERNAL_NAME);
        wizardPage.clickCreateExternalButtonInWizard(driver);
        Assert.assertTrue(ticketDetailsPage.checkExistingExternal(TT_EXTERNAL));
    }

    @Test(priority = 2, testName = "Add dictionary to ticket", description = "Add dictionary to ticket")
    @Description("Add dictionary to ticket")
    public void addDictionaryToTicket() {
        ticketDetailsPage.selectTab(driver, DICTIONARIES_TAB_LABEL);
        ticketDetailsPage.clickContextAction(ADD_TO_LIBRARY_LABEL);
    }
}
