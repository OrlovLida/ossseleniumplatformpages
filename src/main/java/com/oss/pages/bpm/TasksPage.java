/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.pages.bpm;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.Input;
import com.oss.framework.prompts.ConfirmationBox;
import com.oss.framework.prompts.ConfirmationBoxInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.framework.widgets.tablewidget.TableInterface;
import com.oss.framework.widgets.tablewidget.TableWidget;
import com.oss.framework.widgets.tabswidget.OldTabs;
import com.oss.framework.widgets.tabswidget.TabsInterface;
import com.oss.pages.BasePage;

/**
 * @author Gabriela Kasza
 */
public class TasksPage extends BasePage {
    public static TasksPage goToTasksPage(WebDriver driver, String basicURL){
        driver.get(String.format("%s/#/view/bpm/tasks", basicURL));
        return new TasksPage(driver);
    }

    protected TasksPage(WebDriver driver) {
        super(driver);
    }
    public void findTask(String processCode, String taskName){
       TableInterface table = OldTable.createByWindowTitle(driver, wait, "Tasks");

        table.searchByAttributeWithLabel("Name", Input.ComponentType.TEXT_FIELD,taskName);
        table.searchByAttributeWithLabel("Process Code", Input.ComponentType.TEXT_FIELD,processCode);
        table.selectRowByAttributeValueWithLabel("Process Code",processCode);

    }
    public void startTask(String processCode, String taskName){
        findTask(processCode,taskName);
        actionTask("Start task");

    }
    public void completeTask(String processCode, String taskName){
        findTask(processCode,taskName);
        actionTask("Complete task");
    }
    private void actionTask(String actionLabel){
        TabsInterface tabs= OldTabs.create(driver,wait);
        tabs.selectTabByLabel("Form");
        tabs.callActionByLabel(actionLabel);
        ConfirmationBoxInterface prompt= ConfirmationBox.create(driver, wait);
        prompt.clickButtonByLabel("Proceed");
        wait.until(ExpectedConditions.invisibilityOf(driver.findElement(By.xpath("//div[contains(@class,'OssWindow newPrompt')]"))));
    }
}
