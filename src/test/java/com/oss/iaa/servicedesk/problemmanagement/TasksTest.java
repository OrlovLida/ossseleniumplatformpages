package com.oss.iaa.servicedesk.problemmanagement;

import java.time.LocalDateTime;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.comarch.oss.web.pages.NotificationWrapperPage;
import com.oss.BaseTestCase;
import com.oss.pages.iaa.servicedesk.issue.problem.MyGroupProblemsPage;
import com.oss.pages.iaa.servicedesk.issue.task.MyTasksPage;
import com.oss.pages.iaa.servicedesk.issue.task.TaskDashboardPage;
import com.oss.pages.iaa.servicedesk.issue.wizard.SDWizardPage;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.ISSUE_OUT_ASSIGNEE_ATTR;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.TASK_LABEL;

@Listeners({TestListener.class})
public class TasksTest extends BaseTestCase {

    private static final String TASK_WIZARD_PARENT_PROBLEM = "input_parentProblem";
    private static final String TASK_NAME = "Task " + LocalDateTime.now();
    private static final String TASK_ASSIGNEE = "sd_seleniumtest";
    private static final String PROBLEM_OUT_PREFIX = "problemOut";
    private static final String NAME_ATTR = "name";
    private static final String MY_TASKS_ASSIGNEE_ATTRIBUTE = PROBLEM_OUT_PREFIX + "." + ISSUE_OUT_ASSIGNEE_ATTR;
    private static final String MY_TASKS_NAME_ATTRIBUTE = PROBLEM_OUT_PREFIX + "." + NAME_ATTR;
    private static final String TASKS_DOWNLOAD_FILE = "Problem*.xlsx";
    private static final String TASK_WIZARD_NAME = "name";
    private static final String TASK_WIZARD_ASSIGNEE = "assignee";
    private static final String TASK_WIZARD_LABEL = "label";
    private static final String TASK_WIZARD_CREATE_TASK_BUTTON = "SaveButtonId";
    private TaskDashboardPage taskDashboardPage;
    private MyTasksPage myTasksPage;
    private NotificationWrapperPage notificationWrapperPage;
    private SDWizardPage sdWizardPage;
    private String taskID;
    private MyGroupProblemsPage myGroupProblemsPage;

    @BeforeMethod
    public void goToTaskDashboardPage() {
        taskDashboardPage = TaskDashboardPage.goToPage(driver, BASIC_URL);
    }

    @Parameters({"taskAssignee"})
    @Test(priority = 1, testName = "Create Task", description = "Create Task")
    @Description("Create Task")
    public void createTask(
            @Optional("sd_seleniumtest") String taskAssignee
    ) {
        myGroupProblemsPage = new MyGroupProblemsPage(driver, webDriverWait).openView(driver, BASIC_URL);
        myGroupProblemsPage.searchFullText(TASK_ASSIGNEE);
        String parentProblem = myGroupProblemsPage.getIdForNthTicketInTable(0);
        taskDashboardPage = TaskDashboardPage.goToPage(driver, BASIC_URL);
        sdWizardPage = taskDashboardPage.openCreateTaskWizard();
        sdWizardPage.insertValueContainsToComponent(parentProblem, TASK_WIZARD_PARENT_PROBLEM);
        sdWizardPage.insertValueContainsToComponent(TASK_NAME, TASK_WIZARD_NAME);
        sdWizardPage.insertValueContainsToComponent(TASK_ASSIGNEE, TASK_WIZARD_ASSIGNEE);
        sdWizardPage.insertValueContainsToComponent(TASK_LABEL, TASK_WIZARD_LABEL);
        sdWizardPage.clickButton(TASK_WIZARD_CREATE_TASK_BUTTON);

        taskID = taskDashboardPage.getIDForTaskWithName(TASK_NAME);

        Assert.assertEquals(taskDashboardPage.getAssigneeForNthTaskInTasksTable(0), taskAssignee);
    }

    @Test(priority = 2, testName = "My Tasks view", description = "My Tasks view")
    @Description("My Tasks view")
    public void myTasks() {
        myTasksPage = new MyTasksPage(driver, webDriverWait);
        myTasksPage.openView(driver, BASIC_URL);
        myTasksPage.filterBy(MY_TASKS_NAME_ATTRIBUTE, TASK_NAME);
        myTasksPage.filterBy(MY_TASKS_ASSIGNEE_ATTRIBUTE, TASK_ASSIGNEE);

        Assert.assertEquals(myTasksPage.countIssuesInTable(), 1);
        Assert.assertEquals(myTasksPage.getIssueID(0), taskID);
    }

    @Test(priority = 3, testName = "Tasks Export", description = "Export Tasks from Task Dashboard")
    @Description("Export Tasks from Task Dashboard")
    public void exportTasks() {
        taskDashboardPage.exportFromDashboard(TASKS_DOWNLOAD_FILE);
        notificationWrapperPage = taskDashboardPage.openNotificationPanel();

        Assert.assertEquals(notificationWrapperPage.amountOfNotifications(), 0);
        Assert.assertTrue(taskDashboardPage.checkIfFileIsNotEmpty(TASKS_DOWNLOAD_FILE));
    }
}
