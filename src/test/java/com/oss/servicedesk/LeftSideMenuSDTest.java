package com.oss.servicedesk;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.servicedesk.ServiceDeskMenuPage;
import com.oss.pages.servicedesk.issue.problem.ClosedProblemsPage;
import com.oss.pages.servicedesk.issue.problem.MyGroupProblemsPage;
import com.oss.pages.servicedesk.issue.problem.MyProblemsPage;
import com.oss.pages.servicedesk.issue.problem.ProblemDashboardPage;
import com.oss.pages.servicedesk.issue.problem.ProblemSearchPage;
import com.oss.pages.servicedesk.issue.task.MyTasksPage;
import com.oss.pages.servicedesk.issue.task.TaskDashboardPage;
import com.oss.pages.servicedesk.issue.ticket.ClosedTicketsPage;
import com.oss.pages.servicedesk.issue.ticket.MyGroupTicketsPage;
import com.oss.pages.servicedesk.issue.ticket.MyTicketsPage;
import com.oss.pages.servicedesk.issue.ticket.TicketDashboardPage;
import com.oss.pages.servicedesk.issue.ticket.TicketSearchPage;

import io.qameta.allure.Description;

import static com.oss.pages.servicedesk.ServiceDeskConstants.PROBLEMS_DASHBOARD;
import static com.oss.pages.servicedesk.ServiceDeskConstants.TICKET_DASHBOARD;

public class LeftSideMenuSDTest extends BaseTestCase {

    private ServiceDeskMenuPage serviceDeskMenuPage;
    private TicketDashboardPage ticketDashboardPage;
    private ProblemDashboardPage problemDashboardPage;
    private TicketSearchPage ticketSearchPage;
    private MyTicketsPage myTicketsPage;
    private MyGroupTicketsPage myGroupTicketsPage;
    private ClosedTicketsPage closedTicketsPage;
    private ProblemSearchPage problemSearchPage;
    private MyProblemsPage myProblemsPage;
    private MyGroupProblemsPage myGroupProblemsPage;
    private ClosedProblemsPage closedProblemsPage;
    private TaskDashboardPage taskDashboardPage;
    private MyTasksPage myTasksPage;
    private static final String DASHBOARD_PAGE = "Dashboard";
    private static final String TICKETS_SEARCH_PAGE = "Tickets Search";
    private static final String MY_TICKETS_PAGE = "My Tickets";
    private static final String MY_GROUP_TICKETS_PAGE = "My Group Tickets";
    private static final String PROBLEMS_SEARCH_PAGE = "Problems Search";
    private static final String MY_PROBLEMS_PAGE = "My Problems";
    private static final String MY_GROUP_PROBLEMS_PAGE = "My Group Problems";
    private static final String TASKS_DASHBOARD_PAGE = "Tasks";
    private static final String CLOSED_TICKETS_PAGE = "Closed Tickets";
    private static final String CLOSED_PROBLEMS_PAGE = "Closed Problems";
    private static final String MY_TASKS_PAGE = "My Tasks";

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

    @Test(testName = "Go to Problem Dashboard", description = "Going to Problem Dashboard from Left Side Menu")
    @Description("Going to Problem Dashboard from Left Side Menu")
    public void goToProblemDashboard() {
        serviceDeskMenuPage.chooseFromProblemManagementMenu(DASHBOARD_PAGE);
        problemDashboardPage = new ProblemDashboardPage(driver, webDriverWait);
        Assert.assertTrue(problemDashboardPage.isDashboardOpen(BASIC_URL, PROBLEMS_DASHBOARD));
    }

    @Test(testName = "Go to Problem Search", description = "Going to Problem Search from Left Side Menu")
    @Description("Going to Problem Search from Left Side Menu")
    public void goToProblemSearch() {
        serviceDeskMenuPage.chooseFromProblemManagementMenu(PROBLEMS_SEARCH_PAGE);
        problemSearchPage = new ProblemSearchPage(driver, webDriverWait);
        Assert.assertTrue(problemSearchPage.isPageOpened(BASIC_URL));
        Assert.assertEquals(problemSearchPage.getViewTitle(), PROBLEMS_SEARCH_PAGE);
    }

    @Test(testName = "Go to My Problems", description = "Going to My Problems from Left Side Menu")
    @Description("Going to My Problems from Left Side Menu")
    public void goToMyProblems() {
        serviceDeskMenuPage.chooseFromProblemManagementMenu(MY_PROBLEMS_PAGE);
        myProblemsPage = new MyProblemsPage(driver, webDriverWait);
        Assert.assertTrue(myProblemsPage.isPageOpened(BASIC_URL));
        Assert.assertEquals(myProblemsPage.getViewTitle(), MY_PROBLEMS_PAGE);
    }

    @Test(testName = "Go to My Group Problems", description = "Going to My Group Problems from Left Side Menu")
    @Description("Going to My Group Problems from Left Side Menu")
    public void goToMyGroupProblems() {
        serviceDeskMenuPage.chooseFromProblemManagementMenu(MY_GROUP_PROBLEMS_PAGE);
        myGroupProblemsPage = new MyGroupProblemsPage(driver, webDriverWait);
        Assert.assertTrue(myGroupProblemsPage.isPageOpened(BASIC_URL));
        Assert.assertEquals(myGroupProblemsPage.getViewTitle(), MY_GROUP_PROBLEMS_PAGE);
    }

    @Test(testName = "Go to Tasks dashboard", description = "Going to Tasks dashboard from Left Side Menu")
    @Description("Going to Tasks dashboard from Left Side Menu")
    public void goToTasksDashboard() {
        serviceDeskMenuPage.chooseFromProblemManagementMenu(TASKS_DASHBOARD_PAGE);
        taskDashboardPage = new TaskDashboardPage(driver, webDriverWait);
        Assert.assertTrue(taskDashboardPage.isTaskDashboardOpened(BASIC_URL));
        Assert.assertEquals(taskDashboardPage.getViewTitle(), TASKS_DASHBOARD_PAGE);
    }

    @Test(testName = "Go to Closed Tickets", description = "Going to Closed Tickets from Left Side Menu")
    @Description("Going to Closed Tickets from Left Side Menu")
    public void goToClosedTickets() {
        serviceDeskMenuPage.chooseFromTroubleTicketsMenu(CLOSED_TICKETS_PAGE);
        closedTicketsPage = new ClosedTicketsPage(driver, webDriverWait);
        Assert.assertTrue(closedTicketsPage.isPageOpened(BASIC_URL));
        Assert.assertEquals(closedTicketsPage.getViewTitle(), CLOSED_TICKETS_PAGE);
    }

    @Test(testName = "Go to Closed Problems", description = "Going to Closed Problems from Left Side Menu")
    @Description("Going to Closed Problems from Left Side Menu")
    public void goToClosedProblems() {
        serviceDeskMenuPage.chooseFromProblemManagementMenu(CLOSED_PROBLEMS_PAGE);
        closedProblemsPage = new ClosedProblemsPage(driver, webDriverWait);
        Assert.assertTrue(closedProblemsPage.isPageOpened(BASIC_URL));
        Assert.assertEquals(closedProblemsPage.getViewTitle(), CLOSED_PROBLEMS_PAGE);
    }

    @Test(testName = "Go to My Tasks", description = "Going to My Tasks from Left Side Menu")
    @Description("Going to My Tasks from Left Side Menu")
    public void goToMyTasks() {
        serviceDeskMenuPage.chooseFromProblemManagementMenu(MY_TASKS_PAGE);
        myTasksPage = new MyTasksPage(driver, webDriverWait);
        Assert.assertTrue(myTasksPage.isPageOpened(BASIC_URL));
        Assert.assertEquals(myTasksPage.getViewTitle(), MY_TASKS_PAGE);
    }
}

