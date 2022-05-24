/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2021 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.pages.bpm.milestones;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.propertypanel.PropertyPanel;
import com.oss.framework.widgets.table.TableWidget;
import com.oss.pages.BasePage;

/**
 * @author Gabriela Kasza
 */
public class MilestoneViewPage extends BasePage {

    private static final String PROPERTY_PANEL_WIDGET = "PropertyPanelWidget";

    public MilestoneViewPage(WebDriver driver) {
        super(driver);
    }

    private static final String MILESTONE_TABLE_ID = "bpm_milestones_view_milestonesTableWidget";
    private static final String EDIT_MILESTONE_BUTTON = "editMilestonesContextAction";

    public static MilestoneViewPage goToMilestoneViewPage(WebDriver driver, String basicURL) {
        driver.get(String.format("%s/#/views/bpm/milestones" +
                "?perspective=LIVE", basicURL));
        return new MilestoneViewPage(driver);
    }

    public void selectMilestone(String name) {
        TableWidget milestoneTable = TableWidget.createById(driver, MILESTONE_TABLE_ID, wait);
        milestoneTable.searchByAttribute("name", Input.ComponentType.TEXT_FIELD, name);
        DelayUtils.waitForPageToLoad(driver, wait);
        milestoneTable.selectRow(0);
    }

    public void selectFirstMilestone() {
        DelayUtils.waitForPageToLoad(driver, wait);
        TableWidget milestoneTable = TableWidget.createById(driver, MILESTONE_TABLE_ID, wait);
        milestoneTable.selectRow(0);
    }

    public String getMilestoneAttribute(String attributeNameId) {
        selectFirstMilestone();
        DelayUtils.waitForPageToLoad(driver, wait);
        PropertyPanel propertyPanel = PropertyPanel.createById(driver, wait, PROPERTY_PANEL_WIDGET);
        return propertyPanel.getPropertyValue(attributeNameId);
    }

    public void enableMilestoneAttribute(String attributeLabel, String attributePath) {
        DelayUtils.waitForPageToLoad(driver, wait);
        PropertyPanel propertyPanel = PropertyPanel.createById(driver, wait, PROPERTY_PANEL_WIDGET);
        propertyPanel.enableAttributeByLabel(attributeLabel, attributePath);
    }

    public void enableMilestoneAttribute(String attributeLabel) {
        DelayUtils.waitForPageToLoad(driver, wait);
        PropertyPanel propertyPanel = PropertyPanel.createById(driver, wait, PROPERTY_PANEL_WIDGET);
        propertyPanel.enableAttributeByLabel(attributeLabel);
    }

    public void setAttributesConfiguration(String configurationName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        PropertyPanel propertyPanel = PropertyPanel.createById(driver, wait, PROPERTY_PANEL_WIDGET);
        propertyPanel.setAttributesConfiguration(configurationName);
    }

    public int getRowNumber(String value, String attributeNameLabel) {
        TableWidget milestoneTable = TableWidget.createById(driver, MILESTONE_TABLE_ID, wait);
        return milestoneTable.getRowNumber(value, attributeNameLabel);
    }

    public void callAction(String actionId) {
        TableWidget milestoneTable = TableWidget.createById(driver, MILESTONE_TABLE_ID, wait);
        milestoneTable.callAction(actionId);
    }

    public void addColumnAttribute(String columnAttributeLabel) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TableWidget milestoneTable = TableWidget.createById(driver, MILESTONE_TABLE_ID, wait);
        milestoneTable.enableColumnByLabel(columnAttributeLabel);
    }

    public void selectAll() {
        TableWidget milestoneTable = TableWidget.createById(driver, MILESTONE_TABLE_ID, wait);
        milestoneTable.selectAllRows();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void clearFilters() {
        TableWidget milestoneTable = TableWidget.createById(driver, MILESTONE_TABLE_ID, wait);
        milestoneTable.clearAllFilters();
        DelayUtils.waitForPageToLoad(driver, wait);
    }
}
