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
        DelayUtils.waitForPageToLoad(driver, wait);
        getTabTable().callActionByLabel("ADD");
        DelayUtils.waitForPageToLoad(driver, wait);
        DropdownList.create(driver, wait)
                .selectOption(option);
    }

    @Step("Click plus icon by label")
    public void clickPlusIconByLabel(String label) {
        getTabTable().callActionByLabel(label);
    }

    @Step("Select {tabName} tab")
    public CellSiteConfigurationPage selectTab(String tabName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getTabTable().selectTabByLabel(tabName, TAB_TABLE_DATA_ATTRIBUTE_NAME);
        return this;
    }

    @Step("Filter and select {objectName} row")
    public CellSiteConfigurationPage filterObject(String columnName, String objectName) {
        OldTable tabTable = OldTable.createByComponentDataAttributeName(driver, wait, TAB_TABLE_DATA_ATTRIBUTE_NAME);
        tabTable.searchByAttributeWithLabel(columnName, Input.ComponentType.TEXT_FIELD, objectName);
        DelayUtils.waitForPageToLoad(driver, wait);
        tabTable.selectRowByAttributeValueWithLabel(columnName, objectName);
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

    @Step("Select trail type")
    public void selectTrailType(String trailType) {
        Wizard wizard = Wizard.createByComponentId(driver, wait, "Popup");
        Input input = wizard.getComponent("trailType", COMBOBOX);
        input.setSingleStringValue(trailType);
        wizard.clickAccept();
    }

    public OldTable getTabTable() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return OldTable.createByComponentDataAttributeName(driver, wait, TAB_TABLE_DATA_ATTRIBUTE_NAME);
    }

    public void selectResource(String name) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getTabTable().selectRowByAttributeValueWithLabel("Name", name);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public TreeWidget getTree() {
        return TreeWidget.createByDataAttributeName(driver, wait, TREE_DATA_ATTRIBUTE_NAME);
    }

    public void selectRowByAttributeValueWithLabel(String attribute, String label) {
        OldTable tabTable = OldTable.createByComponentDataAttributeName(driver, wait, TAB_TABLE_DATA_ATTRIBUTE_NAME);
        tabTable.selectRowByAttributeValueWithLabel(attribute, label);
    }
}