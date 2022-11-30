package com.oss.iaa.servicedesk.DTAG;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.iaa.servicedesk.infomanagement.MessagesPage;
import com.oss.pages.iaa.servicedesk.issue.ticket.ClosedTicketsPage;
import com.oss.pages.iaa.servicedesk.issue.ticket.MyGroupTicketsPage;
import com.oss.pages.iaa.servicedesk.issue.ticket.TicketDashboardPage;
import com.oss.pages.iaa.servicedesk.issue.ticket.TicketSearchPage;

import io.qameta.allure.Description;

public class SDDashboardsTest extends BaseTestCase {

    private TicketSearchPage ticketSearchPage;
    private MessagesPage messagesPage;
    private MyGroupTicketsPage myGroupTicketsPage;
    private ClosedTicketsPage closedTicketsPage;

    @BeforeMethod
    public void goToTicketDashboardPage() {
       new TicketDashboardPage(driver, webDriverWait).goToPage(driver, BASIC_URL);
    }

    @Test(priority = 1, testName = "Check Ticket Search Dashboard", description = "Check Ticket Search Dashboard")
    @Description("Check Ticket Search Dashboard")
    public void checkTicketSearchDashboard() {
        ticketSearchPage = new TicketSearchPage(driver, webDriverWait).openView(driver, BASIC_URL);
        Assert.assertTrue(ticketSearchPage.checkTicketsTableVisibility());
    }

    @Test(priority = 2, testName = "Check My Group Tickets Dashboard", description = "Check My Group Tickets Dashboard")
    @Description("Check My Group Tickets Dashboard")
    public void checkMyGroupTicketsDashboard() {
        myGroupTicketsPage = new MyGroupTicketsPage(driver, webDriverWait).openView(driver, BASIC_URL);
        Assert.assertTrue(myGroupTicketsPage.checkTicketsTableVisibility());
    }

    @Test(priority = 3, testName = "Check Closed Tickets Dashboard", description = "Check Closed Tickets Dashboard")
    @Description("Check Closed Tickets Dashboard")
    public void checkClosedTicketsDashboard() {
        closedTicketsPage = new ClosedTicketsPage(driver, webDriverWait).openView(driver, BASIC_URL);
        Assert.assertTrue(closedTicketsPage.checkTicketsTableVisibility());
    }

    @Test(priority = 4, testName = "Check Messages Dashboard", description = "Check Messages Dashboard")
    @Description("Check Messages Dashboard")
    public void checkMessagesDashboard() {
        messagesPage = new MessagesPage(driver, webDriverWait).openView(driver, BASIC_URL);
        Assert.assertTrue(messagesPage.checkTicketsTableVisibility());
        messagesPage.selectFirstMessage();
        messagesPage.getNotificationPreview().clickReply();
        Assert.assertTrue(messagesPage.isNotificationWizardVisible());
        messagesPage.getNotificationWizard().clickCancel();
    }
}