package com.oss.pages.radio;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.prompts.ConfirmationBox;
import com.oss.framework.prompts.ConfirmationBoxInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.framework.widgets.treewidget.TreeWidget;
import com.oss.pages.BasePage;
import com.oss.pages.physical.LocationOverviewPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

import static com.oss.framework.components.inputs.Input.ComponentType.COMBOBOX;


/**
 * @author Milena Miętkiewicz
 */

public class CellSiteConfigurationPage extends BasePage {
    private static final String TAB_TABLE_DATA_ATTRIBUTE_NAME = "TableTabsApp";
    private static final String TREE_DATA_ATTRIBUTE_NAME = "SiteHierarchyApp";

    public CellSiteConfigurationPage(WebDriver driver) {
        super(driver);
    }

    @Step("Click plus icon and select {option} from the drop-down list")
    public void clickPlusIconAndSelectOption(String option) {
        useTableContextActionByLabel("ADD");
        DelayUtils.waitForPageToLoad(driver, wait);
        DropdownList.create(driver, wait).selectOption(option);
    }

    @Step("Select {tabName} tab")
    public CellSiteConfigurationPage selectTab(String tabName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getTabTable().selectTabByLabel(tabName, TAB_TABLE_DATA_ATTRIBUTE_NAME);
        return this;
    }

    @Step("Filter and select {objectName} row")
    public CellSiteConfigurationPage filterObject(String columnName, String objectName) {
        getTabTable().searchByAttributeWithLabel(columnName, Input.ComponentType.TEXT_FIELD, objectName);
        selectRowByAttributeValueWithLabel(columnName, objectName);
        return this;
    }

    @Step("Select {label} row")
    public CellSiteConfigurationPage selectRowByAttributeValueWithLabel(String attribute, String label) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getTabTable().selectRowByAttributeValueWithLabel(attribute, label);
        DelayUtils.waitForPageToLoad(driver, wait);
        return this;
    }

    @Step("Click Edit icon")
    public void clickEditIcon() {
        getTabTable().callActionByLabel("Edit");
    }

    @Step("Remove object")
    public void removeObject() {
        getTabTable().callActionByLabel("Delete");
        DelayUtils.waitForPageToLoad(driver, wait);
        ConfirmationBoxInterface prompt = ConfirmationBox.create(driver, wait);
        prompt.clickButtonByLabel("Delete");
    }

    @Step("Expand the tree and select Base Station")
    public CellSiteConfigurationPage expandTreeToBaseStation(String locationType, String locationName, String baseStation) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getTree().expandTreeRow(locationType);
        getTree().expandTreeRow(locationName);
        getTree().expandTreeRow("Base Stations");
        getTree().selectTreeRow(baseStation);
        return this;
    }

    @Step("Expand the tree and select location")
    public CellSiteConfigurationPage expandTreeToLocation(String locationType, String locationName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getTree().expandTreeRow(locationType);
        getTree().selectTreeRow(locationName);
        return this;
    }

    @Step("Expand the tree and select Cell")
    public CellSiteConfigurationPage expandTreeToCell(String locationType, String locationName, String baseStationName, String cellName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getTree().expandTreeRow(locationType);
        getTree().expandTreeRow(locationName);
        getTree().expandTreeRow("Base Stations");
        getTree().expandTreeRow(baseStationName);
        getTree().selectTreeRow(cellName);
        return this;
    }

    @Step("Select tree row")
    public void selectTreeRow(String treeRowName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getTree().selectTreeRow(treeRowName);
    }

    @Step("Use tree context action")
    public void useTreeContextAction(String groupId, String actionId) {
        getTree().callActionById(groupId, actionId);
    }

    @Step("Use table context action")
    public void useTableContextActionById(String id) {
        getTabTable().callAction(id);
    }

    @Step("Use table context action")
    public void useTableContextActionByLabel(String actionLabel) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getTabTable().callActionByLabel(actionLabel);
    }

    @Step("Select trail type")
    public void selectTrailType(String trailType) {
        Wizard wizard = Wizard.createByComponentId(driver, wait, "Popup");
        Input input = wizard.getComponent("trailType", COMBOBOX);
        input.setSingleStringValue(trailType);
        wizard.clickAccept();
    }

    @Step("Get row number for object with {attributeLabel} {value}")
    public int getRowNumber(String attributeLabel, String value) {
        return getTabTable().getRowNumber(value, attributeLabel);
    }

    @Step("Get {attributeLabel} value for row number {rowNumber}")
    public String getValueByRowNumber(String attributeLabel, int rowNumber) {
        return getTabTable().getCellValue(rowNumber, attributeLabel);
    }

    @Step("Get row count in a table")
    public int getRowCount(String attributeLabel) {
        return getTabTable().getNumberOfRowsInTable(attributeLabel);
    }

    public boolean hasNoData() {
        return getTabTable().hasNoData();
    }

    public TreeWidget getTree() {
        return TreeWidget.createByDataAttributeName(driver, wait, TREE_DATA_ATTRIBUTE_NAME);
    }

    public OldTable getTabTable() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return OldTable.createByComponentDataAttributeName(driver, wait, TAB_TABLE_DATA_ATTRIBUTE_NAME);
    }
}