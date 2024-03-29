package com.oss.iaa.servicedesk;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.comarch.oss.web.pages.NotificationWrapperPage;
import com.oss.BaseTestCase;
import com.oss.pages.iaa.servicedesk.issue.IssueDetailsPage;
import com.oss.pages.iaa.servicedesk.issue.ticket.ClosedTicketsPage;
import com.oss.pages.iaa.servicedesk.issue.ticket.TicketDashboardPage;
import com.oss.pages.iaa.servicedesk.issue.ticket.TicketOverviewTab;
import com.oss.pages.iaa.servicedesk.issue.wizard.SDWizardPage;

import io.qameta.allure.Description;

import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.DOWNLOAD_FILE;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.EXPORT_WIZARD_ID;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.ID_ATTRIBUTE;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.TROUBLE_TICKET_ISSUE_TYPE;

public class ClosedTicketsTest extends BaseTestCase {

    private TicketDashboardPage ticketDashboardPage;
    private SDWizardPage sdWizardPage;
    private IssueDetailsPage issueDetailsPage;
    private TicketOverviewTab ticketOverviewTab;
    private ClosedTicketsPage closedTicketsPage;
    private NotificationWrapperPage notificationWrapperPage;
    private String ticketID;
    private static final String STATUS_ACKNOWLEDGED = "Acknowledged";
    private static final String STATUS_IN_PROGRESS = "In Progress";
    private static final String STATUS_RESOLVED = "Resolved";
    private static final String STATUS_CLOSED = "Closed";

    @BeforeClass
    public void goToTicketDashboard() {
        ticketDashboardPage = new TicketDashboardPage(driver, webDriverWait).goToPage(driver, BASIC_URL);
    }

    @Parameters({"MOIdentifier", "ttAssignee"})
    @Test(priority = 1, testName = "Close Ticket", description = "Create ticket and go through all necessary steps to close it")
    @Description("Create ticket and go through all necessary steps to close it")
    public void createAndCloseTicket(
            @Optional("CFS_Access_Product_Selenium_1") String MOIdentifier,
            @Optional("Tier 2 Mobile") String ttAssignee
    ) {
        sdWizardPage = ticketDashboardPage.openCreateTicketWizard("CTT");
        sdWizardPage.createTicket(MOIdentifier, ttAssignee);
        ticketID = ticketDashboardPage.getIdFromMessage();
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        ticketOverviewTab = (TicketOverviewTab) issueDetailsPage.selectOverviewTab(TROUBLE_TICKET_ISSUE_TYPE);
        ticketOverviewTab.allowEditingTicket();
        issueDetailsPage.skipAllActionsOnCheckList();
        ticketOverviewTab.changeTicketStatus(STATUS_ACKNOWLEDGED);
        issueDetailsPage.skipAllActionsOnCheckList();
        ticketOverviewTab.changeTicketStatus(STATUS_IN_PROGRESS);
        issueDetailsPage.skipAllActionsOnCheckList();
        ticketOverviewTab.changeTicketStatus(STATUS_RESOLVED);
        issueDetailsPage.skipAllActionsOnCheckList();
        ticketOverviewTab.changeTicketStatus(STATUS_CLOSED);
        Assert.assertEquals(ticketOverviewTab.checkTicketStatus(), STATUS_CLOSED);
    }

    @Test(priority = 2, testName = "Check Closed Tickets View", description = "Refresh, search and check if ticket is shown in the closed tickets table")
    @Description("Refresh, search and check if ticket is shown in the closed tickets table")
    public void checkClosedTicketView() {
        closedTicketsPage = new ClosedTicketsPage(driver, webDriverWait).openView(driver, BASIC_URL);
        closedTicketsPage.clickRefresh();
        closedTicketsPage.filterBy(ID_ATTRIBUTE, ticketID);
        Assert.assertEquals(closedTicketsPage.getIdForNthTicketInTable(0), ticketID);
    }

    @Test(priority = 3, testName = "Export Closed Ticket", description = "Export Closed Ticket")
    @Description("Export Closed Ticket")
    public void ExportClosedTicket() {
        closedTicketsPage = new ClosedTicketsPage(driver, webDriverWait).openView(driver, BASIC_URL);
        try {
            closedTicketsPage.exportFromSearchViewTable(EXPORT_WIZARD_ID);
        } catch (RuntimeException e) {
            Assert.fail(e.getMessage());
        }
        notificationWrapperPage = new NotificationWrapperPage(driver);

        Assert.assertEquals(notificationWrapperPage.amountOfNotifications(), 0);
        Assert.assertTrue(closedTicketsPage.checkIfFileIsNotEmpty(DOWNLOAD_FILE));
    }
}
