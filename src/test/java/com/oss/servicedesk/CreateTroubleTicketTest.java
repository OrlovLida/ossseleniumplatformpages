package com.oss.servicedesk;

import com.oss.BaseTestCase;
import com.oss.pages.servicedesk.ticket.TicketDashboardPage;
import com.oss.pages.servicedesk.ticket.TicketDetailsPage;
import com.oss.pages.servicedesk.ticket.TicketSearchPage;
import com.oss.pages.servicedesk.ticket.wizard.SDWizardPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Listeners({TestListener.class})
public class CreateTroubleTicketTest extends BaseTestCase {

    private TicketDashboardPage ticketDashboardPage;
    private TicketSearchPage ticketSearchPage;
    private SDWizardPage SDWizardPage;
    private TicketDetailsPage ticketDetailsPage;

    private final static String MO_IDENTIFIER = "(03) LTE";
    private final static String TT_ASSIGNEE = "ca_kodrobinska";
    private final static String TT_DESCRIPTION = "TestSelenium";
    private final static String TT_NEW_ASSIGNEE = "ossadmin";
    private final static String TT_WIZARD_ASSIGNEE = "TT_WIZARD_INPUT_ASSIGNEE_LABEL";
    private static final String CREATE_DATE_FILTER_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter CREATE_DATE_FILTER_DATE_FORMATTER = DateTimeFormatter.ofPattern(CREATE_DATE_FILTER_DATE_PATTERN);

    @BeforeClass
    public void goToTicketDashboardPage() {
        ticketDashboardPage = TicketDashboardPage.goToPage(driver, BASIC_URL);
    }

    @Test(priority = 1, testName = "Create NTT Ticket", description = "Create Ticket")
    @Description("Create Ticket")
    public void createNTTTicket() {
        SDWizardPage = ticketDashboardPage.openCreateTicketWizard("NTT");
        SDWizardPage.getMoStep().enterTextIntoSearchComponent(MO_IDENTIFIER);
        SDWizardPage.getMoStep().selectRowInMOTable("0");
        SDWizardPage.clickNextButtonInWizard();
        SDWizardPage.insertValueToSearchComponent(TT_ASSIGNEE, TT_WIZARD_ASSIGNEE);
        SDWizardPage.enterIncidentDescription(TT_DESCRIPTION);
        SDWizardPage.clickNextButtonInWizard();
        SDWizardPage.clickNextButtonInWizard();
        SDWizardPage.clickAcceptButtonInWizard();
    }

    @Test(priority = 2, testName = "Open Ticket Details from Tickets Search view", description = "Open Ticket Details from Tickets Search view")
    @Description("Open Ticket Details from Tickets Search view")
    public void openTicketDetailsFromTicketsSearchView() {
        ticketSearchPage = new TicketSearchPage(driver, webDriverWait);
        ticketSearchPage = ticketSearchPage.goToPage(driver, BASIC_URL);
        ticketSearchPage.filterByTextField(TicketSearchPage.ASSIGNEE_ATTRIBUTE, TT_ASSIGNEE);
        String date = LocalDateTime.now().minusMinutes(5).format(CREATE_DATE_FILTER_DATE_FORMATTER);
        ticketSearchPage.filterByTextField(TicketSearchPage.CREATION_TIME_ATTRIBUTE, date);
        ticketSearchPage.filterByComboBox(TicketSearchPage.STATUS_ATTRIBUTE, "Open");
        ticketDetailsPage = ticketSearchPage.openTicketDetailsView("0", BASIC_URL);
    }

    @Test(priority = 3, testName = "Edit Ticket Details", description = "Edit Ticket Details")
    @Description("Edit Ticket Details")
    public void editTicketDetails() {
        ticketDetailsPage = new TicketDetailsPage(driver, webDriverWait);
        SDWizardPage = ticketDetailsPage.openEditTicketWizard();
        SDWizardPage.clickNextButtonInWizard();
        SDWizardPage.insertValueToSearchComponent(TT_NEW_ASSIGNEE, TT_WIZARD_ASSIGNEE);
        SDWizardPage.clickNextButtonInWizard();
        SDWizardPage.clickNextButtonInWizard();
        SDWizardPage.clickAcceptButtonInWizard();
    }

}
