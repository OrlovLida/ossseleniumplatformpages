package com.oss.servicedesk.problemmanagement;

import java.time.LocalDateTime;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.servicedesk.issue.IssueDetailsPage;
import com.oss.pages.servicedesk.issue.tabs.OverviewTab;
import com.oss.pages.servicedesk.issue.task.MyTasksPage;
import com.oss.pages.servicedesk.issue.task.TaskDashboardPage;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

import static com.oss.pages.servicedesk.ServiceDeskConstants.DETAILS_TABS_CONTAINER_ID;
import static com.oss.pages.servicedesk.ServiceDeskConstants.ISSUE_OUT_ASSIGNEE_ATTR;
import static com.oss.pages.servicedesk.ServiceDeskConstants.ISSUE_OUT_STATUS_ATTR;
import static com.oss.pages.servicedesk.ServiceDeskConstants.PROBLEM_ISSUE_TYPE;
import static com.oss.pages.servicedesk.ServiceDeskConstants.TASK_LABEL;

@Listeners({TestListener.class})
public class TasksTest extends BaseTestCase {

    private TaskDashboardPage taskDashboardPage;
    private IssueDetailsPage issueDetailsPage;
    private OverviewTab taskOverviewTab;
    private MyTasksPage myTasksPage;
    private com.oss.pages.servicedesk.issue.wizard.SDWizardPage SDWizardPage;
    private String taskID;

    private static final String TASK_WIZARD_PARENT_PROBLEM = "parentProblem";

    private static final String TASK_NAME = "Task " + LocalDateTime.now();
    private static final String TASK_NEW_ASSIGNEE = "sd_seleniumtest";
    private static final String TASK_NEW_STATUS = "In Progress";

    private static final String PROBLEM_OUT_PREFIX = "problemOut";
    private static final String NAME_ATTR = "name";
    private static final String MY_TASKS_ASSIGNEE_ATTRIBUTE = PROBLEM_OUT_PREFIX + "." + ISSUE_OUT_ASSIGNEE_ATTR;
    private static final String MY_TASKS_STATUS_ATTRIBUTE = PROBLEM_OUT_PREFIX + "." + ISSUE_OUT_STATUS_ATTR;
    private static final String MY_TASKS_NAME_ATTRIBUTE = PROBLEM_OUT_PREFIX + "." + NAME_ATTR;

    @BeforeMethod
    public void goToTaskDashboardPage() {
        taskDashboardPage = TaskDashboardPage.goToPage(driver, BASIC_URL);
    }

    @Parameters({"parentProblem", "taskAssignee"})
    @Test(priority = 1, testName = "Create Task", description = "Create Task")
    @Description("Create Task")
    public void createTask(
            @Optional("50") String parentProblem,
            @Optional("ca_kodrobinska") String taskAssignee
    ) {
        SDWizardPage = taskDashboardPage.openCreateTaskWizard();
        SDWizardPage.insertValueToSearchComponent(parentProblem, TASK_WIZARD_PARENT_PROBLEM);
        SDWizardPage.createTask(TASK_NAME, taskAssignee, TASK_LABEL);
        taskID = taskDashboardPage.getIDForTaskWithName(TASK_NAME);

        Assert.assertEquals(taskDashboardPage.getAssigneeForNthTaskInTasksTable(0), taskAssignee);
    }

    @Test(priority = 2, testName = "Edit Task", description = "Edit Task")
    @Description("Edit Task")
    public void editTask() {
        issueDetailsPage = taskDashboardPage.openIssueDetailsView(taskID, BASIC_URL, PROBLEM_ISSUE_TYPE);
        issueDetailsPage.maximizeWindow(DETAILS_TABS_CONTAINER_ID);
        taskOverviewTab = issueDetailsPage.selectOverviewTab(PROBLEM_ISSUE_TYPE);
        taskOverviewTab.changeIssueAssignee(TASK_NEW_ASSIGNEE);
        taskOverviewTab.changeIssueStatus(TASK_NEW_STATUS);
        goToTaskDashboardPage();

        Assert.assertEquals(taskDashboardPage.getAssigneeForNthTaskInTasksTable(0), TASK_NEW_ASSIGNEE);
    }

    @Test(priority = 3, testName = "My Tasks view", description = "My Tasks view")
    @Description("My Tasks view")
    public void myTasks() {
        myTasksPage = new MyTasksPage(driver, webDriverWait);
        myTasksPage.openView(driver, BASIC_URL);
        myTasksPage.filterByTextField(MY_TASKS_NAME_ATTRIBUTE, TASK_NAME);
        myTasksPage.filterByTextField(MY_TASKS_ASSIGNEE_ATTRIBUTE, TASK_NEW_ASSIGNEE);
        myTasksPage.filterByComboBox(MY_TASKS_STATUS_ATTRIBUTE, TASK_NEW_STATUS);

        Assert.assertEquals(myTasksPage.countIssuesInTable(), 1);
        Assert.assertEquals(myTasksPage.getIssueID(0), taskID);
    }
}
