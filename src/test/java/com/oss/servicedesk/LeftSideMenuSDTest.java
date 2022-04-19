package com.oss.servicedesk;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.servicedesk.ServiceDeskMenuPage;
import com.oss.pages.servicedesk.issue.BaseDashboardPage;
import com.oss.pages.servicedesk.issue.problem.MyProblemsPage;
import com.oss.pages.servicedesk.issue.problem.ProblemSearchPage;
import com.oss.pages.servicedesk.issue.task.TaskDashboardPage;
import com.oss.pages.servicedesk.issue.ticket.MyTicketsPage;
import com.oss.pages.servicedesk.issue.ticket.TicketSearchPage;

import io.qameta.allure.Description;

public class LeftSideMenuSDTest extends BaseTestCase {

    private ServiceDeskMenuPage serviceDeskMenuPage;
    private BaseDashboardPage baseDashboardPage;
    private TicketSearchPage ticketSearchPage;
    private MyTicketsPage myTicketsPage;
    private ProblemSearchPage problemSearchPage;
    private MyProblemsPage myProblemsPage;
    private TaskDashboardPage taskDashboardPage;
    private static final String TROUBLE_TICKET_DASHBOARD = "_TroubleTickets";
    private static final String PROBLEMS_DASHBOARD = "_ProblemManagement";
    private static final String DASHBOARD_PAGE = "Dashboard";
    private static final String TICKETS_SEARCH_PAGE = "Tickets Search";
    private static final String MY_TICKETS_PAGE = "My Tickets";
    private static final String MY_GROUP_TICKETS_PAGE = "My Group Tickets";
    private static final String PROBLEMS_SEARCH_PAGE = "Problems Search";
    private static final String MY_PROBLEMS_PAGE = "My Problems";
    private static final String MY_GROUP_PROBLEMS_PAGE = "My Group Problems";
    private static final String TASKS_DASHBOARD_PAGE = "Tasks";

    @BeforeMethod
    public void openMainPage() {
        serviceDeskMenuPage = new ServiceDeskMenuPage(driver, webDriverWait).openMainPage(BASIC_URL);
    }

    @Test(testName = "Go to TT Dashboard", description = "Going to TT Dashboard from Left Side Menu")
    @Description("Going to TT Dashboard from Left Side Menu")
    public void goToTTDashboard() {
        serviceDeskMenuPage.chooseFromTroubleTicketsMenu(DASHBOARD_PAGE);
        baseDashboardPage = new BaseDashboardPage(driver, webDriverWait);
        Assert.assertTrue(baseDashboardPage.isDashboardOpen(BASIC_URL, TROUBLE_TICKET_DASHBOARD));
    }

    @Test(testName = "Go to Ticket Search", description = "Going to Ticket Search from Left Side Menu")
    @Description("Going to Ticket Search from Left Side Menu")
    public void goToTicketSearch() {
        serviceDeskMenuPage.chooseFromTroubleTicketsMenu(TICKETS_SEARCH_PAGE);
        ticketSearchPage = new TicketSearchPage(driver, webDriverWait);
        Assert.assertTrue(ticketSearchPage.isSearchPageOpened(BASIC_URL, "ticket-search"));
        Assert.assertEquals(ticketSearchPage.getViewTitle(), TICKETS_SEARCH_PAGE);
    }

    @Test(testName = "Go to My Tickets", description = "Going to My Tickets from Left Side Menu")
    @Description("Going to My Tickets from Left Side Menu")
    public void goToMyTickets() {
        serviceDeskMenuPage.chooseFromTroubleTicketsMenu(MY_TICKETS_PAGE);
        myTicketsPage = new MyTicketsPage(driver, webDriverWait);
        Assert.assertTrue(myTicketsPage.isMyTicketsPageOpened(BASIC_URL, MY_TICKETS_PAGE));
        Assert.assertEquals(myTicketsPage.getViewTitle(), MY_TICKETS_PAGE);
    }

    @Test(testName = "Go to My Group Tickets", description = "Going to My Group Tickets from Left Side Menu")
    @Description("Going to My Group Tickets from Left Side Menu")
    public void goToMyGroupTickets() {
        serviceDeskMenuPage.chooseFromTroubleTicketsMenu(MY_GROUP_TICKETS_PAGE);
        myTicketsPage = new MyTicketsPage(driver, webDriverWait);
        Assert.assertTrue(myTicketsPage.isMyTicketsPageOpened(BASIC_URL, MY_GROUP_TICKETS_PAGE));
        Assert.assertEquals(myTicketsPage.getViewTitle(), MY_GROUP_TICKETS_PAGE);
    }

    @Test(testName = "Go to Problem Dashboard", description = "Going to Problem Dashboard from Left Side Menu")
    @Description("Going to Problem Dashboard from Left Side Menu")
    public void goToProblemDashboard() {
        serviceDeskMenuPage.chooseFromProblemManagementMenu(DASHBOARD_PAGE);
        baseDashboardPage = new BaseDashboardPage(driver, webDriverWait);
        Assert.assertTrue(baseDashboardPage.isDashboardOpen(BASIC_URL, PROBLEMS_DASHBOARD));
    }

    @Test(testName = "Go to Problem Search", description = "Going to Problem Search from Left Side Menu")
    @Description("Going to Problem Search from Left Side Menu")
    public void goToProblemSearch() {
        serviceDeskMenuPage.chooseFromProblemManagementMenu(PROBLEMS_SEARCH_PAGE);
        problemSearchPage = new ProblemSearchPage(driver, webDriverWait);
        Assert.assertTrue(problemSearchPage.isSearchPageOpened(BASIC_URL, "problem-search"));
        Assert.assertEquals(problemSearchPage.getViewTitle(), PROBLEMS_SEARCH_PAGE);
    }

    @Test(testName = "Go to My Problems", description = "Going to My Problems from Left Side Menu")
    @Description("Going to My Problems from Left Side Menu")
    public void goToMyProblems() {
        serviceDeskMenuPage.chooseFromProblemManagementMenu(MY_PROBLEMS_PAGE);
        myProblemsPage = new MyProblemsPage(driver, webDriverWait);
        Assert.assertTrue(myProblemsPage.isMyProblemPageOpened(BASIC_URL, MY_PROBLEMS_PAGE));
        Assert.assertEquals(myProblemsPage.getViewTitle(), MY_PROBLEMS_PAGE);
    }

    @Test(testName = "Go to My Group Problems", description = "Going to My Group Problems from Left Side Menu")
    @Description("Going to My Group Problems from Left Side Menu")
    public void goToMyGroupProblems() {
        serviceDeskMenuPage.chooseFromProblemManagementMenu(MY_GROUP_PROBLEMS_PAGE);
        myProblemsPage = new MyProblemsPage(driver, webDriverWait);
        Assert.assertTrue(myProblemsPage.isMyProblemPageOpened(BASIC_URL, MY_GROUP_PROBLEMS_PAGE));
        Assert.assertEquals(myProblemsPage.getViewTitle(), MY_GROUP_PROBLEMS_PAGE);
    }

    @Test(testName = "Go to Tasks dashboard", description = "Going to Tasks dashboard from Left Side Menu")
    @Description("Going to Tasks dashboard from Left Side Menu")
    public void goToTasksDashboard() {
        serviceDeskMenuPage.chooseFromProblemManagementMenu(TASKS_DASHBOARD_PAGE);
        taskDashboardPage = new TaskDashboardPage(driver, webDriverWait);
        Assert.assertTrue(taskDashboardPage.isTaskDashboardOpened(BASIC_URL));
        Assert.assertEquals(taskDashboardPage.getViewTitle(), TASKS_DASHBOARD_PAGE);
    }
}