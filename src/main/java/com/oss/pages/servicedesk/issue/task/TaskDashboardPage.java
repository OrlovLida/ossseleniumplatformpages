package com.oss.pages.servicedesk.issue.task;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.pages.servicedesk.BaseSDPage;
import com.oss.pages.servicedesk.issue.wizard.SDWizardPage;

import io.qameta.allure.Step;

public class TaskDashboardPage extends BaseSDPage {

    private static final Logger log = LoggerFactory.getLogger(TaskDashboardPage.class);

    private static final String CREATE_TASK_BUTTON_ID = "create-task";
    private static final String TASKS_TABLE_ID = "_tableTasks";
    private static final String TASKS_TABLE_TASK_NAME = "Task Name";
    private static final String TASKS_TABLE_TASK_ID = "Task ID";
    private static final String ASSIGNEE_ATTRIBUTE = "Assignee";
    private static final String EDIT_TASK_BUTTON_ID = "open-details";
    private static final String TASKS_URL_PATTERN = "%s/#/view/service-desk/problem/tasks";
    private static final String PROMPT_ID = "_createTask_prompt-card";
    private static final String EDIT_PROMPT_ID = "_taskDetails_prompt-card";

    public TaskDashboardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("I Open task dashboard View")
    public static TaskDashboardPage goToPage(WebDriver driver, String basicURL) {
        WebDriverWait wait = new WebDriverWait(driver, 150);

        String pageUrl = String.format(TASKS_URL_PATTERN, basicURL);
        driver.get(pageUrl);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Opened page: {}", pageUrl);

        return new TaskDashboardPage(driver, wait);
    }

    @Step("Check if Task Dashboard is opened")
    public boolean isTaskDashboardOpened(String basicURL) {
        log.info("Current URL is {}", driver.getCurrentUrl());
        return driver.getCurrentUrl().equals(String.format(TASKS_URL_PATTERN, basicURL));
    }

    @Step("I open create task wizard")
    public SDWizardPage openCreateTaskWizard() {
        DelayUtils.waitForPageToLoad(driver, wait);
        Button.createById(driver, CREATE_TASK_BUTTON_ID).click();
        log.info("Create task wizard is opened");

        return new SDWizardPage(driver, wait, PROMPT_ID);
    }

    @Step("I check Tasks table")
    public OldTable getTasksTable() {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Create Tasks Table");
        return OldTable.createById(driver, wait, TASKS_TABLE_ID);
    }

    private String getAttributeFromTasksTable(int index, String attributeName) {
        String attributeValue = getTasksTable().getCellValue(index, attributeName);
        log.info("Got value for {} attribute: {} from Tasks Table", attributeName, attributeValue);
        return attributeValue;
    }

    public String getAssigneeForNthTaskInTasksTable(int n) {
        DelayUtils.waitForPageToLoad(driver, wait);
        return getAttributeFromTasksTable(n, ASSIGNEE_ATTRIBUTE);
    }

    @Step("get row number for task with {id}")
    public int getRowForTaskWithID(String id) {
        return getTasksTable().getRowNumber(id, TASKS_TABLE_TASK_ID);
    }

    @Step("get row number for task with name {taskName}")
    public int getRowForTaskWithName(String taskName) {
        return getTasksTable().getRowNumber(taskName, TASKS_TABLE_TASK_NAME);
    }

    @Step("get task ID for task with name {taskName}")
    public String getIDForTaskWithName(String taskName) {

        return getTasksTable().getCellValue(getRowForTaskWithName(taskName), TASKS_TABLE_TASK_ID);
    }

    public void selectTask(int taskIndex) {
        DelayUtils.waitForPageToLoad(driver, wait);
        OldTable.createById(driver, wait, TASKS_TABLE_ID).selectRow(taskIndex);
    }

    @Step("I open edit task wizard")
    public SDWizardPage openEditTaskWizard() {
        DelayUtils.waitForPageToLoad(driver, wait);
        Button.createById(driver, EDIT_TASK_BUTTON_ID).click();
        log.info("Edit task wizard is opened");

        return new SDWizardPage(driver, wait, EDIT_PROMPT_ID);
    }
}
