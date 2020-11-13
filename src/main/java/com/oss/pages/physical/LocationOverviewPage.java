package com.oss.pages.physical;

import com.oss.framework.components.contextactions.ButtonContainer;
import com.oss.framework.components.contextactions.OldActionsContainer;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.framework.widgets.tabswidget.TabWindowWidget;
import com.oss.framework.widgets.tabswidget.TabsInterface;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * @author Milena Miętkiewicz
 */

public class LocationOverviewPage extends BasePage {

    private static final String DEVICES_TABLE_DATA_ATTRIBUTE_NAME = "tableAppDevicesId";


    public LocationOverviewPage(WebDriver driver) {
        super(driver);
    }

    @Step("Click {buttonName} button")
    public void clickButton(String buttonName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        ButtonContainer.create(driver, wait)
                .callActionByLabel(buttonName);
    }

    @Step("Click button by label")
    public void clickButtonFromDeviceTabByLabel(String buttonLabel) {
        DelayUtils.waitForPageToLoad(driver, wait);
        OldTable table = OldTable.createByComponentDataAttributeName(driver, wait, DEVICES_TABLE_DATA_ATTRIBUTE_NAME);
        table.callActionByLabel(buttonLabel);
    }

    @Step("Select {tabName} tab")
    public LocationOverviewPage selectTab(String tabName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TabsInterface tabs = TabWindowWidget.create(driver, wait);
        tabs.selectTabByLabel(tabName);
        return this;
    }

    @Step("Filter and select {objectName} row")
    public LocationOverviewPage filterObject(String columnName, String objectName) {
        getTabTable().searchByAttributeWithLabel(columnName, Input.ComponentType.TEXT_FIELD, objectName);
        getTabTable().selectRowByAttributeValueWithLabel(columnName, objectName);
        return this;
    }

    //TODO: change to OldTable.createByComponentDataAttributeName(driver, wait, "data-attributename"); after OSSPHY-46774
    @Step("Click Edit Location icon")
    public void clickEditLocationIcon() {
        OldActionsContainer.createFromParent(driver, wait, driver.findElement(By.xpath(".//div[@class='OssWindow tabWindow']")))
                .callActionByLabel("Edit Location");
    }

    //TODO: change to OldTable.createByComponentDataAttributeName(driver, wait, "data-attributename"); after OSSPHY-46774
    @Step("Click Remove Location icon")
    public void clickRemoveLocationIcon() {
        OldActionsContainer.createFromParent(driver, wait, driver.findElement(By.xpath(".//div[@class='OssWindow tabWindow']")))
                .callActionByLabel("Remove Location");
    }

    //TODO: change data-attributename after OSSPHY-46774, it works only for Locations (temporary)
    public OldTable getTabTable() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return OldTable.createByComponentDataAttributeName(driver, wait, "tableAppLocationsId");
    }

    private OldTable getTabDevicesTable() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return OldTable.createByComponentDataAttributeName(driver, wait, DEVICES_TABLE_DATA_ATTRIBUTE_NAME);
    }

    public void selectDeviceFromTableByAttributeValueWithLabel(String attributeLabel, String value) {
        getTabDevicesTable().selectRowByAttributeValueWithLabel(attributeLabel, value);
    }
}