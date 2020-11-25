package com.oss.pages.physical;

import com.oss.framework.components.contextactions.ButtonContainer;
import com.oss.framework.components.contextactions.OldActionsContainer;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.prompts.ConfirmationBox;
import com.oss.framework.prompts.ConfirmationBoxInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.framework.widgets.tablewidget.TableInterface;
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

    //TODO: filterLocationsObject() is a temporary name which will be changed to filterObject() and made generic after OSSPHY-46774
    //TODO: getLocationsTabTable is a temporary name until OSSPHY-46774 fix comes, it will be changed to getTabTable and made generic
    @Step("Filter and select {objectName} row")
    public LocationOverviewPage filterLocationsObject(String columnName, String objectName) {
        getLocationsTabTable().searchByAttributeWithLabel(columnName, Input.ComponentType.TEXT_FIELD, objectName);
        getLocationsTabTable().selectRowByAttributeValueWithLabel(columnName, objectName);
        return this;
    }

    //TODO: getCoolingZonesTabTable is a temporary name until OSSPHY-46774 fix comes, it will be changed to getTabTable and made generic
    @Step("Filter and select {objectName} row")
    public LocationOverviewPage filterCoolingObject(String columnName, String objectName) {
        getCoolingZonesTabTable().searchByAttributeWithLabel(columnName, Input.ComponentType.TEXT_FIELD, objectName);
        getCoolingZonesTabTable().selectRowByAttributeValueWithLabel(columnName, objectName);
        return this;
    }

    //TODO: getDevicesTabTable is a temporary name until OSSPHY-46774 fix comes, it will be changed to getTabTable and made generic
    @Step("Filter and select {objectName} row")
    public LocationOverviewPage filterDevicesObject(String columnName, String objectName) {
        getDevicesTabTable().searchByAttributeWithLabel(columnName, Input.ComponentType.TEXT_FIELD, objectName);
        getDevicesTabTable().selectRowByAttributeValueWithLabel(columnName, objectName);
        return this;
    }

    @Step("Filter and select {objectName} row")
    public LocationOverviewPage filterPowerManagementObject(String columnName, String objectName) {
        //TODO: getPowerManagementTabTable is a temporary name until OSSPHY-46774 fix comes, it will be changed to getTabTable and made generic
        getPowerManagementTabTable().searchByAttributeWithLabel(columnName, Input.ComponentType.TEXT_FIELD, objectName);
        getPowerManagementTabTable().selectRowByAttributeValueWithLabel(columnName, objectName);
        return this;
    }

    @Step("Click Action By Id")
    public void clickActionById(String id) {
        TabsInterface actions = TabWindowWidget.create(driver, wait);
        actions.callActionById(id);
    }

    //TODO: change to OldTable.createByComponentDataAttributeName(driver, wait, "data-attributename"); after OSSPHY-46774
    @Step("Click icon by label")
    public void clickIconByLabel(String label) {
        OldActionsContainer.createFromParent(driver, wait, driver.findElement(By.xpath(".//div[@class='OssWindow tabWindow']")))
                .callActionByLabel(label);
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

    //TODO: change to OldTable.createByComponentDataAttributeName(driver, wait, "data-attributename"); after OSSPHY-46774
    @Step("Click Remove Device")
    public void clickRemoveDevice() {
        OldActionsContainer.createFromParent(driver, wait, driver.findElement(By.xpath(".//div[@class='OssWindow tabWindow']")))
                .callActionByLabel("Delete Element");
    }

    //TODO: change to OldTable.createByComponentDataAttributeName(driver, wait, "data-attributename"); after OSSPHY-46774
    @Step("Click Delete Cooling Zones")
    public void clickDeleteCoolingZone() {
        OldActionsContainer.createFromParent(driver, wait, driver.findElement(By.xpath(".//div[@class='OssWindow tabWindow']")))
                .callActionByLabel("Delete Cooling Zone");
    }

    //TODO: change method name to getTabTable and data-attributename after OSSPHY-46774 (EVERY TEST USING THIS METHOD SHOULD BE UPDATED)
    public OldTable getLocationsTabTable() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return OldTable.createByComponentDataAttributeName(driver, wait, "tableAppLocationsId");
    }

    //TODO: this method is temporary and should be deleted after getTabTable() method works for every tab (EVERY TEST USING THIS METHOD SHOULD BE UPDATED)
    public OldTable getCoolingZonesTabTable() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return OldTable.createByComponentDataAttributeName(driver, wait, "tableAppCoolingZonesId");
    }

    //TODO: this method is temporary and should be deleted after getTabTable() method works for every tab (EVERY TEST USING THIS METHOD SHOULD BE UPDATED)
    public OldTable getDevicesTabTable() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return OldTable.createByComponentDataAttributeName(driver, wait, "tableAppDevicesId");
    }

    //TODO: this method is temporary and should be deleted after getTabTable() method works for every tab (EVERY TEST USING THIS METHOD SHOULD BE UPDATED)
    public OldTable getPowerManagementTabTable() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return OldTable.createByComponentDataAttributeName(driver, wait, "tableAppPowerManagementId");
    }

    //TODO: this method is temporary and should be deleted after getTabTable() method works for every tab (EVERY TEST USING THIS METHOD SHOULD BE UPDATED)
    private OldTable getTabDevicesTable() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return OldTable.createByComponentDataAttributeName(driver, wait, DEVICES_TABLE_DATA_ATTRIBUTE_NAME);
    }

    public void selectDeviceFromTableByAttributeValueWithLabel(String attributeLabel, String value) {
        getTabDevicesTable().selectRowByAttributeValueWithLabel(attributeLabel, value);
    }
}