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
import com.oss.pages.servicedesk.issue.ticket.MyTicketsPage;
import com.oss.pages.servicedesk.issue.ticket.TicketDashboardPage;
import com.oss.pages.servicedesk.issue.wizard.SDWizardPage;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

import static com.oss.pages.servicedesk.ServiceDeskConstants.ID_ATTRIBUTE;
import static com.oss.pages.servicedesk.ServiceDeskConstants.TROUBLE_TICKET_ISSUE_TYPE;

@Listeners({TestListener.class})
public class MyGroupTicketsVFNZTest extends BaseTestCase {
    private TicketDashboardPage ticketDashboardPage;
    private MyTicketsPage myTicketsPage;
    private NotificationWrapperPage notificationWrapperPage;
    private IssueDetailsPage issueDetailsPage;
    private SDWizardPage sdWizardPage;
    private String ticketID;

    private static final String DOWNLOAD_FILE = "Selenium test*.csv";
    private static final String EXPORT_WIZARD_ID = "exportgui-mainview";
    private static final String TT_WIZARD_ASSIGNEE = "TT_WIZARD_INPUT_ASSIGNEE_LABEL";
    private static final String DETAILS_WINDOW_ID = "_detailsWindow";
    private static final String SELENIUM_TEST_USER = "sd_seleniumtest";

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
        myTicketsPage = new MyTicketsPage(driver, webDriverWait).goToMyGroupTickets(driver, BASIC_URL);
        myTicketsPage.filterByTextField(ID_ATTRIBUTE, ticketID);
        Assert.assertFalse(myTicketsPage.isIssueTableEmpty());
        Assert.assertEquals(ticketID, myTicketsPage.getIdForNthTicketInTable(0));
    }

    @Test(priority = 3, testName = "Refresh MyGroup Tickets", description = "Refresh MyGroup Tickets")
    @Description("Refresh MyGroup Tickets")
    public void refreshMyGroupTickets() {
        myTicketsPage = new MyTicketsPage(driver, webDriverWait).goToMyGroupTickets(driver, BASIC_URL);
        if (!myTicketsPage.isIssueTableEmpty()) {
            int ticketsInTable = myTicketsPage.countIssuesInTable();
            myTicketsPage.clickRefresh();
            int ticketsAfterRefresh = myTicketsPage.countIssuesInTable();

            Assert.assertFalse(myTicketsPage.isIssueTableEmpty());
            Assert.assertTrue(ticketsInTable >= ticketsAfterRefresh);
        } else {
            Assert.fail("No data in table - cannot check refresh function");
        }
    }

    @Test(priority = 4, testName = "Export from My Group Tickets", description = "Export from My Group Tickets")
    @Description("Export from My Group Tickets")
    public void exportFromMyGroupTickets() {
        myTicketsPage = new MyTicketsPage(driver, webDriverWait).goToMyGroupTickets(driver, BASIC_URL);
        try {
            myTicketsPage.exportFromSearchViewTable(EXPORT_WIZARD_ID);
        } catch (RuntimeException e) {
            Assert.fail(e.getMessage());
        }
        notificationWrapperPage = new NotificationWrapperPage(driver);

        Assert.assertEquals(notificationWrapperPage.amountOfNotifications(), 0);
        Assert.assertTrue(myTicketsPage.checkIfFileIsNotEmpty(DOWNLOAD_FILE));
    }

    @Test(priority = 5, testName = "My Tickets Check", description = "Change assignee nad check if ticket is visible in My Ticket View")
    @Description("Change assignee nad check if ticket is visible in My Ticket View")
    public void myTicketsCheck() {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        issueDetailsPage.allowEditingTicket();
        issueDetailsPage.maximizeWindow(DETAILS_WINDOW_ID);
        sdWizardPage = issueDetailsPage.openEditTicketWizard();
        sdWizardPage.clickNextButtonInWizard();
        sdWizardPage.insertValueToSearchComponent(SELENIUM_TEST_USER, TT_WIZARD_ASSIGNEE);
        sdWizardPage.clickNextButtonInWizard();
        sdWizardPage.clickAcceptButtonInWizard();
        myTicketsPage = new MyTicketsPage(driver, webDriverWait).goToMyTickets(driver, BASIC_URL);
        Assert.assertFalse(myTicketsPage.isIssueTableEmpty());

        myTicketsPage.filterByTextField(ID_ATTRIBUTE, ticketID);
        Assert.assertFalse(myTicketsPage.isIssueTableEmpty());
    }
}
