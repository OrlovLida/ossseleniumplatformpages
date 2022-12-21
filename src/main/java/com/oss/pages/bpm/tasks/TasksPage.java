/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.pages.bpm.tasks;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.widgets.table.TableInterface;
import com.oss.pages.BasePage;
import com.oss.pages.bpm.tasks.taskforms.IPDTaskFormPage;
import com.oss.pages.bpm.tasks.taskforms.KDTaskFormPage;

import io.qameta.allure.Step;

/**
 * @author Gabriela Kasza
 */
public class TasksPage extends BasePage {

    public static final String READY_FOR_INTEGRATION_TASK = "Ready for Integration";
    public static final String IMPLEMENTATION_TASK = "Implementation";
    public static final String SCOPE_DEFINITION_TASK = "Scope definition";
    public static final String ACCEPTANCE_TASK = "Acceptance";
    public static final String VERIFICATION_TASK = "Verification";
    public static final String LOW_LEVEL_PLANNING_TASK = "Low Level Planning";
    public static final String HIGH_LEVEL_PLANNING_TASK = "High Level Planning";
    public static final String CORRECT_DATA_TASK = "Correct data";
    public static final String UPDATE_REQUIREMENTS_TASK = "Update Requirements";
    private static final Logger log = LoggerFactory.getLogger(TasksPage.class);
    private static final String TABLE_TASKS_ID = "bpm_task_view_task-table";
    private static final String TABS_TASKS_VIEW_ID = "bpm_task_view_right_window";
    private static final String PROCESS_CODE = "Process Code";
    private static final String NAME = "Name";
    private static final String ASSIGNEE = "Assignee";
    private static final String REFRESH_TABLE_ID = "refreshTable";
    private static final String NON_EXISTING_TASK_EXCEPTION = "There is no task for specified values";
    private static final String SHOW_WITH_COMPLETED_FILTER_LABEL = "Show with Completed";
    private static final String CODE_LABEL = "Code";

    public TasksPage(WebDriver driver) {
        super(driver);
    }

    public static TasksPage goToTasksPage(WebDriver driver, WebDriverWait wait, String basicURL) {
        DelayUtils.waitForPageToLoad(driver, wait);
        driver.get(String.format("%s/#/view/bpm/tasks", basicURL));
        DelayUtils.waitForPageToLoad(driver, wait);
        return new TasksPage(driver);
    }

    public void clearAllColumnFilters() {
        OldTable tasksTable = getOldTable();
        tasksTable.clearAllColumnValues();
    }

    public void findTask(String processCode, String taskName) {
        OldTable table = getOldTable();
        table.clearColumnValue(ASSIGNEE);
        table.searchByAttributeWithLabel(PROCESS_CODE, Input.ComponentType.TEXT_FIELD, processCode);
        DelayUtils.waitForPageToLoad(driver, wait);
        table.searchByAttributeWithLabel(NAME, Input.ComponentType.TEXT_FIELD, taskName);
        table.doRefreshWhileNoData(10000, REFRESH_TABLE_ID);
        table.selectRowByAttributeValueWithLabel(PROCESS_CODE, processCode);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public String startTaskByUsernameAndTaskName(String username, String taskName) {
        OldTable table = getOldTable();
        table.searchByAttributeWithLabel(ASSIGNEE, Input.ComponentType.TEXT_FIELD, username);
        DelayUtils.waitForPageToLoad(driver, wait);
        table.searchByAttributeWithLabel(NAME, Input.ComponentType.TEXT_FIELD, taskName);
        DelayUtils.waitForPageToLoad(driver, wait);
        if (table.hasNoData()) {
            return NON_EXISTING_TASK_EXCEPTION;
        } else {
            return getProcessCodeAndStartItIfNotStarted(username, taskName);
        }
    }

    public void changeTransitionAndCompleteTask(String processCode, String taskName, String transition) {
        findTask(processCode, taskName);
        getIPDTaskForm().setTransition(transition);
        getIPDTaskForm().completeTask();
    }

    @Step("Checking is task started")
    public boolean isTaskStarted(String processCode, String taskName) {
        findTask(processCode, taskName);
        DelayUtils.waitForPageToLoad(driver, wait);
        return !getOldTable().getCellValue(0, ASSIGNEE).isEmpty();
    }

    public void startTask(String processCode, String taskName) {
        findTask(processCode, taskName);
        getIPDTaskForm().startTask();
    }

    public void completeTask(String processCode, String taskName) {
        findTask(processCode, taskName);
        getIPDTaskForm().completeTask();
    }

    public void setupIntegration(String processCode) {
        findTask(processCode, READY_FOR_INTEGRATION_TASK);
        getIPDTaskForm().openSetupIntegrationWizard();
    }

    public void addFile(String processCode, String taskName, String filePath) {
        findTask(processCode, taskName);
        getIPDTaskForm().attachFile(filePath);
    }

    public void clickPerformConfigurationButton() {
        getIPDTaskForm().clickPerformConfigurationButton();
    }

    public void clickPlanViewButton() {
        getIPDTaskForm().clickPlanViewButton();
    }

    public void showCompletedTasks() {
        getOldTable().selectPredefinedFilter(SHOW_WITH_COMPLETED_FILTER_LABEL);
    }

    public String getIPCodeByProcessName(String processIPName) {
        TableInterface ipTable = getIPDTaskForm().getIPTable();
        int rowNumber = ipTable.getRowNumber(processIPName, NAME);
        return ipTable.getCellValue(rowNumber, CODE_LABEL);
    }

    public void completeNRP(String processCode) {
        String ipCode = proceedNRPToImplementationTask(processCode);
        completeTask(ipCode, IMPLEMENTATION_TASK);
        startTask(ipCode, ACCEPTANCE_TASK);
        completeTask(ipCode, ACCEPTANCE_TASK);
        startTask(processCode, VERIFICATION_TASK);
        completeTask(processCode, VERIFICATION_TASK);
    }

    public String proceedNRPToImplementationTask(String processCode) {
        completeTask(processCode, HIGH_LEVEL_PLANNING_TASK);
        startTask(processCode, LOW_LEVEL_PLANNING_TASK);
        completeTask(processCode, LOW_LEVEL_PLANNING_TASK);
        return proceedNRPFromReadyForIntegration(processCode);
    }

    public String proceedNRPFromReadyForIntegration(String processCode) {
        startTask(processCode, READY_FOR_INTEGRATION_TASK);
        completeTask(processCode, READY_FOR_INTEGRATION_TASK);
        showCompletedTasks();
        findTask(processCode, READY_FOR_INTEGRATION_TASK);
        DelayUtils.sleep(3000);
        TableInterface ipTable = getIPDTaskForm().getIPTable();
        String ipCode = ipTable.getCellValue(0, CODE_LABEL);
        startTask(ipCode, SCOPE_DEFINITION_TASK);
        completeTask(ipCode, SCOPE_DEFINITION_TASK);
        startTask(ipCode, IMPLEMENTATION_TASK);
        return ipCode;
    }

    public List<String> getListOfAttachments() {
        return getIPDTaskForm().getListOfAttachments();
    }

    public void clearFilter() {
        OldTable table = getOldTable();
        table.clearColumnValue(PROCESS_CODE);
        DelayUtils.waitForPageToLoad(driver, wait);
        table.clearColumnValue(NAME);
        DelayUtils.waitForPageToLoad(driver, wait);
        table.clearColumnValue(ASSIGNEE);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    private OldTable getOldTable() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return OldTable.createById(driver, wait, TABLE_TASKS_ID);
    }

    private String getProcessCodeAndStartItIfNotStarted(String username, String taskName) {
        OldTable table = getOldTable();
        String processCode = table.getCellValue(0, PROCESS_CODE);
        log.debug(PROCESS_CODE + " = {}", processCode);
        String assignee = table.getCellValue(0, ASSIGNEE);
        log.debug(ASSIGNEE + " = {}", assignee);
        if (!assignee.equals(username)) {
            startTask(processCode, taskName);
        }
        return processCode;
    }

    public IPDTaskFormPage getIPDTaskForm() {
        return IPDTaskFormPage.create(driver, wait, TABS_TASKS_VIEW_ID);
    }

    public KDTaskFormPage getKDTaskForm() {
        return KDTaskFormPage.create(driver, wait, TABS_TASKS_VIEW_ID);
    }
}