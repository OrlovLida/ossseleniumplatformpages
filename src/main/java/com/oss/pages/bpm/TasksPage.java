/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.pages.bpm;

import com.oss.framework.components.inputs.Button;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.prompts.ConfirmationBox;
import com.oss.framework.prompts.ConfirmationBoxInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.framework.widgets.tablewidget.TableInterface;
import com.oss.framework.widgets.tabswidget.TabsInterface;
import com.oss.framework.widgets.tabswidget.TabsWidget;
import com.oss.pages.BasePage;
import com.oss.pages.dms.AttachFileWizardPage;

/**
 * @author Gabriela Kasza
 */
public class TasksPage extends BasePage {
    public static TasksPage goToTasksPage(WebDriver driver, WebDriverWait wait, String basicURL) {
        DelayUtils.waitForPageToLoad(driver, wait);
        driver.get(String.format("%s/#/view/bpm/tasks", basicURL));
        DelayUtils.waitForPageToLoad(driver, wait);
        return new TasksPage(driver);
    }
    
    private static final String TABLE_TASKS = "bpm_task_view_task-table";
    private static final String TABS_TASKS_VIEW = "bpm_task_view_tabs-container";
    private static final String ATTACH_FILE_BUTTON = "addAttachmentAction";
    private static final String FORM_TAB_ID = "bpm_task_view_form-tab";
    private static final String ATTACHMENT_TAB_ID = "bpm_task_view_tasks-attachment-tab";
    private static final String ASSIGN_TASK_ICON_ID = "form.toolbar.assignTask";
    private static final String COMPLETE_TASK_ICON_ID = "form.toolbar.closeTask";
    private static final String SETUP_INTEGRATION_ICON_ID = "form.toolbar.setupIntegrationButton";
    private static final String IP_TABLE = "form.specific.ip_involved_nrp_group.ip_involved_nrp_table";
    
    public TasksPage(WebDriver driver) {
        super(driver);
    }
    
    public void findTask(String processCode, String taskName) {
        TableInterface table = OldTable.createByComponentDataAttributeName(driver, wait, TABLE_TASKS);
        DelayUtils.waitForPageToLoad(driver, wait);
        table.searchByAttributeWithLabel("Process Code", Input.ComponentType.TEXT_FIELD, processCode);
        DelayUtils.waitForPageToLoad(driver, wait);
        table.searchByAttributeWithLabel("Name", Input.ComponentType.TEXT_FIELD, taskName);
        table.doRefreshWhileNoData(10000, "Reload table");
        table.selectRowByAttributeValueWithLabel("Process Code", processCode);
        
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
        findTask(processCode, "Ready for Integration");
        DelayUtils.waitForPageToLoad(driver, wait);
        getTab().callActionById(SETUP_INTEGRATION_ICON_ID);
    }
    
    public void addFile(String processCode, String taskName, String filePath) {
        findTask(processCode, taskName);
        selectTab(ATTACHMENT_TAB_ID);
        getTab().callActionById(ATTACH_FILE_BUTTON);
        AttachFileWizardPage attachFileWizardPage = new AttachFileWizardPage(driver);
        attachFileWizardPage.selectRadioButton("Upload anyway");
        attachFileWizardPage.attachFile(filePath);
        attachFileWizardPage.skipAndAccept();
        
    }
    
    public void selectTab(String tabId) {
        getTab().selectTabById(tabId);
    }
    
    private TabsInterface getTab() {
        return TabsWidget.createById(driver, wait, TABS_TASKS_VIEW);
        
    }
    
    private TableInterface getIPTable() {
        return OldTable.createByComponentDataAttributeName(driver, wait, IP_TABLE);
    }
    
    private void actionTask(String actionId) {
        selectTab(FORM_TAB_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        getTab().callActionById(actionId);
        ConfirmationBoxInterface prompt = ConfirmationBox.create(driver, wait);
        prompt.clickButtonByLabel("Proceed");
    }
    
    public void openIntegrationProcessByClickingOnIdLink() {
        TableInterface table = OldTable.createByComponentId(driver, wait, "ip_involved_nrp_group1");
        table.selectLinkInSpecificColumn("Id");
    }
    
    public void clickPerformConfigurationButton() {
        Button button = Button.create(driver, "Perform Configuration", "a");
        button.click();
    }
    
    public void clickPlanViewButton() {
        Button button = Button.create(driver, "Plan View", "a");
        button.click();
    }
    
    public void showCompletedTasks() {
        OldTable table = OldTable.createByComponentDataAttributeName(driver, wait, TABLE_TASKS);
        table.selectPredefinedFilter("Show with Completed");
        
    }
    
    public String getIPCodeByProcessName(String processIPName) {
        TableInterface ipTable = getIPTable();
        int rowNumber = ipTable.getRowNumber(processIPName, "Name");
        return ipTable.getCellValue(rowNumber, "Code");
    }
    
    public void completeNRP( String processCode) {
        completeTask(processCode,"High Level Planning");
        startTask(processCode, "Low Level Planning");
        completeTask(processCode, "Low Level Planning");
        startTask(processCode, "Ready for Integration");
        completeTask(processCode, "Ready for Integration");
        showCompletedTasks();
        findTask(processCode, "Ready for Integration");
        DelayUtils.sleep(3000);
        TableInterface ipTable = getIPTable();
        String ipCode = ipTable.getCellValue(0, "Code");
        startTask(ipCode, "Scope definition");
        completeTask(ipCode, "Scope definition");
        startTask(ipCode, "Implementation");
        completeTask(ipCode, "Implementation");
        startTask(ipCode, "Acceptance");
        completeTask(ipCode, "Acceptance");
        startTask(processCode, "Verification");
        completeTask(processCode, "Verification");
    }
}
