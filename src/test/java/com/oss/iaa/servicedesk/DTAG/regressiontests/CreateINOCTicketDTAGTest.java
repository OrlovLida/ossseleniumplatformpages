package com.oss.iaa.servicedesk.DTAG.regressiontests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.iaa.servicedesk.issue.ticket.TicketDashboardPage;
import com.oss.pages.iaa.servicedesk.issue.wizard.SDWizardPage;

import io.qameta.allure.Description;

public class CreateINOCTicketDTAGTest extends BaseTestCase {

    private TicketDashboardPage ticketDashboardPage;
    private SDWizardPage SDWizardPage;

    private final static String INOC_DASHBOARD_SUFFIX = "ForInoc";

    private final static String TT_WIZARD_PRODUCT = "TT_WIZARD_INPUT_PRODUCT_LABEL";
    private final static String TT_WIZARD_SLA_CONTRACT= "TT_WIZARD_INPUT_SLA_CONTRACT_LABEL";
    private final static String TT_WIZARD_REPORTED_INCIDENT_TYPE = "TT_WIZARD_INPUT_REPORTED_INCIDENT_TYPE_LABEL";
    private static final String TT_WIZARD_INPUT_INCIDENT_TYPE = "TT_WIZARD_INPUT_INCIDENT_TITLE_LABEL";

    private final static String TT_PRODUCT = "GTEL";
    private final static String TT_SLA_CONTRACT= "BEST EFFORT";
    private final static String TT_INCIDENT_DESCRIPTION = "Test selenium";
    private final static String TT_REPORTED_INCIDENT_TYPE = "Erroring";
    private final static String TT_MANAGED_OBJECTS = "Managed Objects";

    @BeforeMethod
    public void goToTicketDashboardPage() {
        ticketDashboardPage = new TicketDashboardPage(driver, webDriverWait).goToPage(driver, BASIC_URL, INOC_DASHBOARD_SUFFIX);
    }

    @Parameters({"MOIdentifier"})
    @Test(priority = 1, testName = "Create 'INOC Service' Ticket", description = "Create 'INOC Service' Ticket")
    @Description("Create 'INOC Service' Ticket")
    public void createINOCTicket(
            @Optional("TEST_MO_PATH") String MOIdentifier
    ) {
        SDWizardPage = ticketDashboardPage.openCreateTicketWizard("INOC_Service");
        SDWizardPage.getMoStep().enterTextIntoMOIdentifierField(MOIdentifier);
        SDWizardPage.getMoStep().selectRowInMOTable("0");
        SDWizardPage.clickNextButtonInWizard();
        SDWizardPage.insertValueContainsToComponent(TT_PRODUCT, TT_WIZARD_PRODUCT);
        SDWizardPage.insertValueContainsToComponent(TT_SLA_CONTRACT, TT_WIZARD_SLA_CONTRACT);
        SDWizardPage.insertValueContainsToComponent(TT_INCIDENT_DESCRIPTION,TT_WIZARD_INPUT_INCIDENT_TYPE);
        SDWizardPage.enterIncidentDescription(TT_INCIDENT_DESCRIPTION);
        SDWizardPage.insertValueContainsToComponent(TT_REPORTED_INCIDENT_TYPE, TT_WIZARD_REPORTED_INCIDENT_TYPE);
        SDWizardPage.clickNextButtonInWizard();
        SDWizardPage.clickAcceptButtonInWizard();
        Assert.assertEquals(ticketDashboardPage.getAttributeFromTable(0, TT_MANAGED_OBJECTS), MOIdentifier);
    }
}