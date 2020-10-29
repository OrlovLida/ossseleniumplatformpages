package com.oss.pages.radio;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.prompts.ConfirmationBox;
import com.oss.framework.prompts.ConfirmationBoxInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.framework.widgets.tablewidget.TableInterface;
import com.oss.framework.widgets.treewidget.TreeWidget;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

/**
 * @author Milena Miętkiewicz
 */

public class CellSiteConfigurationPage extends BasePage {
    private static final String TAB_TABLE_DATA_ATTRIBUTE_NAME = "TableTabsApp";
    private static final String TREE_CLASS = "TreeView";
    private static final String DEVICES_TABLE = "DevicesTableApp";

    public CellSiteConfigurationPage(WebDriver driver) {
        super(driver);
    }

    @Step("Click plus icon and select {option} from the drop-down list")
    public void clickPlusIconAndSelectOption(String option) {
        getTabTable().callActionByLabel("ADD");
        DropdownList list = DropdownList.create(driver, wait);
        list.selectOption(option);
    }

    @Step("Select {tabName} tab")
    public CellSiteConfigurationPage selectTab(String tabName) {
        getTabTable().selectTabByLabel(tabName, TAB_TABLE_DATA_ATTRIBUTE_NAME);
        return this;
    }

    @Step("Filter and select {objectName} row")
    public CellSiteConfigurationPage filterObject(String columnName, String objectName) {
        OldTable tabTable = OldTable.createByComponentDataAttributeName(driver, wait, TAB_TABLE_DATA_ATTRIBUTE_NAME);
        tabTable.searchByAttributeWithLabel(columnName, Input.ComponentType.TEXT_FIELD, objectName);
        tabTable.selectRowByAttributeValueWithLabel(columnName, objectName);
        return this;
    }

    @Step("Click Edit icon")
    public void clickEditIcon() {
        getTabTable().callActionByLabel("Edit");
    }

    @Step("Click Remove icon")
    public void clickRemoveIcon() {
        getTabTable().callActionByLabel("Delete");
        DelayUtils.waitForPageToLoad(driver, wait);
        ConfirmationBoxInterface prompt = ConfirmationBox.create(driver, wait);
        prompt.clickButtonByLabel("Delete");
    }

    @Step("Expand the tree and select eNodeB")
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

    public OldTable getTabTable() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return OldTable.createByComponentDataAttributeName(driver, wait, TAB_TABLE_DATA_ATTRIBUTE_NAME);
    }

    public TreeWidget getTree() {
        Widget.waitForWidget(wait, TREE_CLASS);
        return TreeWidget.createByClass(driver, TREE_CLASS, wait);
    }
}

