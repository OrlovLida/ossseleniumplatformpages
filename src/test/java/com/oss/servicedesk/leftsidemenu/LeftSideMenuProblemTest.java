package com.oss.servicedesk.leftsidemenu;

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

import io.qameta.allure.Description;

import static com.oss.pages.servicedesk.ServiceDeskConstants.PROBLEMS_DASHBOARD;

public class LeftSideMenuProblemTest extends BaseTestCase {

    private ServiceDeskMenuPage serviceDeskMenuPage;
    private ProblemDashboardPage problemDashboardPage;
    private ProblemSearchPage problemSearchPage;
    private MyProblemsPage myProblemsPage;
    private MyGroupProblemsPage myGroupProblemsPage;
    private ClosedProblemsPage closedProblemsPage;
    private TaskDashboardPage taskDashboardPage;
    private MyTasksPage myTasksPage;

    private static final String DASHBOARD_PAGE = "Dashboard";
    private static final String PROBLEMS_SEARCH_PAGE = "Problems Search";
    private static final String MY_PROBLEMS_PAGE = "My Problems";
    private static final String MY_GROUP_PROBLEMS_PAGE = "My Group Problems";
    private static final String TASKS_DASHBOARD_PAGE = "Tasks";
    private static final String CLOSED_PROBLEMS_PAGE = "Closed Problems";
    private static final String MY_TASKS_PAGE = "My Tasks";

    @BeforeMethod
    public void openMainPage() {
        serviceDeskMenuPage = new ServiceDeskMenuPage(driver, webDriverWait).openMainPage(BASIC_URL);
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

    @Test(testName = "Go to Closed Problems", description = "Going to Closed Problems from Left Side Menu")
    @Description("Going to Closed Problems from Left Side Menu")
    public void goToClosedProblems() {
        serviceDeskMenuPage.chooseFromProblemManagementMenu(CLOSED_PROBLEMS_PAGE);
        closedProblemsPage = new ClosedProblemsPage(driver, webDriverWait);
        Assert.assertTrue(closedProblemsPage.isPageOpened(BASIC_URL));
        Assert.assertEquals(closedProblemsPage.getViewTitle(), CLOSED_PROBLEMS_PAGE);
    }

    @Test(testName = "Go to Tasks dashboard", description = "Going to Tasks dashboard from Left Side Menu")
    @Description("Going to Tasks dashboard from Left Side Menu")
    public void goToTasksDashboard() {
        serviceDeskMenuPage.chooseFromProblemManagementMenu(TASKS_DASHBOARD_PAGE);
        taskDashboardPage = new TaskDashboardPage(driver, webDriverWait);
        Assert.assertTrue(taskDashboardPage.isTaskDashboardOpened(BASIC_URL));
        Assert.assertEquals(taskDashboardPage.getViewTitle(), TASKS_DASHBOARD_PAGE);
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

