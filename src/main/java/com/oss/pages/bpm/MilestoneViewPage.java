/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2021 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.pages.bpm;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.propertypanel.PropertyPanel;
import com.oss.framework.widgets.tablewidget.TableWidget;
import com.oss.pages.BasePage;

/**
 * @author Gabriela Kasza
 */
public class MilestoneViewPage extends BasePage {
    public MilestoneViewPage(WebDriver driver) {
        super(driver);
    }

    private final static String MILESTONE_TABLE = "bpm_milestones_view_milestonesTableWidget";
    private final static String EDIT_MILESTONE_BUTTON = "editMilestonesContextAction";

    public static MilestoneViewPage goToMilestoneViewPage(WebDriver driver, String basicURL) {
        driver.get(String.format("%s/#/views/bpm/milestones" +
                "?perspective=LIVE", basicURL));
        return new MilestoneViewPage(driver);
    }
    
    public void selectMilestone(String name) {
        TableWidget milestoneTable = TableWidget.createById(driver, MILESTONE_TABLE, wait);
        milestoneTable.searchByAttribute("name", Input.ComponentType.TEXT_FIELD,name);
        DelayUtils.waitForPageToLoad(driver,wait);
        milestoneTable.selectRow(0);
    }

    public String getValuePropertyPanel(String attributeNameId, int row){
        DelayUtils.waitForPageToLoad(driver,wait);
        TableWidget milestoneTable = TableWidget.createById(driver, MILESTONE_TABLE, wait);
        milestoneTable.selectRow(row);
        DelayUtils.waitForPageToLoad(driver,wait);
        PropertyPanel propertyPanel = PropertyPanel.createById(driver, wait, "PropertyPanelWidget");
        return propertyPanel.getPropertyValue(attributeNameId);
    }

    public int getRowNumber(String value, String attributeNameLabel){
        TableWidget milestoneTable = TableWidget.createById(driver, MILESTONE_TABLE, wait);
        return milestoneTable.getRowNumber(value,attributeNameLabel);
    }

    public void callAction(String actionId){
        TableWidget milestoneTable = TableWidget.createById(driver, MILESTONE_TABLE, wait);
        milestoneTable.callAction(actionId);
    }
}
