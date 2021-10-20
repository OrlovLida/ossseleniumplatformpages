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
    public final static String TT_DESCRIPTION = "TestSelenium";
    private final static String TT_DESCRIPTION_EDITED = "TestSelenium_edited";
    private final static String TT_NEW_ASSIGNEE = "admin oss";
    private final static String TT_CORRELATION_ID = "12345";
    private final static String TT_REFERENCE_ID = "12345";
    private final static String TT_ESCALATED_TO = "admin oss";
    private final static String TT_SEVERITY = "Warning";

    private final static String TT_WIZARD_ASSIGNEE = "TT_WIZARD_INPUT_ASSIGNEE_LABEL";
    private final static String TT_WIZARD_ESCALATED_TO = "TT_WIZARD_INPUT_ESCALATED_TO_LABEL";
    private final static String TT_WIZARD_SEVERITY = "TT_WIZARD_INPUT_SEVERITY_LABEL";
    private final static String CREATE_DATE_FILTER_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private final static String TT_WIZARD_CORRELATION_ID = "ISSUE_CORRELATION_ID";
    private final static String TT_WIZARD_REFERENCE_ID = "TT_WIZARD_INPUT_REFERENCE_ID_LABEL";
    private final static String TT_WIZARD_ISSUE_START_DATE_ID = "IssueStartDate";
    private final static String TT_WIZARD_MESSAGE_DATE_ID = "PleaseProvideTheTimeOnTheHandsetTheTxtMessageArrived";
    private final static String DETAILS_WINDOW_ID = "_detailsWindow";

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
}