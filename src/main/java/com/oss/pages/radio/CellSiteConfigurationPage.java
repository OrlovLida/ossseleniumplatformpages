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

    private final OldTable tabTable = OldTable.createByComponentDataAttributeName(driver, wait, TAB_TABLE);
    private final TreeWidget tree = TreeWidget.createByDataAttributeName(driver, wait, "SiteHierarchyApp");

    @Step("Click plus icon and select {0} from the drop-down list")
    public void clickPlusIconAndSelectOption(String option) {
        DelayUtils.waitForPageToLoad(driver, wait);
        tabTable.callActionByLabelFromParent("ADD");
        DropdownList list = DropdownList.create(driver, wait);
        list.selectOption(option);
    }

    @Step("Select {0} tab")
    public void selectTab(String tabName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        tabTable.selectTabByLabel(tabName, TAB_TABLE);
    }

    @Step("Filter and select {1) row")
    public void filterObject(String columnName, String objectName) {
        tabTable.searchByAttributeWithLabel(columnName, Input.ComponentType.TEXT_FIELD, objectName);
        tabTable.selectRowByAttributeValueWithLabel(columnName, objectName);
    }

    @Step("Click Edit icon")
    public void clickEditIcon() {
        tabTable.callActionByLabelFromParent("Edit");
    }

    @Step("Click Remove icon")
    public void clickRemoveIcon() {
        tabTable.callActionByLabelFromParent("Delete");
    }

    @Step("Expand the tree and select eNodeB")
    public void expandTreeToENodeB(String locationType, String locationName, String eNodeBName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        tree.expandTreeRow(locationType);
        tree.expandTreeRow(locationName);
        tree.expandTreeRow("Base Stations");
        tree.selectTreeRow(eNodeBName);
    }

    @Step("Expand the tree and select location")
    public void expandTreeToLocation(String locationType, String locationName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        tree.expandTreeRow(locationType);
        tree.selectTreeRow(locationName);
    }

}