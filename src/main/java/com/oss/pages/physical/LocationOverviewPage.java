package com.oss.pages.physical;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.contextactions.ButtonContainer;
import com.oss.framework.components.contextactions.OldActionsContainer;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.components.prompts.ConfirmationBoxInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.widgets.tabs.TabsInterface;
import com.oss.framework.widgets.tabs.TabsWidget;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

/**
 * @author Milena Miętkiewicz
 */

public class LocationOverviewPage extends BasePage {

    private static final String REFRESH_BUTTON_ID = "refresh-table-action";
    private static final String TAB_ID = "windowBottomId";

    public LocationOverviewPage(WebDriver driver) {
        super(driver);
    }

    @Step("Select {tabName} tab")
    public LocationOverviewPage selectTab(String tabName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getTabs().selectTabByLabel(tabName);
        return this;
    }

    @Step("Click {buttonName} button")
    public void clickButton(String buttonName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        ButtonContainer.create(driver, wait)
                .callActionByLabel(buttonName);
    }

    @Step("Click {actionLabel} in specific tab")
    public void clickButtonByLabelInSpecificTab(TabName tabName, String actionLabel) {
        getTabTable(tabName).callActionByLabel(actionLabel);
    }

    @Step("Click {actionId} in specific tab")
    public void clickActionById(TabName tabName, String actionId) {
        getTabTable(tabName).callAction(actionId);
    }

    @Step("Click refresh in specific tab")
    public void clickRefreshInSpecificTab(TabName tabName) {
        getTabTable(tabName).callAction(OldActionsContainer.KEBAB_GROUP_ID, REFRESH_BUTTON_ID);
    }

    @Step("Filter and select object with {columnName} {objectName}")
    public LocationOverviewPage filterObjectInSpecificTab(TabName tabName, String columnName, String objectName) {
        getTabTable(tabName).searchByAttributeWithLabel(columnName, Input.ComponentType.TEXT_FIELD, objectName);
        getTabTable(tabName).selectRowByAttributeValueWithLabel(columnName, objectName);
        return this;
    }

    @Step("Select object with {attributeLabel} {value}")
    public void selectObjectInSpecificTab(TabName tabName, String attributeLabel, String value) {
        getTabTable(tabName).selectRowByAttributeValueWithLabel(attributeLabel, value);
    }

    @Step("Get {attributeLabel} value for row number {rowNumber}")
    public String getValueByRowNumber(TabName tabName, String attributeLabel, int rowNumber) {
        return getTabTable(tabName).getCellValue(rowNumber, attributeLabel);
    }

    @Step("Click {label} in confirmation box")
    public void clickButtonInConfirmationBox(String label) {
        ConfirmationBoxInterface prompt = ConfirmationBox.create(driver, wait);
        prompt.clickButtonByLabel(label);
    }

    public OldTable getTabTable(TabName tabName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        return OldTable.createById(driver, wait, tabName.getTab());
    }

    private TabsInterface getTabs(){
        return TabsWidget.createById(driver, wait, TAB_ID);
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
