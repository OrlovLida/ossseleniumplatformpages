/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.pages.bpm;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.pages.BasePage;

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
    
}
