/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.pages.bpm;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.contextactions.ButtonContainer;
import com.oss.framework.prompts.ConfirmationBox;
import com.oss.framework.prompts.ConfirmationBoxInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.framework.widgets.tablewidget.TableInterface;
import com.oss.framework.widgets.tabswidget.OldTabs;
import com.oss.framework.widgets.tabswidget.TabsInterface;
import com.oss.pages.BasePage;
import com.oss.pages.dms.AttachFileWizardPage;

/**
 * @author Gabriela Kasza
 */
public class TasksPage extends BasePage {
    public static TasksPage goToTasksPage(WebDriver driver, String basicURL){
        driver.get(String.format("%s/#/view/bpm/tasks", basicURL));
        return new TasksPage(driver);
    }
    private String TABLE_TASKS = "bpm_task_view_task-table";
    private String TABS_TASKS_VIEW = "bpm_task_view_tabs-container";
    private String ATTACH_FILE_BUTTON = "attachmentManagerBusinessView_topCommonButtons-1";
    private String FORM_TAB_ID ="0";
    private String ATTACHMENT_TAB_ID= "3";

    protected TasksPage(WebDriver driver) {
        super(driver);
    }
    public void findTask(String processCode, String taskName){
        TableInterface table = OldTable.createByComponentDataAttributeName(driver, wait, TABLE_TASKS);
        DelayUtils.waitForPageToLoad(driver, wait);
        table.searchByAttributeWithLabel("Process Code", Input.ComponentType.TEXT_FIELD,processCode);
        DelayUtils.waitForPageToLoad(driver, wait);
        table.searchByAttributeWithLabel("Name", Input.ComponentType.TEXT_FIELD,taskName);
        table.refreshUntilNoData(10000, "Reload table");
        //DelayUtils.sleep(1000);
        table.selectRowByAttributeValueWithLabel("Process Code",processCode);

    }
    public void startTask(String processCode, String taskName){
        findTask(processCode,taskName);
        DelayUtils.waitForPageToLoad(driver, wait);
        actionTask("Start task");

    }
    public void completeTask(String processCode, String taskName){
        findTask(processCode,taskName);
        DelayUtils.waitForPageToLoad(driver, wait);
        actionTask("Complete task");
    }
    public void setupIntegration(String processCode){
        findTask(processCode,"Ready for Integration");
        DelayUtils.waitForPageToLoad(driver, wait);
        TabsInterface tabs= OldTabs.createById(driver,wait, TABS_TASKS_VIEW);
        tabs.callActionByLabel("Setup Integration");
    }
    public void addFile(String processCode, String taskName, String filePath){
        findTask(processCode,taskName);
        TabsInterface tabs= OldTabs.createById(driver,wait, TABS_TASKS_VIEW);
        DelayUtils.waitForPageToLoad(driver,wait);
        tabs.selectTabById(ATTACHMENT_TAB_ID);
        ButtonContainer action = ButtonContainer.create(driver, wait);
        action.callActionById(ATTACH_FILE_BUTTON);
        AttachFileWizardPage attachFileWizardPage = new AttachFileWizardPage(driver);
        attachFileWizardPage.selectRadioButton("Upload anyway");
        attachFileWizardPage.attachFile(filePath);
        DelayUtils.sleep();
        attachFileWizardPage.nextButton();
        attachFileWizardPage.acceptButton();
        DelayUtils.waitForPageToLoad(driver, wait);
    }
    public void selectTab(String tabLabel){
        TabsInterface tabs= OldTabs.createById(driver,wait, TABS_TASKS_VIEW);
        tabs.selectTabByLabel(tabLabel);
    }

    private void actionTask(String actionLabel){
        TabsInterface tabs= OldTabs.createById(driver,wait, TABS_TASKS_VIEW);
        tabs.selectTabById(FORM_TAB_ID);
        DelayUtils.waitForPageToLoad(driver,wait);
        tabs.callActionByLabel(actionLabel);
        ConfirmationBoxInterface prompt= ConfirmationBox.create(driver, wait);
        prompt.clickButtonByLabel("Proceed");
    }
}
