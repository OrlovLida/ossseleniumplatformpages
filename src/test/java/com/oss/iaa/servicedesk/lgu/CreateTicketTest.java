package com.oss.iaa.servicedesk.lgu;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.iaa.servicedesk.issue.IssueDetailsPage;
import com.oss.pages.iaa.servicedesk.issue.ticket.ClosedTicketsPage;
import com.oss.pages.iaa.servicedesk.issue.ticket.MyGroupTicketsPage;
import com.oss.pages.iaa.servicedesk.issue.ticket.MyTicketsPage;
import com.oss.pages.iaa.servicedesk.issue.ticket.TicketDashboardPage;
import com.oss.pages.iaa.servicedesk.issue.ticket.TicketOverviewTab;
import com.oss.pages.iaa.servicedesk.issue.wizard.SDWizardPage;

import io.qameta.allure.Description;

import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.ID_ATTRIBUTE;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.TROUBLE_TICKET_ISSUE_TYPE;

public class CreateTicketTest extends BaseTestCase {

    private static final String SEVERITY_ID = "TT_WIZARD_INPUT_SEVERITY_LABEL";
    private static final String SEVERITY = "Major";
    private static final String FLOW_TYPE = "NTT_Manual";
    private static final String SEVERITY_ATTRIBUTE_NAME = "Severity";
    private static final String TT_WIZARD_ASSIGNEE = "TT_WIZARD_INPUT_ASSIGNEE_LABEL";
    private static final String INCIDENT_DESCRIPTION = "test selenium description";
    private static final String STATUS_CLOSED = "Closed";


    private TicketDashboardPage ticketDashboardPage;
    private SDWizardPage sdWizardPage;
    private MyTicketsPage myTicketsPage;
    private MyGroupTicketsPage myGroupTicketsPage;
    private IssueDetailsPage issueDetailsPage;
    private TicketOverviewTab ticketOverviewTab;
    private ClosedTicketsPage closedTicketsPage;
    private String ticketID;

    @BeforeMethod
    public void goToTicketDashboardPage() {
        ticketDashboardPage = new TicketDashboardPage(driver, webDriverWait).goToPage(driver, BASIC_URL);
    }

    @Parameters({"MOIdentifier", "ttAssignee"})
    @Test(priority = 1, testName = "Create NTT Manual Ticket", description = "Create NTT Manual Ticket")
    @Description("Create NTT Manual Ticket")
    public void createTicket(
            @Optional("TEST_MO") String MOIdentifier,
            @Optional("sd_seleniumtest") String ttAssignee
    ) {
        sdWizardPage = ticketDashboardPage.openCreateTicketWizard(FLOW_TYPE);
        sdWizardPage.getMoStep().enterTextIntoMOIdentifierField(MOIdentifier);
        sdWizardPage.getMoStep().selectObjectInMOTable(MOIdentifier);
        sdWizardPage.clickNextButtonInWizard();
        sdWizardPage.insertValueToComponent(SEVERITY, SEVERITY_ID);
        sdWizardPage.enterDescription(INCIDENT_DESCRIPTION);
        sdWizardPage.insertValueToComponent(ttAssignee, TT_WIZARD_ASSIGNEE);
        sdWizardPage.clickNextButtonInWizard();
        sdWizardPage.clickAcceptButtonInWizard();
        ticketID = ticketDashboardPage.getIdFromMessage();
        Assert.assertTrue(ticketDashboardPage.getAttributeFromTable(0, SEVERITY_ATTRIBUTE_NAME).contains(SEVERITY));
        Assert.assertEquals(ticketDashboardPage.getRowForIssueWithID(ticketID), 0);
    }

    @Test(priority = 2, testName = "Check My Tickets View", description = "Check My Tickets View")
    @Description("Check My Tickets View")
    public void checkMyTicketsView() {
        myTicketsPage = new MyTicketsPage(driver, webDriverWait).openView(driver,BASIC_URL);
        myTicketsPage.filterBy(ID_ATTRIBUTE, ticketID);
        Assert.assertFalse(myTicketsPage.isIssueTableEmpty());
        Assert.assertEquals(myTicketsPage.getIdForNthTicketInTable(0), ticketID);
    }

    @Test(priority = 3, testName = "Check My Group Tickets View", description = "Check My Group Tickets View")
    @Description("Check My Group Tickets View")
    public void checkMyGroupTicketsView() {
        myGroupTicketsPage = new MyGroupTicketsPage(driver, webDriverWait).openView(driver,BASIC_URL);
        DelayUtils.sleep(10000); //TODO delete when enviroment will be stable
        myGroupTicketsPage.filterBy(ID_ATTRIBUTE, ticketID);
        Assert.assertFalse(myGroupTicketsPage.isIssueTableEmpty());
        Assert.assertEquals(myGroupTicketsPage.getIdForNthTicketInTable(0), ticketID);
    }

    @Test(priority = 4, testName = "Close Ticket", description = "Open ticket and go through all necessary steps to close it")
    @Description("Open ticket and go through all necessary steps to close it")
    public void closeTicket(
    ) {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        ticketOverviewTab = (TicketOverviewTab) issueDetailsPage.selectOverviewTab(TROUBLE_TICKET_ISSUE_TYPE);
        ticketOverviewTab.allowEditingTicket();
        issueDetailsPage.skipAllActionsOnCheckList();
        DelayUtils.sleep(10000); //TODO delete when enviroment will be stable
        ticketOverviewTab.changeIssueStatus(STATUS_CLOSED);
        ticketOverviewTab.releaseTicket();

        Assert.assertEquals(ticketOverviewTab.checkTicketStatus(), STATUS_CLOSED);
    }

    @Test(priority = 5, testName = "Check Closed Tickets View", description = "Refresh, search and check if ticket is shown in the closed tickets table")
    @Description("Refresh, search and check if ticket is shown in the closed tickets table")
    public void checkClosedTicketView() {
        closedTicketsPage = new ClosedTicketsPage(driver, webDriverWait).openView(driver, BASIC_URL);
        closedTicketsPage.clickRefresh();
        closedTicketsPage.filterBy(ID_ATTRIBUTE, ticketID);
        Assert.assertEquals(closedTicketsPage.getIdForNthTicketInTable(0), ticketID);
    }
}