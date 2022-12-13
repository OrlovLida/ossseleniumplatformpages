package com.oss.iaa.servicedesk.DTAG;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.iaa.servicedesk.issue.ticket.TicketDashboardPage;
import com.oss.pages.iaa.servicedesk.issue.wizard.SDWizardPage;

import io.qameta.allure.Description;

public class CreateVoiceTicketDTAGReadOnlyTest extends BaseTestCase {

    private TicketDashboardPage ticketDashboardPage;
    private SDWizardPage SDWizardPage;

    private final static String VOICE_DASHBOARD_SUFFIX = "ForVoice";

    private final static String TT_WIZARD_PRODUCT = "TT_WIZARD_INPUT_PRODUCT_LABEL";
    private final static String TT_WIZARD_PRIORITY = "TT_WIZARD_INPUT_PRIORITY_LABEL";
    private final static String TT_WIZARD_DESTINATION = "TT_WIZARD_INPUT_DESTINATION_VOICE_LABEL";
    private final static String TT_WIZARD_REPORTED_INCIDENT_TYPE = "TT_WIZARD_INPUT_REPORTED_INCIDENT_TYPE_LABEL";
    private final static String TT_WIZARD_TROUBLE_CATEGORY = "TT_WIZARD_INPUT_TROUBLE_CATEGORY_LABEL";
    private final static String TT_WIZARD_CANCEL_BUTTON_ID = "wizard-cancel-button-TT_WIZARD";

    private final static String TT_PRODUCT = "Inbound traffic";
    private final static String TT_PRIORITY = "Prio Nr5 720h";
    private final static String TT_DESTINATION = "Poland";
    private final static String TT_INCIDENT_DESCRIPTION = "Test selenium";
    private final static String TT_REPORTED_INCIDENT_TYPE = "ACD";
    private final static String TT_TROUBLE_CATEGORY = "No product";

    @BeforeMethod
    public void goToTicketDashboardPage() {
        ticketDashboardPage = new TicketDashboardPage(driver, webDriverWait).goToPage(driver, BASIC_URL, VOICE_DASHBOARD_SUFFIX);
    }

    @Parameters({"MOIdentifier"})
    @Test(priority = 1, testName = "Create 'Voice Other' Ticket", description = "Create 'Voice Other' Ticket")
    @Description("Create 'Voice Other' Ticket")
    public void createVoiceOtherTicket(
            @Optional("ANAM/IRL10/-/SMS+protect/22044/Z030A/0VS/100 (22044144824)") String MOIdentifier
    ) {
        SDWizardPage = ticketDashboardPage.openCreateTicketWizard("Voice_Other");
        SDWizardPage.getMoStep().enterTextIntoMOIdentifierField(MOIdentifier);
        SDWizardPage.getMoStep().selectRowInMOTable("0");
        SDWizardPage.clickNextButtonInWizard();
        SDWizardPage.insertValueContainsToComponent(TT_PRODUCT, TT_WIZARD_PRODUCT);
        SDWizardPage.insertValueContainsToComponent(TT_PRIORITY, TT_WIZARD_PRIORITY);
        SDWizardPage.insertValueContainsToComponent(TT_DESTINATION, TT_WIZARD_DESTINATION);
        SDWizardPage.enterIncidentDescription(TT_INCIDENT_DESCRIPTION);
        SDWizardPage.insertValueContainsToComponent(TT_REPORTED_INCIDENT_TYPE, TT_WIZARD_REPORTED_INCIDENT_TYPE);
        SDWizardPage.insertValueContainsToComponent(TT_TROUBLE_CATEGORY, TT_WIZARD_TROUBLE_CATEGORY);
        SDWizardPage.clickNextButtonInWizard();
        SDWizardPage.clickButton(TT_WIZARD_CANCEL_BUTTON_ID);
    }
}