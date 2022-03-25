package com.oss.servicedesk;

import java.time.LocalDateTime;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.servicedesk.task.MyTasksPage;
import com.oss.pages.servicedesk.task.TaskDashboardPage;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

import static com.oss.pages.servicedesk.GraphQLIssueTableConstants.ISSUE_OUT_ASSIGNEE_ATTR;
import static com.oss.pages.servicedesk.GraphQLIssueTableConstants.ISSUE_OUT_STATUS_ATTR;

@Listeners({TestListener.class})
public class CreateTaskVFNZTest extends BaseTestCase {

    private TaskDashboardPage taskDashboardPage;
    private MyTasksPage myTasksPage;
    private com.oss.pages.servicedesk.ticket.wizard.SDWizardPage SDWizardPage;
    private String taskID;

    private static final String TASK_WIZARD_NAME = "name";
    private static final String TASK_WIZARD_ASSIGNEE = "assignee";
    private static final String TASK_WIZARD_PARENT_PROBLEM = "parentProblem";
    private static final String TASK_WIZARD_LABEL = "label";
    private static final String TASK_WIZARD_CREATE_TASK_BUTTON = "_createTaskSubmitId-1";
    private static final String TASK_WIZARD_STATUS = "status";
    private static final String TASK_WIZARD_SAVE_BUTTON = "_taskDetailsSubmitId-1";

    private static final String TASK_NAME = "Task " + LocalDateTime.now();
    private static final String TASK_NEW_ASSIGNEE = "sd_seleniumtest";
    private static final String TASK_LABEL = "Task - Selenium Test";
    private static final String TASK_NEW_STATUS = "In Progress";

    private static final String PROBLEM_OUT_PREFIX = "problemOut";
    private static final String NAME_ATTR = "name";
    private static final String MY_TASKS_ASSIGNEE_ATTRIBUTE = PROBLEM_OUT_PREFIX + "." + ISSUE_OUT_ASSIGNEE_ATTR;
    private static final String MY_TASKS_STATUS_ATTRIBUTE = PROBLEM_OUT_PREFIX + "." + ISSUE_OUT_STATUS_ATTR;
    private static final String MY_TASKS_NAME_ATTRIBUTE = PROBLEM_OUT_PREFIX + "." + NAME_ATTR;

    @BeforeMethod
    public void goToTicketDashboardPage() {
        taskDashboardPage = TaskDashboardPage.goToPage(driver, BASIC_URL);
    }

    @Parameters({"parentProblem", "taskAssignee"})
    @Test(priority = 1, testName = "Create Task", description = "Create Task")
    @Description("Create Task")
    public void createTask(
            @Optional("142") String parentProblem,
            @Optional("ca_kodrobinska") String taskAssignee
    ) {
        SDWizardPage = taskDashboardPage.openCreateTaskWizard();
        SDWizardPage.insertValueToTextComponent(TASK_NAME, TASK_WIZARD_NAME);
        SDWizardPage.insertValueToSearchComponent(taskAssignee, TASK_WIZARD_ASSIGNEE);
        SDWizardPage.insertValueToSearchComponent(parentProblem, TASK_WIZARD_PARENT_PROBLEM);
        SDWizardPage.insertValueToTextComponent(TASK_LABEL, TASK_WIZARD_LABEL);
        SDWizardPage.clickButton(TASK_WIZARD_CREATE_TASK_BUTTON);
        taskID = taskDashboardPage.getIDForTaskWithName(TASK_NAME);

        Assert.assertEquals(taskDashboardPage.getAssigneeForNthTaskInTasksTable(0), taskAssignee);
    }

    @Test(priority = 2, testName = "Edit Task", description = "Edit Task")
    @Description("Edit Task")
    public void editTask() {
        taskDashboardPage.selectTask(taskDashboardPage.getRowForTaskWithID(taskID));
        SDWizardPage = taskDashboardPage.openEditTaskWizard();
        SDWizardPage.insertValueToSearchComponent(TASK_NEW_ASSIGNEE, TASK_WIZARD_ASSIGNEE);
        SDWizardPage.insertValueToComboBoxComponent(TASK_NEW_STATUS, TASK_WIZARD_STATUS);
        SDWizardPage.clickButton(TASK_WIZARD_SAVE_BUTTON);

        Assert.assertEquals(taskDashboardPage.getAssigneeForNthTaskInTasksTable(0), TASK_NEW_ASSIGNEE);
    }

    @Test(priority = 3, testName = "My Tasks view", description = "My Tasks view")
    @Description("My Tasks view")
    public void myTasks() {
        myTasksPage = new MyTasksPage(driver, webDriverWait);
        myTasksPage.goToPage(driver, BASIC_URL);
        myTasksPage.filterByTextField(MY_TASKS_NAME_ATTRIBUTE, TASK_NAME);
        myTasksPage.filterByTextField(MY_TASKS_ASSIGNEE_ATTRIBUTE, TASK_NEW_ASSIGNEE);
        myTasksPage.filterByComboBox(MY_TASKS_STATUS_ATTRIBUTE, TASK_NEW_STATUS);

        Assert.assertEquals(myTasksPage.countIssuesInTable(), 1);
        Assert.assertEquals(myTasksPage.getIssueID(0), taskID);
    }
}
