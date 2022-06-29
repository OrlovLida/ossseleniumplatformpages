package com.oss.pages.bpm;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.components.prompts.ConfirmationBoxInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.widgets.table.TableInterface;
import com.oss.framework.widgets.table.TableWidget;
import com.oss.framework.widgets.tabs.TabsInterface;
import com.oss.framework.widgets.tabs.TabsWidget;
import com.oss.framework.widgets.treetable.OldTreeTableWidget;
import com.oss.pages.BasePage;
import com.oss.pages.dms.AttachFileWizardPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

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
    private static final String ATTACH_FILE_BUTTON_ID = "addAttachmentAction";
    private static final String FORM_TAB_ID = "bpm_task_graphql_view_form-tab";
    private static final String ATTACHMENT_TAB_ID = "bpm_task_graphql_view_tasks-attachments-tab";
    private static final String ASSIGN_TASK_ICON_ID = "form.toolbar.assignTask";
    private static final String COMPLETE_TASK_ICON_ID = "form.toolbar.closeTask";
    private static final String SETUP_INTEGRATION_ICON_ID = "form.toolbar.setupIntegrationButton";
    private static final String IP_TABLE_ID = "form.specific.ip_involved_nrp_group.ip_involved_nrp_table";
    private static final String TRANSITION_COMBOBOX_ID = "transitionComboBox";
    private static final String PROCESS_CODE = "Process Code";
    private static final String NAME = "Name";
    private static final String ASSIGNEE = "Assignee";
    private static final String NON_EXISTING_TASK_EXCEPTION = "There is no task for specified values";
    private static final String UPLOAD_ANYWAY_LABEL = "Upload anyway";
    private static final String PERFORM_CONFIGURATION_BUTTON_LABEL = "Perform Configuration";
    private static final String PLAN_VIEW_BUTTON_LABEL = "Plan View";
    private static final String CODE_LABEL = "Code";
    private static final String ATTACHMENTS_LIST_ID = "attachmentManagerBusinessView_commonTreeTable_BPMTask";
    private static final String ATTACHMENTS_AND_DIRECTORIES = "Attachments and directories";
    private static final String HOME_LABEL = "HOME";
    private static final String PROCEED_BUTTON_LABEL = "Proceed";
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

    private TableInterface getIPTable() {
        return OldTable.createById(driver, wait, IP_TABLE_ID);
    }

    public void clearFilters() {
        TableWidget tasksTable = getTableWidget();
        tasksTable.clearAllFilters();
    }

    private TabsInterface getTab() {
        return TabsWidget.createById(driver, wait, TABS_TASKS_VIEW_ID);
    }

    public void selectTab(String tabId) {
        getTab().selectTabById(tabId);
    }

    private void actionTask(String actionId) {
        selectTab(FORM_TAB_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        getTab().callActionById(actionId);
        DelayUtils.waitForPageToLoad(driver, wait);
        ConfirmationBoxInterface prompt = ConfirmationBox.create(driver, wait);
        prompt.clickButtonByLabel(PROCEED_BUTTON_LABEL);
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
        actionTask(ASSIGN_TASK_ICON_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void completeTask(String processCode, String taskName) {
        findTask(processCode, taskName);
        DelayUtils.waitForPageToLoad(driver, wait);
        actionTask(COMPLETE_TASK_ICON_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void setupIntegration(String processCode) {
        findTask(processCode, READY_FOR_INTEGRATION_TASK);
        DelayUtils.waitForPageToLoad(driver, wait);
        getTab().callActionById(SETUP_INTEGRATION_ICON_ID);
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
        Input input = ComponentFactory.create(TRANSITION_COMBOBOX_ID, Input.ComponentType.BPM_COMBOBOX, driver, wait);
        input.setSingleStringValue(transition);
        DelayUtils.waitForPageToLoad(driver, wait);
        actionTask(COMPLETE_TASK_ICON_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void addFile(String processCode, String taskName, String filePath) {
        findTask(processCode, taskName);
        selectTab(ATTACHMENT_TAB_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        getTab().callActionById(ATTACH_FILE_BUTTON_ID);
        AttachFileWizardPage attachFileWizardPage = new AttachFileWizardPage(driver);
        attachFileWizardPage.selectRadioButton(UPLOAD_ANYWAY_LABEL);
        attachFileWizardPage.attachFile(filePath);
        attachFileWizardPage.skipAndAccept();
    }

    public List<String> getListOfAttachments() {
        OldTreeTableWidget treeTable =
                OldTreeTableWidget.create(driver, wait, ATTACHMENTS_LIST_ID);
        List<String> allNodes = treeTable.getAllVisibleNodes(ATTACHMENTS_AND_DIRECTORIES);
        return allNodes.stream().filter(node -> !node.equals(HOME_LABEL)).collect(Collectors.toList());
    }

    public void clickPerformConfigurationButton() {
        Button button = Button.createByLabel(driver, TABS_TASKS_VIEW_ID, PERFORM_CONFIGURATION_BUTTON_LABEL);
        button.click();
    }

    public void clickPlanViewButton() {
        Button button = Button.createByLabel(driver, PLAN_VIEW_BUTTON_LABEL);
        button.click();
    }

    public void showCompletedTasks() {
        TableWidget table = getTableWidget();
        table.clearAllFilters();
        table.searchByAttribute(STATUS_INPUT_ID, FINISHED_STATUS);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public String getIPCodeByProcessName(String processIPName) {
        TableInterface ipTable = getIPTable();
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
        TableInterface ipTable = getIPTable();
        String ipCode = ipTable.getCellValue(0, CODE_LABEL);
        startTask(ipCode, SCOPE_DEFINITION_TASK);
        completeTask(ipCode, SCOPE_DEFINITION_TASK);
        startTask(ipCode, IMPLEMENTATION_TASK);
        return ipCode;
    }
}
