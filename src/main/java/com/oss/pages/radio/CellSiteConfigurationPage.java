package com.oss.pages.radio;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.framework.widgets.treewidget.TreeWidget;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

/**
 * @author Milena Miętkiewicz
 */

public class CellSiteConfigurationPage extends BasePage {

    private static final String TAB_TABLE = "TableTabsApp";

    public CellSiteConfigurationPage(WebDriver driver) {
        super(driver);
    }

    private OldTable tabTable = OldTable.createByComponentDataAttributeName(driver, wait, TAB_TABLE);
    private TreeWidget tree = TreeWidget.createByDataAttributeName(driver, wait, "SiteHierarchyApp");

    @Step("Click plus icon")
    public CellSiteConfigurationPage clickPlusIcon() {
        DelayUtils.waitForPageToLoad(driver, wait);
        tabTable.callActionByLabel("ADD");
        return this;
    }

    @Step("Select Create eNodeB from the drop-down list")
    public ENodeBWizardPage selectCreateENodeB() {
        DelayUtils.waitForPageToLoad(driver, wait);
        DropdownList list = DropdownList.create(driver, wait);
        list.selectOption("Create eNodeB");
        return new ENodeBWizardPage(driver);
    }

    @Step("Select Create Cell 4G from the drop-down list")
    public Cell4GWizardPage selectCreateCell4G() {
        DelayUtils.waitForPageToLoad(driver, wait);
        DropdownList list = DropdownList.create(driver, wait);
        list.selectOption("Create Cell 4G");
        return new Cell4GWizardPage(driver);
    }

    @Step("Select Base Stations Tab")
    public CellSiteConfigurationPage selectBaseStationsTab() {
        DelayUtils.waitForPageToLoad(driver, wait);
        tabTable.selectTabByLabel("Base Stations", TAB_TABLE);
        return this;
    }

    @Step("Select Cells Tab")
    public CellSiteConfigurationPage selectCellsTab() {
        DelayUtils.waitForPageToLoad(driver, wait);
        tabTable.selectTabByLabel("Cells", TAB_TABLE);
        return this;
    }

    @Step("Filter object and select object row")
    public void filterObject(String columnName, String objectName) {
        OldTable table = OldTable.createByComponentDataAttributeName(driver, wait, "table(" + TAB_TABLE + ")");
        table.searchByAttributeWithLabel(columnName, Input.ComponentType.TEXT_FIELD, objectName);
        table.selectRowByAttributeValueWithLabel(columnName, objectName);
    }

    @Step("Click Edit icon")
    public void clickEditIcon() {
        tabTable.callActionByLabel("Edit");
    }

    @Step("Click Remove icon")
    public void clickRemoveIcon() {
        tabTable.callActionByLabel("Delete");
    }

    @Step("Expand the tree and select eNodeB")
    public CellSiteConfigurationPage expandTreeToENodeB(String locationType, String locationName, String eNodeBName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        tree.expandTreeRow(locationType);
        tree.expandTreeRow(locationName);
        tree.expandTreeRow("Base Stations");
        tree.selectTreeRow(eNodeBName);
        return this;
    }

    @Step("Expand the tree and select location")
    public CellSiteConfigurationPage expandTreeToLocation(String locationType, String locationName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        tree.expandTreeRow(locationType);
        tree.selectTreeRow(locationName);
        return this;
    }
}