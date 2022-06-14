package com.oss.servicedesk;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.platform.NotificationWrapperPage;
import com.oss.pages.servicedesk.issue.IssueDetailsPage;
import com.oss.pages.servicedesk.issue.ticket.MyGroupTicketsPage;
import com.oss.pages.servicedesk.issue.ticket.MyTicketsPage;
import com.oss.pages.servicedesk.issue.ticket.TicketDashboardPage;
import com.oss.pages.servicedesk.issue.ticket.TicketOverviewTab;
import com.oss.pages.servicedesk.issue.wizard.SDWizardPage;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

import static com.oss.pages.servicedesk.ServiceDeskConstants.DETAILS_TABS_CONTAINER_ID;
import static com.oss.pages.servicedesk.ServiceDeskConstants.DOWNLOAD_FILE;
import static com.oss.pages.servicedesk.ServiceDeskConstants.EXPORT_WIZARD_ID;
import static com.oss.pages.servicedesk.ServiceDeskConstants.ID_ATTRIBUTE;
import static com.oss.pages.servicedesk.ServiceDeskConstants.TROUBLE_TICKET_ISSUE_TYPE;
import static com.oss.pages.servicedesk.ServiceDeskConstants.USER_NAME;

@Listeners({TestListener.class})
public class MyGroupTicketsTest extends BaseTestCase {
    private TicketDashboardPage ticketDashboardPage;
    private MyTicketsPage myTicketsPage;
    private MyGroupTicketsPage myGroupTicketsPage;
    private NotificationWrapperPage notificationWrapperPage;
    private IssueDetailsPage issueDetailsPage;
    private TicketOverviewTab ticketOverviewTab;
    private SDWizardPage sdWizardPage;
    private String ticketID;

    private static final String TT_WIZARD_ASSIGNEE = "TT_WIZARD_INPUT_ASSIGNEE_LABEL";

    @BeforeMethod
    public void goToTicketDashboardPage() {
        ticketDashboardPage = new TicketDashboardPage(driver, webDriverWait).goToPage(driver, BASIC_URL);
    }

    @Parameters({"MOIdentifier", "ttAssignee"})
    @Test(priority = 1, testName = "Create CTT Ticket", description = "Create CTT Ticket")
    @Description("Create CTT Ticket")
    public void createCTTTicket(
            @Optional("CFS_Access_Product_Selenium_1") String MOIdentifier,
            @Optional("Tier 2 Mobile") String ttAssignee
    ) {
        sdWizardPage = ticketDashboardPage.openCreateTicketWizard("CTT");
        sdWizardPage.createTicket(MOIdentifier, ttAssignee);
        ticketID = ticketDashboardPage.getIdFromMessage();
        Assert.assertTrue(ticketDashboardPage.getAssigneeForNthTicketInTable(0).contains(ttAssignee));
        Assert.assertEquals(ticketDashboardPage.getRowForIssueWithID(ticketID), 0);
    }

    @Test(priority = 2, testName = "Check My Group Tickets", description = "Check My Group Tickets")
    @Description("Check My Group Tickets")
    public void checkMyGroupTickets() {
        myGroupTicketsPage = new MyGroupTicketsPage(driver, webDriverWait).openView(driver, BASIC_URL);
        myGroupTicketsPage.filterBy(ID_ATTRIBUTE, ticketID);
        Assert.assertFalse(myGroupTicketsPage.isIssueTableEmpty());
        Assert.assertEquals(myGroupTicketsPage.getIdForNthTicketInTable(0), ticketID);
    }

    @Test(priority = 3, testName = "Refresh MyGroup Tickets", description = "Refresh MyGroup Tickets")
    @Description("Refresh MyGroup Tickets")
    public void refreshMyGroupTickets() {
        myGroupTicketsPage = new MyGroupTicketsPage(driver, webDriverWait).openView(driver, BASIC_URL);
        if (!myGroupTicketsPage.isIssueTableEmpty()) {
            int ticketsInTable = myGroupTicketsPage.countIssuesInTable();
            myGroupTicketsPage.clickRefresh();
            int ticketsAfterRefresh = myGroupTicketsPage.countIssuesInTable();

            Assert.assertFalse(myGroupTicketsPage.isIssueTableEmpty());
            Assert.assertTrue(ticketsInTable <= ticketsAfterRefresh);
        } else {
            Assert.fail("No data in table - cannot check refresh function");
        }
    }

    @Test(priority = 4, testName = "Export from My Group Tickets", description = "Export from My Group Tickets")
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

    @Test(priority = 5, testName = "My Tickets Check", description = "Change assignee and check if ticket is visible in My Ticket View")
    @Description("Change assignee and check if ticket is visible in My Ticket View")
    public void myTicketsCheck() {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        ticketOverviewTab = (TicketOverviewTab) issueDetailsPage.selectOverviewTab(TROUBLE_TICKET_ISSUE_TYPE);
        ticketOverviewTab.allowEditingTicket();
        issueDetailsPage.maximizeWindow(DETAILS_TABS_CONTAINER_ID);
        sdWizardPage = ticketOverviewTab.openEditIssueWizard();
        sdWizardPage.clickNextButtonInWizard();
        sdWizardPage.insertValueToComponent(USER_NAME, TT_WIZARD_ASSIGNEE);
        sdWizardPage.clickNextButtonInWizard();
        sdWizardPage.clickAcceptButtonInWizard();
        myTicketsPage = new MyTicketsPage(driver, webDriverWait).openView(driver, BASIC_URL);
        Assert.assertFalse(myTicketsPage.isIssueTableEmpty());

        myTicketsPage.filterBy(ID_ATTRIBUTE, ticketID);
        Assert.assertFalse(myTicketsPage.isIssueTableEmpty());
    }
}
