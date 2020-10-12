package com.oss.pages.platform;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;
import com.oss.framework.widgets.Wizard;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.framework.widgets.tablewidget.TableInterface;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

/**
 * @author Ewa Frączek
 */


public class OldInventoryViewPage extends BasePage {

    //TODO: remove
    private TableInterface mainTable;
    //TODO: remove
    private Wizard wizard;

    public OldInventoryViewPage(WebDriver driver) {
        super(driver);
    }

    private static final String INVENTORY_VIEW = "InventoryView";
    private static final String OLD_TABLE_WIDGET = "OSSTableWidget";

//    public TableInterface getTableWidget() {
//        if (mainTable == null) {
//            //TODO: remove  Widget.waitForWidget,
//            Widget.waitForWidget(wait, OLD_TABLE_WIDGET);
////            mainTable = OldTable.createByComponentDataAttributeName(driver, wait, INVENTORY_VIEW); to be replaced after fix of OSSWEB-8398
//            mainTable = OldTable.createByOssWindow(driver, wait);
//        }
//        return mainTable;
//    }

    public TableInterface getTableWidget() {
        Widget.waitForWidget(wait, OLD_TABLE_WIDGET);
        return OldTable.createByOssWindow(driver, wait);
    }

    public Wizard getWizard() {
        if (wizard == null) {
            wizard = Wizard.createWizard(driver, wait);
        }
        return wizard;
    }

    @Step("Select row")
    public void selectRow(String columnName, String value) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getTableWidget().selectRowByAttributeValueWithLabel(columnName, value);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Use context action")
    public void useContextAction(String groupId, String actionId) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getTableWidget().callAction(groupId, actionId);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Filter object name and select object name row")
    public OldInventoryViewPage filterObjectName(String objectName, String tableObjects) {
        TableInterface table = OldTable.createByComponentDataAttributeName(driver, wait, "table("+tableObjects+")");
        table.searchByAttributeWithLabel("Name", Input.ComponentType.TEXT_FIELD, objectName);
        table.selectRowByAttributeValueWithLabel("Name", objectName);
        return this;
    }

    @Step("Expand Show on button and select Location Overview from the drop-down list")
    public LocationOverviewPage expandShowOnLocationOverview() {
        ActionsContainer actionsContainer = ActionsContainer.createFromParent(driver.findElement(By.xpath(".//div[@class='OssWindow']")), driver, wait);
        actionsContainer.callAction("NAVIGATION", "OpenLocationOverviewAction");
        return new LocationOverviewPage(driver);
    }

}
