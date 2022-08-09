package com.oss.pages.bpm;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.TableInterface;
import com.oss.framework.widgets.table.TableWidget;
import com.oss.pages.BasePage;
import com.oss.pages.platform.HomePage;

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
    private static final String BPM_AND_PLANNING = "BPM and Planning";
    private static final String TASKS = "Tasks";
    private static final String PROCESS_OPERATIONS = "Process Operations";

    public TasksPageV2(WebDriver driver) {
        super(driver);
    }

    public static TasksPageV2 goToTasksPage(WebDriver driver, WebDriverWait wait, String basicURL) {
        DelayUtils.waitForPageToLoad(driver, wait);
        driver.get(String.format("%s/#/views/bpm/tasksgql", basicURL));
        DelayUtils.waitForPageToLoad(driver, wait);
        return new TasksPageV2(driver);
    }

    public static TasksPageV2 goToTasksPage(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitForPageToLoad(driver, wait);
        HomePage homePage = new HomePage(driver);
        homePage.chooseFromLeftSideMenu(TASKS, BPM_AND_PLANNING, PROCESS_OPERATIONS);
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
        DelayUtils.waitForPageToLoad(driver, wait);
        TableWidget table = getTableWidget();
        table.clearAllFilters();
        table.searchByAttribute(PROCESS_CODE_INPUT_ID, processCode);
        DelayUtils.waitForPageToLoad(driver, wait);
        table.searchByAttribute(TASK_NAME_INPUT_ID, taskName);
        DelayUtils.waitForPageToLoad(driver, wait);
        table.selectRowByAttributeValue(PROCESS_CODE_COLUMN_ID, processCode);
        DelayUtils.waitForPageToLoad(driver, wait);
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
        getTaskForm().startTask();
    }

    public void completeTask(String processCode, String taskName) {
        findTask(processCode, taskName);
        getTaskForm().completeTask();
    }

    public void setupIntegration(String processCode) {
        findTask(processCode, READY_FOR_INTEGRATION_TASK);
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
        getTaskForm().setTransition(transition);
        getTaskForm().completeTask();
    }

    public void addFile(String processCode, String taskName, String filePath) {
        findTask(processCode, taskName);
        getTaskForm().attachFile(filePath);
    }

    public List<String> getListOfAttachments() {
        return getTaskForm().getListOfAttachments();
    }

    public void showCompletedTasks() {
        DelayUtils.waitForPageToLoad(driver, wait);
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
        DelayUtils.waitForPageToLoad(driver, wait);
        startAndCompleteTask(ipCode, ACCEPTANCE_TASK);
        DelayUtils.waitForPageToLoad(driver, wait);
        startAndCompleteTask(processCode, VERIFICATION_TASK);
    }

    public String proceedNRPToImplementationTask(String processCode) {
        completeTask(processCode, HIGH_LEVEL_PLANNING_TASK);
        DelayUtils.waitForPageToLoad(driver, wait);
        startAndCompleteTask(processCode, LOW_LEVEL_PLANNING_TASK);
        return proceedNRPFromReadyForIntegration(processCode);
    }

    public String proceedNRPFromReadyForIntegration(String processCode) {
        DelayUtils.waitForPageToLoad(driver, wait);
        startAndCompleteTask(processCode, READY_FOR_INTEGRATION_TASK);
        String ipCode = getIPCodeFromCompletedNRP(processCode);
        startAndCompleteTask(ipCode, SCOPE_DEFINITION_TASK);
        DelayUtils.waitForPageToLoad(driver, wait);
        startTask(ipCode, IMPLEMENTATION_TASK);
        return ipCode;
    }

    private String getIPCodeFromCompletedNRP(String nrpCode) {
        showCompletedTasks();
        findTask(nrpCode, READY_FOR_INTEGRATION_TASK);
        DelayUtils.sleep(3000);
        TableInterface ipTable = getTaskForm().getIPTable();
        String ipCode = ipTable.getCellValue(0, CODE_LABEL);
        TableWidget table = getTableWidget();
        table.unselectAllRows();
        DelayUtils.waitForPageToLoad(driver, wait);
        table.hideSelectionBar();
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

    public void startAndCompleteTask(String processCode, String taskName) {
        startTask(processCode, taskName);
        completeTask(processCode, taskName);
    }
}
