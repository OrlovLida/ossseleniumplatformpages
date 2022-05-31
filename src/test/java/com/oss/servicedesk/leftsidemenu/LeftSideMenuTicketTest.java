package com.oss.servicedesk.leftsidemenu;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.servicedesk.ServiceDeskMenuPage;
import com.oss.pages.servicedesk.issue.ticket.ClosedTicketsPage;
import com.oss.pages.servicedesk.issue.ticket.MyGroupTicketsPage;
import com.oss.pages.servicedesk.issue.ticket.MyTicketsPage;
import com.oss.pages.servicedesk.issue.ticket.TicketDashboardPage;
import com.oss.pages.servicedesk.issue.ticket.TicketSearchPage;

import io.qameta.allure.Description;

import static com.oss.pages.servicedesk.ServiceDeskConstants.TICKET_DASHBOARD;

public class LeftSideMenuTicketTest extends BaseTestCase {

    private ServiceDeskMenuPage serviceDeskMenuPage;
    private TicketDashboardPage ticketDashboardPage;
    private TicketSearchPage ticketSearchPage;
    private MyTicketsPage myTicketsPage;
    private MyGroupTicketsPage myGroupTicketsPage;
    private ClosedTicketsPage closedTicketsPage;

    private static final String DASHBOARD_PAGE = "Dashboard";
    private static final String TICKETS_SEARCH_PAGE = "Tickets Search";
    private static final String MY_TICKETS_PAGE = "My Tickets";
    private static final String MY_GROUP_TICKETS_PAGE = "My Group Tickets";
    private static final String CLOSED_TICKETS_PAGE = "Closed Tickets";

    @BeforeMethod
    public void openMainPage() {
        serviceDeskMenuPage = new ServiceDeskMenuPage(driver, webDriverWait).openMainPage(BASIC_URL);
    }

    @Test(testName = "Go to TT Dashboard", description = "Going to TT Dashboard from Left Side Menu")
    @Description("Going to TT Dashboard from Left Side Menu")
    public void goToTTDashboard() {
        serviceDeskMenuPage.chooseFromTroubleTicketsMenu(DASHBOARD_PAGE);
        ticketDashboardPage = new TicketDashboardPage(driver, webDriverWait);
        Assert.assertTrue(ticketDashboardPage.isDashboardOpen(BASIC_URL, TICKET_DASHBOARD));
    }

    @Test(testName = "Go to Ticket Search", description = "Going to Ticket Search from Left Side Menu")
    @Description("Going to Ticket Search from Left Side Menu")
    public void goToTicketSearch() {
        serviceDeskMenuPage.chooseFromTroubleTicketsMenu(TICKETS_SEARCH_PAGE);
        ticketSearchPage = new TicketSearchPage(driver, webDriverWait);
        Assert.assertTrue(ticketSearchPage.isPageOpened(BASIC_URL));
        Assert.assertEquals(ticketSearchPage.getViewTitle(), TICKETS_SEARCH_PAGE);
    }

    @Test(testName = "Go to My Tickets", description = "Going to My Tickets from Left Side Menu")
    @Description("Going to My Tickets from Left Side Menu")
    public void goToMyTickets() {
        serviceDeskMenuPage.chooseFromTroubleTicketsMenu(MY_TICKETS_PAGE);
        myTicketsPage = new MyTicketsPage(driver, webDriverWait);
        Assert.assertTrue(myTicketsPage.isPageOpened(BASIC_URL));
        Assert.assertEquals(myTicketsPage.getViewTitle(), MY_TICKETS_PAGE);
    }

    @Test(testName = "Go to My Group Tickets", description = "Going to My Group Tickets from Left Side Menu")
    @Description("Going to My Group Tickets from Left Side Menu")
    public void goToMyGroupTickets() {
        serviceDeskMenuPage.chooseFromTroubleTicketsMenu(MY_GROUP_TICKETS_PAGE);
        myGroupTicketsPage = new MyGroupTicketsPage(driver, webDriverWait);
        Assert.assertTrue(myGroupTicketsPage.isPageOpened(BASIC_URL));
        Assert.assertEquals(myGroupTicketsPage.getViewTitle(), MY_GROUP_TICKETS_PAGE);
    }

    @Test(testName = "Go to Closed Tickets", description = "Going to Closed Tickets from Left Side Menu")
    @Description("Going to Closed Tickets from Left Side Menu")
    public void goToClosedTickets() {
        serviceDeskMenuPage.chooseFromTroubleTicketsMenu(CLOSED_TICKETS_PAGE);
        closedTicketsPage = new ClosedTicketsPage(driver, webDriverWait);
        Assert.assertTrue(closedTicketsPage.isPageOpened(BASIC_URL));
        Assert.assertEquals(closedTicketsPage.getViewTitle(), CLOSED_TICKETS_PAGE);
    }
}

