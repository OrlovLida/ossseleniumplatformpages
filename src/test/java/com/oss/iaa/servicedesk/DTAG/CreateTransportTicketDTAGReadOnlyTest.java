package com.oss.iaa.servicedesk.DTAG;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.iaa.servicedesk.issue.ticket.TicketDashboardPage;
import com.oss.pages.iaa.servicedesk.issue.wizard.SDWizardPage;

import io.qameta.allure.Description;

public class CreateTransportTicketDTAGReadOnlyTest extends BaseTestCase {

    private TicketDashboardPage ticketDashboardPage;
    private SDWizardPage SDWizardPage;

    private final static String TRANSPORT_DASHBOARD_SUFFIX = "ForTransport";

    private final static String TT_WIZARD_SLA_CONTRACT= "TT_WIZARD_INPUT_SLA_CONTRACT_LABEL";
    private final static String TT_WIZARD_REPORTED_INCIDENT_TYPE = "TT_WIZARD_INPUT_REPORTED_INCIDENT_TYPE_LABEL";
    private final static String TT_WIZARD_TROUBLE_CATEGORY = "TT_WIZARD_INPUT_TROUBLE_CATEGORY_LABEL";
    private final static String TT_WIZARD_CANCEL_BUTTON_ID = "wizard-cancel-button-TT_WIZARD";

    private final static String TT_INCIDENT_DESCRIPTION = "Test selenium";
    private final static String TT_SLA_CONTRACT= "BEST EFFORT";
    private final static String TT_REPORTED_INCIDENT_TYPE = "Incident";
    private final static String TT_TROUBLE_CATEGORY = "Others";

    @BeforeMethod
    public void goToTicketDashboardPage() {
        ticketDashboardPage = new TicketDashboardPage(driver, webDriverWait).goToPage(driver, BASIC_URL, TRANSPORT_DASHBOARD_SUFFIX);
    }

    @Parameters({"MOIdentifier"})
    @Test(priority = 1, testName = "Create 'Transport IP' Ticket", description = "Create 'Transport IP' Ticket")
    @Description("Create 'Transport IP' Ticket")
    public void createTransportTicket(
            @Optional("TEST_MO_PATH") String MOIdentifier
    ) {
        SDWizardPage = ticketDashboardPage.openCreateTicketWizard("Transport_IP");
        SDWizardPage.getMoStep().enterTextIntoMOIdentifierField(MOIdentifier);
        SDWizardPage.getMoStep().selectRowInMOTable("0");
        SDWizardPage.clickNextButtonInWizard();
        SDWizardPage.insertValueContainsToComponent(TT_SLA_CONTRACT, TT_WIZARD_SLA_CONTRACT);
        SDWizardPage.enterIncidentDescription(TT_INCIDENT_DESCRIPTION);
        SDWizardPage.insertValueContainsToComponent(TT_REPORTED_INCIDENT_TYPE, TT_WIZARD_REPORTED_INCIDENT_TYPE);
        SDWizardPage.insertValueContainsToComponent(TT_TROUBLE_CATEGORY, TT_WIZARD_TROUBLE_CATEGORY);
        SDWizardPage.clickNextButtonInWizard();
        SDWizardPage.clickButton(TT_WIZARD_CANCEL_BUTTON_ID);
    }
}