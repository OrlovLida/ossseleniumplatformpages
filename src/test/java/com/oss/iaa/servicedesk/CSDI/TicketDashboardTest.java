package com.oss.iaa.servicedesk.CSDI;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.iaa.servicedesk.issue.ticket.TicketDashboardPage;
import com.oss.pages.platform.NotificationWrapperPage;

import io.qameta.allure.Description;

public class TicketDashboardTest extends BaseTestCase {

    private static final String TT_DOWNLOAD_FILE = "TroubleTicket*.xlsx";
    private NotificationWrapperPage notificationWrapperPage;
    private TicketDashboardPage ticketDashboardPage;

    @BeforeMethod
    public void goToTicketDashboardPage() {
        ticketDashboardPage = new TicketDashboardPage(driver, webDriverWait).goToPage(driver, BASIC_URL);
    }

    @Test(priority = 1, testName = "Check Trouble Tickets Table", description = "Check Trouble Tickets Table")
    @Description("Check Trouble Tickets Table")
    public void checkTroubleTicketsTable() {
        Assert.assertTrue(ticketDashboardPage.checkIfTableExists());
    }

    @Test(priority = 2, testName = "Export from Ticket Dashboard", description = "Export from Ticket Dashboard")
    @Description("Export from Ticket Dashboard")
    public void exportFromTicketDashboard() {
        ticketDashboardPage.exportFromDashboard(TT_DOWNLOAD_FILE);
        notificationWrapperPage = ticketDashboardPage.openNotificationPanel();

        Assert.assertEquals(notificationWrapperPage.amountOfNotifications(), 1);
        Assert.assertTrue(ticketDashboardPage.checkIfFileIsNotEmpty(TT_DOWNLOAD_FILE));
    }
}