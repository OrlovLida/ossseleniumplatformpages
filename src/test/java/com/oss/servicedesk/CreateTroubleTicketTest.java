package com.oss.servicedesk;

import com.oss.BaseTestCase;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.servicedesk.ticket.TicketDashboardPage;
import com.oss.pages.servicedesk.ticket.TicketDetailsPage;
import com.oss.pages.servicedesk.ticket.TicketSearchPage;
import com.oss.pages.servicedesk.ticket.wizard.MOStep;
import com.oss.pages.servicedesk.ticket.wizard.WizardPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners({TestListener.class})
public class CreateTroubleTicketTest extends BaseTestCase {

    private TicketDashboardPage ticketDashboardPage;
    private TicketSearchPage ticketSearchPage;
    private WizardPage wizardPage;
    private TicketDetailsPage ticketDetailsPage;

    private final static String MO_IDENTIFIER = "(03) LTE";
    private final static String TT_ASSIGNEE = "ca_kodrobinska";
    private final static String TT_NEW_ASSIGNEE = "ossadmin";
    private final static String TT_WIZARD_ASSIGNEE = "TT_WIZARD_INPUT_ASSIGNEE_LABEL";
    private final static String TT_WIZARD_DESCRIPTION = "TT_WIZARD_INPUT_INCIDENT_DESCRIPTION_LABEL";


    @Test(priority = 1, testName = "Open TT Dashboard", description = "Open TT Dashboard")
    @Description("Open TT Dashboard")
    public void goToTicketDashboardPage() {
        ticketDashboardPage = TicketDashboardPage.openTicketDashboard(driver, BASIC_URL);
    }

    @Test(priority = 2, testName = "Create NTT Ticket", description = "Create Ticket")
    @Description("Create Ticket")
    public void createNTTTicket() {
        wizardPage = ticketDashboardPage.openCreateTicketWizard(driver, "NTT");
        wizardPage.getMoStep().enterTextIntoSearchComponent(driver, MO_IDENTIFIER);
        wizardPage.getMoStep().selectRowInMOTable(driver,"0");
        wizardPage.clickNextButtonInWizard(driver);
        wizardPage.insertValueToSearchComponent(TT_ASSIGNEE, TT_WIZARD_ASSIGNEE);
        wizardPage.clickNextButtonInWizard(driver);
        wizardPage.clickNextButtonInWizard(driver);
        wizardPage.clickAcceptButtonInWizard(driver);

    }



    @Test(priority = 3, testName = "Open Tickets Search view", description = "Open Tickets Search view")
    @Description("Open Tickets Search view")
    public void goToTicketSearchView() {
        ticketSearchPage = TicketSearchPage.goToPage(driver, BASIC_URL);
    }

    @Test(priority = 4, testName = "Open Ticket Details", description = "Open Ticket Details")
    @Description("Open Ticket Details")
    public void openTicketDetails() {

        ticketSearchPage.checkIfTicketExists(TicketSearchPage.ASSIGNEE_ATTRIBUTE, TT_ASSIGNEE);
        // TODO sorting by creation time
        String ticketId = ticketSearchPage.geIdForNthTicketInTable(0);
        ticketSearchPage.openTicketDetailsView("0", BASIC_URL);
        ticketDetailsPage = new TicketDetailsPage(driver);
        Assert.assertTrue(ticketDetailsPage.checkIfDetailViewOfGivenTTIsOpened(ticketId));


    }


    @Test(priority = 5, testName = "Edit Ticket Details", description = "Edit Ticket Details")
    @Description("Edit Ticket Details")
    public void editTicketDetails() {
        wizardPage = ticketDetailsPage.openEditTicketWizard(driver);
        wizardPage.clickNextButtonInWizard(driver);
        wizardPage.insertValueToSearchComponent(TT_NEW_ASSIGNEE, TT_WIZARD_ASSIGNEE);
        wizardPage.clickNextButtonInWizard(driver);
        wizardPage.clickNextButtonInWizard(driver);
        wizardPage.clickAcceptButtonInWizard(driver);
    }



}
