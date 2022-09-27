package com.oss.iaa.servicedesk.CSDI;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.iaa.servicedesk.issue.ticket.TicketDashboardPage;
import com.oss.pages.iaa.servicedesk.issue.wizard.IssueCSDIWizardPage;

import io.qameta.allure.Description;

public class CreateIncidentTicketTest extends BaseTestCase {

    private static final String ISSUE_TITLE = "Selenium Test";
    private static final String SEVERITY = "Warning";
    private static final String STEPS_TO_REPRODUCE = "Test Steps";
    private static final String EXPECTED_BEHAVIOR = "Test Behavior";
    private static final String CONTACT_PERSON = "Test Person";
    private static final String IMPACT = "Low";
    private static final String IMPACT_EXPLANATION = "Test Explanation";
    private static final String URGENCY = "Low";
    private static final String URGENCY_EXPLANATION = "Test Explanation";
    private static final String ENVIRONMENT_TYPE = "REF";
    private static final String COMPONENT = "Service Desk";
    private static final String FLOW_TYPE = "Incident";
    private static final String INCIDENT_DESCRIPTION = "Selenium test ticket";
    private static final String SEVERITY_ATTRIBUTE_NAME = "Severity";

    private TicketDashboardPage ticketDashboardPage;
    private IssueCSDIWizardPage issueCSDIWizardPage;
    private String ticketID;

    @BeforeMethod
    public void goToTicketDashboardPage() {
        ticketDashboardPage = new TicketDashboardPage(driver, webDriverWait).goToPage(driver, BASIC_URL);
    }

    @Test(priority = 1, testName = "Create Incident Ticket", description = "Create Incident Ticket")
    @Description("Create Incident Ticket")
    public void createIncidentTicket(
    ) {
        issueCSDIWizardPage = ticketDashboardPage.openCreateTicketWizard(FLOW_TYPE).openIssueCSDIWizardPage();
        issueCSDIWizardPage.setIssueTitle(ISSUE_TITLE);
        issueCSDIWizardPage.enterIncidentDescription(INCIDENT_DESCRIPTION);
        issueCSDIWizardPage.setSeverity(SEVERITY);
        issueCSDIWizardPage.clickNextButtonInWizard();

        issueCSDIWizardPage.setStepsToReproduce(STEPS_TO_REPRODUCE);
        issueCSDIWizardPage.setExpectedBehavior(EXPECTED_BEHAVIOR);
        issueCSDIWizardPage.setContactPerson(CONTACT_PERSON);

        issueCSDIWizardPage.setImpact(IMPACT);
        issueCSDIWizardPage.setImpactExplanation(IMPACT_EXPLANATION);
        issueCSDIWizardPage.setUrgency(URGENCY);
        issueCSDIWizardPage.setUrgencyExplanation(URGENCY_EXPLANATION);
        issueCSDIWizardPage.setEnvironmentType(ENVIRONMENT_TYPE);
        issueCSDIWizardPage.setComponent(COMPONENT);
        issueCSDIWizardPage.clickAcceptButtonInWizard();

        ticketID = ticketDashboardPage.getIdFromMessage();
        Assert.assertTrue(ticketDashboardPage.getAttributeFromTable(0, SEVERITY_ATTRIBUTE_NAME).contains(SEVERITY));
        Assert.assertEquals(ticketDashboardPage.getRowForIssueWithID(ticketID), 0);
    }
}