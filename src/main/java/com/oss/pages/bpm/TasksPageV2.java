package com.oss.pages.bpm;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.TableInterface;
import com.oss.framework.widgets.table.TableWidget;
import com.oss.pages.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Paweł Rother
 */

public class TasksPageV2 extends BasePage {
    public static final String READY_FOR_INTEGRATION_TASK = "Ready for Integration";
    public static final String IMPLEMENTATION_TASK = "Implementation";
    public static final String SCOPE_DEFINITION_TASK = "Scope definition";
    public static final String ACCEPTANCE_TASK = "Acceptance";
    public static final String VERIFICATION_TASK = "Verification";
    public static final String LOW_LEVEL_PLANNING_TASK = "Low Level Planning";
    public static final String HIGH_LEVEL_PLANNING_TASK = "High Level Planning";
    public static final String CORRECT_DATA_TASK = "Correct data";
    public static final String UPDATE_REQUIREMENTS_TASK = "Update Requirements";
    private static final Logger log = LoggerFactory.getLogger(TasksPageV2.class);
    private static final String TABLE_TASKS_ID = "bpm_task_graphql_view_tasksTableWidget";
    private static final String TABS_TASKS_VIEW_ID = "bpm_task_graphql_view_tasksTabsWidget";
    private static final String PROCESS_CODE = "Process Code";
    private static final String NAME = "Name";
    private static final String ASSIGNEE = "Assignee";
    private static final String NON_EXISTING_TASK_EXCEPTION = "There is no task for specified values";
    private static final String CODE_LABEL = "Code";
    private static final String TASK_NAME_INPUT_ID = "name";
    private static final String PROCESS_CODE_INPUT_ID = "processInstanceCode";
    private static final String PROCESS_CODE_COLUMN_ID = "processInstanceCode";
    private static final String ASSIGNEE_COLUMN_ID = "assigneeUser.name";
    private static final String ASSIGNEE_INPUT_ID = "assigneeUser_OSF";
    private static final String STATUS_INPUT_ID = "state";
    private static final String FINISHED_STATUS = "Finished";

    public TasksPageV2(WebDriver driver) {
        super(driver);
    }

    public static TasksPageV2 goToTasksPage(WebDriver driver, WebDriverWait wait, String basicURL) {
        DelayUtils.waitForPageToLoad(driver, wait);
        driver.get(String.format("%s/#/views/bpm/tasksgql", basicURL));
        DelayUtils.waitForPageToLoad(driver, wait);
        return new TasksPageV2(driver);
    }

    public TableWidget getTableWidget() {
        return TableWidget.createById(driver, TABLE_TASKS_ID, wait);
    }

    public void clearFilters() {
        TableWidget tasksTable = getTableWidget();
        tasksTable.clearAllFilters();
    }

    public void findTask(String processCode, String taskName) {
        TableWidget table = getTableWidget();
        table.clearAllFilters();
        table.searchByAttribute(PROCESS_CODE_INPUT_ID, processCode);
        DelayUtils.waitForPageToLoad(driver, wait);
        table.searchByAttribute(TASK_NAME_INPUT_ID, taskName);
        DelayUtils.waitForPageToLoad(driver, wait);
        table.selectRowByAttributeValue(PROCESS_CODE_COLUMN_ID, processCode);
    }

    private String getProcessCodeAndStartItIfNotStarted(String username, String taskName) {
        TableWidget table = getTableWidget();
        String processCode = table.getCellValue(0, PROCESS_CODE_COLUMN_ID);
        log.debug(PROCESS_CODE + " = {}", processCode);
        String assignee = table.getCellValue(0, ASSIGNEE_COLUMN_ID);
        log.debug(ASSIGNEE + " = {}", assignee);
        if (!assignee.equals(username)) {
            startTask(processCode, taskName);
        }
        return processCode;
    }

    public void startTask(String processCode, String taskName) {
        findTask(processCode, taskName);
        DelayUtils.waitForPageToLoad(driver, wait);
        getTaskForm().startTask();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void completeTask(String processCode, String taskName) {
        findTask(processCode, taskName);
        DelayUtils.waitForPageToLoad(driver, wait);
        getTaskForm().completeTask();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void setupIntegration(String processCode) {
        findTask(processCode, READY_FOR_INTEGRATION_TASK);
        DelayUtils.waitForPageToLoad(driver, wait);
        getTaskForm().setupIntegration();
    }

    public String startTaskByUsernameAndTaskName(String username, String taskName) {
        TableWidget table = getTableWidget();
        table.searchByAttribute(ASSIGNEE_INPUT_ID, username);
        DelayUtils.waitForPageToLoad(driver, wait);
        table.searchByAttribute(TASK_NAME_INPUT_ID, taskName);
        DelayUtils.waitForPageToLoad(driver, wait);
        if (table.hasNoData()) {
            return NON_EXISTING_TASK_EXCEPTION;
        } else {
            return getProcessCodeAndStartItIfNotStarted(username, taskName);
        }
    }

    public void changeTransitionAndCompleteTask(String processCode, String taskName, String transition) {
        findTask(processCode, taskName);
        DelayUtils.waitForPageToLoad(driver, wait);
        getTaskForm().setTransition(transition);
        DelayUtils.waitForPageToLoad(driver, wait);
        getTaskForm().completeTask();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void addFile(String processCode, String taskName, String filePath) {
        findTask(processCode, taskName);
        getTaskForm().attachFile(filePath);
    }

    public List<String> getListOfAttachments() {
        return getTaskForm().getListOfAttachments();
    }

    public void showCompletedTasks() {
        TableWidget table = getTableWidget();
        table.clearAllFilters();
        table.searchByAttribute(STATUS_INPUT_ID, FINISHED_STATUS);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public String getIPCodeByProcessName(String processIPName) {
        TableInterface ipTable = getTaskForm().getIPTable();
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
        TableInterface ipTable = getTaskForm().getIPTable();
        String ipCode = ipTable.getCellValue(0, CODE_LABEL);
        startTask(ipCode, SCOPE_DEFINITION_TASK);
        completeTask(ipCode, SCOPE_DEFINITION_TASK);
        startTask(ipCode, IMPLEMENTATION_TASK);
        return ipCode;
    }

    public void clickPerformConfigurationButton() {
        getTaskForm().clickPerformConfigurationButton();
    }

    public void clickPlanViewButton() {
        getTaskForm().clickPlanViewButton();
    }

    private IPDTaskFormPage getTaskForm() {
        return IPDTaskFormPage.create(driver, wait, TABS_TASKS_VIEW_ID);
    }
}
