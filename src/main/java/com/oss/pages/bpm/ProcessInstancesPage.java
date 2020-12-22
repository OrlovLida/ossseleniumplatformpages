/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.pages.bpm;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.framework.widgets.tablewidget.TableInterface;
import com.oss.pages.BasePage;
import org.openqa.selenium.WebDriver;

/**
 * @author Gabriela Kasza
 */
public class ProcessInstancesPage extends BasePage {
    
    private static final String PROCESS_VIEW = "bpm_processes_view_processes";
    
    protected ProcessInstancesPage(WebDriver driver) {
        super(driver);
    }
    
    public static ProcessInstancesPage goToProcessInstancesPage(WebDriver driver, String basicURL) {
        driver.get(String.format("%s/#/view/bpm/processes" +
                "?perspective=LIVE", basicURL));
        
        return new ProcessInstancesPage(driver);
    }
    
    public void selectPredefinedFilter(String filterName) {
        OldTable processTable = OldTable.createByComponentDataAttributeName(driver, wait, PROCESS_VIEW);
        processTable.selectPredefinedFilter(filterName);
    }
    
    public String getProcessStatus(String code) {
        OldTable processTable = OldTable.createByComponentDataAttributeName(driver, wait, PROCESS_VIEW);
        processTable.searchByAttributeWithLabel("Code", Input.ComponentType.TEXT_FIELD, code);
        int index = processTable.getRowNumber(code, "Code");
        return processTable.getValueCell(index, "Status");
    }
    
    public String getProcessName(String code) {
        OldTable processTable = OldTable.createByComponentDataAttributeName(driver, wait, PROCESS_VIEW);
        processTable.searchByAttributeWithLabel("Code", Input.ComponentType.TEXT_FIELD, code);
        int index = processTable.getRowNumber(code, "Code");
        return processTable.getValueCell(index, "Name");
    }
    
    public void findProcess(String processCode) {
        TableInterface table = OldTable.createByComponentDataAttributeName(driver, wait, PROCESS_VIEW);
        DelayUtils.waitForPageToLoad(driver, wait);
        table.searchByAttributeWithLabel("Code", Input.ComponentType.TEXT_FIELD, processCode);
        DelayUtils.waitForPageToLoad(driver, wait);
        table.doRefreshWhileNoData(10000, "Reload table");
        table.selectRowByAttributeValueWithLabel("Code", processCode);
    }
    
    public void completeNRP(String basicURL, String processCode) {
        String processNRPName = getProcessName(processCode);
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, wait, basicURL);
        tasksPage.startTask(processCode, "Low Level Planning");
        tasksPage.completeTask(processCode, "Low Level Planning");
        tasksPage.startTask(processCode, "Ready for Integration");
        tasksPage.completeTask(processCode, "Ready for Integration");
        tasksPage.showCompletedTasks();
        tasksPage.findTask(processCode, "Ready for Integration");
        DelayUtils.sleep(3000);
        String ipCode = tasksPage.getIPCodeByProcessName("IP auto created from " + processNRPName);
        tasksPage.startTask(ipCode, "Scope definition");
        tasksPage.completeTask(ipCode, "Scope definition");
        tasksPage.startTask(ipCode, "Implementation");
        tasksPage.completeTask(ipCode, "Implementation");
        tasksPage.startTask(ipCode, "Acceptance");
        tasksPage.completeTask(ipCode, "Acceptance");
        tasksPage.startTask(processCode, "Verification");
        tasksPage.completeTask(processCode, "Verification");
    }
}
