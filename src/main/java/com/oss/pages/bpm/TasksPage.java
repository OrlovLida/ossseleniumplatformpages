/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.pages.bpm;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.Input.ComponentType;
import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.components.prompts.ConfirmationBoxInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.widgets.table.TableInterface;
import com.oss.framework.widgets.tabs.TabsInterface;
import com.oss.framework.widgets.tabs.TabsWidget;
import com.oss.framework.widgets.treetable.OldTreeTableWidget;
import com.oss.pages.BasePage;
import com.oss.pages.dms.AttachFileWizardPage;

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
    private static final String TABS_TASKS_VIEW_ID = "bpm_task_view_tabs-container";
    private static final String ATTACH_FILE_BUTTON_ID = "addAttachmentAction";
    private static final String FORM_TAB_ID = "bpm_task_view_form-tab";
    private static final String ATTACHMENT_TAB_ID = "bpm_task_view_tasks-attachment-tab";
    private static final String ASSIGN_TASK_ICON_ID = "form.toolbar.assignTask";
    private static final String COMPLETE_TASK_ICON_ID = "form.toolbar.closeTask";
    private static final String SETUP_INTEGRATION_ICON_ID = "form.toolbar.setupIntegrationButton";
    private static final String IP_TABLE_ID = "form.specific.ip_involved_nrp_group.ip_involved_nrp_table";
    private static final String TRANSITION_COMBOBOX_ID = "transitionComboBox";
    private static final String PROCESS_CODE = "Process Code";
    private static final String NAME = "Name";
    private static final String ASSIGNEE = "Assignee";

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
        table.doRefreshWhileNoData(10000, "refreshTable");
        table.selectRowByAttributeValueWithLabel(PROCESS_CODE, processCode);
    }

    public String startTaskByUsernameAndTaskName(String username, String taskName) {
        OldTable table = getOldTable();
        table.searchByAttributeWithLabel(ASSIGNEE, Input.ComponentType.TEXT_FIELD, username);
        DelayUtils.waitForPageToLoad(driver, wait);
        table.searchByAttributeWithLabel(NAME, Input.ComponentType.TEXT_FIELD, taskName);
        DelayUtils.waitForPageToLoad(driver, wait);
        if (table.hasNoData()) {
            return "There is no task for specified values";
        } else {
            return getProcessCodeAndStartItIfNotStarted(username, taskName);
        }
    }

    public void changeTransitionAndCompleteTask(String processCode, String taskName, String transition) {
        findTask(processCode, taskName);
        DelayUtils.waitForPageToLoad(driver, wait);
        Input input = ComponentFactory.create(TRANSITION_COMBOBOX_ID, ComponentType.BPM_COMBOBOX, driver, wait);
        input.setSingleStringValue(transition);
        DelayUtils.waitForPageToLoad(driver, wait);
        actionTask(COMPLETE_TASK_ICON_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
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

    public void addFile(String processCode, String taskName, String filePath) {
        findTask(processCode, taskName);
        selectTab(ATTACHMENT_TAB_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        getTab().callActionById(ATTACH_FILE_BUTTON_ID);
        AttachFileWizardPage attachFileWizardPage = new AttachFileWizardPage(driver);
        attachFileWizardPage.selectRadioButton("Upload anyway");
        attachFileWizardPage.attachFile(filePath);
        attachFileWizardPage.skipAndAccept();
    }

    public void selectTab(String tabId) {
        getTab().selectTabById(tabId);
    }

    public void clickPerformConfigurationButton() {
        Button button = Button.createByLabel(driver, TABS_TASKS_VIEW_ID, "Perform Configuration");
        button.click();
    }

    public void clickPlanViewButton() {
        Button button = Button.createById(driver, "planView");
        button.click();
    }

    public void showCompletedTasks() {
        getOldTable().selectPredefinedFilter("Show with Completed");
    }

    public String getIPCodeByProcessName(String processIPName) {
        TableInterface ipTable = getIPTable();
        int rowNumber = ipTable.getRowNumber(processIPName, NAME);
        return ipTable.getCellValue(rowNumber, "Code");
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
        String ipCode = ipTable.getCellValue(0, "Code");
        startTask(ipCode, SCOPE_DEFINITION_TASK);
        completeTask(ipCode, SCOPE_DEFINITION_TASK);
        startTask(ipCode, IMPLEMENTATION_TASK);
        return ipCode;
    }

    public List<String> getListOfAttachments() {
        OldTreeTableWidget treeTable =
                OldTreeTableWidget.create(driver, wait, "attachmentManagerBusinessView_commonTreeTable_BPMTask");
        List<String> allNodes = treeTable.getAllVisibleNodes("Attachments and directories");
        return allNodes.stream().filter(node -> !node.equals("HOME")).collect(Collectors.toList());
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
        log.debug("Process Code = {}", processCode);
        String assigne = table.getCellValue(0, ASSIGNEE);
        log.debug("Assignee = {}", assigne);
        if (!assigne.equals(username)) {
            startTask(processCode, taskName);
        }
        return processCode;
    }

    private TabsInterface getTab() {
        return TabsWidget.createById(driver, wait, TABS_TASKS_VIEW_ID);

    }

    private TableInterface getIPTable() {
        return OldTable.createById(driver, wait, IP_TABLE_ID);
    }

    private void actionTask(String actionId) {
        selectTab(FORM_TAB_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        getTab().callActionById(actionId);
        DelayUtils.waitForPageToLoad(driver, wait);
        ConfirmationBoxInterface prompt = ConfirmationBox.create(driver, wait);
        prompt.clickButtonByLabel("Proceed");
    }
}
