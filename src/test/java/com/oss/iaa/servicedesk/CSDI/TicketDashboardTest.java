package com.oss.iaa.servicedesk.CSDI;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.iaa.servicedesk.issue.ticket.TicketDashboardPage;
import com.oss.pages.platform.NotificationWrapperPage;

import io.qameta.allure.Description;

public class TicketDashboardTest extends BaseTestCase {

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
}