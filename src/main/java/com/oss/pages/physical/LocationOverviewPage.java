package com.oss.pages.physical;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.contextactions.ButtonContainer;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.framework.widgets.tabswidget.TabWindowWidget;
import com.oss.framework.widgets.tabswidget.TabsInterface;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

/**
 * @author Milena Miętkiewicz
 */

public class LocationOverviewPage extends BasePage {

    public LocationOverviewPage(WebDriver driver) {
        super(driver);
    }

    @Step("Select {tabName} tab")
    public LocationOverviewPage selectTab(String tabName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TabsInterface tabs = TabWindowWidget.create(driver, wait);
        tabs.selectTabByLabel(tabName);
        return this;
    }

    @Step("Click {buttonName} button")
    public void clickButton(String buttonName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        ButtonContainer.create(driver, wait)
                .callActionByLabel(buttonName);
    }

    @Step("Click button by label")
    public void clickButtonByLabelInSpecificTab(TabName tabName, String actionLabel) {
        getTabTable(tabName).callActionByLabel(actionLabel);
    }

    @Step("Filter and select {objectName} row")
    public LocationOverviewPage filterObjectInSpecificTab(TabName tabName, String columnName, String objectName) {
        getTabTable(tabName).searchByAttributeWithLabel(columnName, Input.ComponentType.TEXT_FIELD, objectName);
        getTabTable(tabName).selectRowByAttributeValueWithLabel(columnName, objectName);
        return this;
    }

    @Step("Select object")
    public void selectObjectInSpecificTab(TabName tabName, String attributeLabel, String value) {
        getTabTable(tabName).selectRowByAttributeValueWithLabel(attributeLabel, value);
    }

    @Step("Get row number for specific value in column")
    public int getRowNumber(TabName tabName, String attributeLabel, String value) {
        return getTabTable(tabName).getRowNumber(value, attributeLabel);
    }

    @Step("Get value for specific row number in column")
    public String getValueByRowNumber(TabName tabName, String attributeLabel, int rowNumber) {
        return getTabTable(tabName).getValueCell(rowNumber, attributeLabel);
    }

    public OldTable getTabTable(TabName tabName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        return OldTable.createByComponentDataAttributeName(driver, wait, tabName.getTab());
    }

    public enum TabName {
        LOCATIONS("tableAppLocationsId"),
        DEVICES("tableAppDevicesId"),
        COOLING_ZONES("tableAppCoolingZonesId"),
        POWER_MANAGEMENT("tableAppPowerManagementId"),
        POP("tabPopId");

        private final String tab;

        TabName(String tab) {
            this.tab = tab;
        }

        public String getTab() {
            return tab;
        }
    }
}