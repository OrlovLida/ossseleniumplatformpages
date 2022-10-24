package com.oss.iaa.servicedesk.CSDI;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.iaa.servicedesk.issue.IssueDetailsPage;
import com.oss.pages.iaa.servicedesk.issue.ticket.ClosedTicketsPage;
import com.oss.pages.iaa.servicedesk.issue.ticket.MyGroupTicketsPage;
import com.oss.pages.iaa.servicedesk.issue.ticket.MyTicketsPage;
import com.oss.pages.iaa.servicedesk.issue.ticket.TicketDashboardPage;
import com.oss.pages.iaa.servicedesk.issue.ticket.TicketOverviewTab;
import com.oss.pages.iaa.servicedesk.issue.ticket.TicketSearchPage;
import com.oss.pages.iaa.servicedesk.issue.wizard.IssueCSDIWizardPage;
import com.oss.pages.platform.NotificationWrapperPage;

import io.qameta.allure.Description;

import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.DOWNLOAD_FILE;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.EXPORT_WIZARD_ID;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.ID_ATTRIBUTE;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.TROUBLE_TICKET_ISSUE_TYPE;

public class CreateIncidentAndTicketSearchTest extends BaseTestCase {

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
    private static final String STATUS_RESOLVED = "Resolved";
    private static final String STATUS_CLOSED = "Closed";
    private static final String TT_DOWNLOAD_FILE = "TroubleTicket*.xlsx";

    private TicketDashboardPage ticketDashboardPage;
    private IssueCSDIWizardPage issueCSDIWizardPage;
    private MyGroupTicketsPage myGroupTicketsPage;
    private MyTicketsPage myTicketsPage;
    private TicketSearchPage ticketSearchPage;
    private NotificationWrapperPage notificationWrapperPage;
    private IssueDetailsPage issueDetailsPage;
    private TicketOverviewTab ticketOverviewTab;
    private ClosedTicketsPage closedTicketsPage;
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

    @Test(priority = 2, testName = "Export from Ticket Dashboard", description = "Export from Ticket Dashboard")
    @Description("Export from Ticket Dashboard")
    public void exportFromTicketDashboard() {
        ticketDashboardPage.exportFromDashboard(TT_DOWNLOAD_FILE);
        notificationWrapperPage = ticketDashboardPage.openNotificationPanel();

        Assert.assertEquals(notificationWrapperPage.amountOfNotifications(), 1);
        Assert.assertTrue(ticketDashboardPage.checkIfFileIsNotEmpty(TT_DOWNLOAD_FILE));
    }

    @Test(priority = 3, testName = "Check My Tickets", description = "Check My Tickets")
    @Description("Check My Tickets")
    public void checkMyTickets() {
        myTicketsPage = new MyTicketsPage(driver, webDriverWait).openView(driver, BASIC_URL);
        myTicketsPage.filterBy(ID_ATTRIBUTE, ticketID);
        Assert.assertFalse(myTicketsPage.isIssueTableEmpty());
        Assert.assertEquals(myTicketsPage.getIdForNthTicketInTable(0), ticketID);
    }

    @Test(priority = 4, testName = "Check Ticket Search", description = "Check Ticket Search")
    @Description("Check Ticket Search")
    public void checkTicketSearch() {
        ticketSearchPage = new TicketSearchPage(driver, webDriverWait).openView(driver, BASIC_URL);
        ticketSearchPage.filterBy(ID_ATTRIBUTE, ticketID);
        Assert.assertFalse(ticketSearchPage.isIssueTableEmpty());
        Assert.assertEquals(ticketSearchPage.getIdForNthTicketInTable(0), ticketID);
    }

    @Test(priority = 5, testName = "Check My Group Tickets", description = "Check My Group Tickets")
    @Description("Check My Group Tickets")
    public void checkMyGroupTickets() {
        myGroupTicketsPage = new MyGroupTicketsPage(driver, webDriverWait).openView(driver, BASIC_URL);
        myGroupTicketsPage.filterBy(ID_ATTRIBUTE, ticketID);
        Assert.assertFalse(myGroupTicketsPage.isIssueTableEmpty());
        Assert.assertEquals(myGroupTicketsPage.getIdForNthTicketInTable(0), ticketID);
    }

    @Test(priority = 6, testName = "Export from My Group Tickets", description = "Export from My Group Tickets")
    @Description("Export from My Group Tickets")
    public void exportFromMyGroupTickets() {
        myGroupTicketsPage = new MyGroupTicketsPage(driver, webDriverWait).openView(driver, BASIC_URL);
        try {
            myGroupTicketsPage.exportFromSearchViewTable(EXPORT_WIZARD_ID);
        } catch (RuntimeException e) {
            Assert.fail(e.getMessage());
        }
        notificationWrapperPage = new NotificationWrapperPage(driver);

        Assert.assertEquals(notificationWrapperPage.amountOfNotifications(), 0);
        Assert.assertTrue(myGroupTicketsPage.checkIfFileIsNotEmpty(DOWNLOAD_FILE));
    }

    @Test(priority = 7, testName = "Close Ticket", description = "Open ticket and go through all necessary steps to close it")
    @Description("Open ticket and go through all necessary steps to close it")
    public void closeTicket(
    ) {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        ticketOverviewTab = (TicketOverviewTab) issueDetailsPage.selectOverviewTab(TROUBLE_TICKET_ISSUE_TYPE);
        ticketOverviewTab.allowEditingTicket();
        issueDetailsPage.skipAllActionsOnCheckList();
        ticketOverviewTab.changeIssueStatus(STATUS_RESOLVED);
        ticketOverviewTab.fillReasonChange(webDriverWait, driver);
        issueDetailsPage.skipAllActionsOnCheckList();
        ticketOverviewTab.changeIssueStatus(STATUS_CLOSED);
        ticketOverviewTab.fillReasonChange(webDriverWait, driver);

        Assert.assertEquals(ticketOverviewTab.checkTicketStatus(), STATUS_CLOSED);
    }

    @Test(priority = 8, testName = "Check Closed Tickets View", description = "Refresh, search and check if ticket is shown in the closed tickets table")
    @Description("Refresh, search and check if ticket is shown in the closed tickets table")
    public void checkClosedTicketView() {
        closedTicketsPage = new ClosedTicketsPage(driver, webDriverWait).openView(driver, BASIC_URL);
        closedTicketsPage.clickRefresh();
        closedTicketsPage.filterBy(ID_ATTRIBUTE, ticketID);
        Assert.assertEquals(closedTicketsPage.getIdForNthTicketInTable(0), ticketID);
    }
}
