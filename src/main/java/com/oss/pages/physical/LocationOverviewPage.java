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

    public LocationOverviewPage(WebDriver driver) {
        super(driver);
    }

    @Step("Click {buttonName} button")
    public void clickButton(String buttonName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getButtons().callActionByLabel(buttonName);
    }

    @Step("Select {tabName} tab")
    public LocationOverviewPage selectTab(String tabName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TabsInterface tabs = TabWindowWidget.create(driver, wait);
        tabs.selectTabByLabel(tabName);
        return this;
    }

    //TODO: remove createByComponentDataAttributeName after OSSPHY-46774
    //it works only for Locations (temporary)
    @Step("Filter and select {objectName} row")
    public LocationOverviewPage filterObject(String columnName, String objectName) {
        OldTable tabTable = OldTable.createByComponentDataAttributeName(driver, wait, "tableAppLocationsId");
        tabTable.searchByAttributeWithLabel(columnName, Input.ComponentType.TEXT_FIELD, objectName);
        tabTable.selectRowByAttributeValueWithLabel(columnName, objectName);
        return this;
    }

    @Step("Click Edit Location icon")
    public void clickEditLocationIcon() {
        getTabTable().callActionByLabel("Edit Location");
    }

    @Step("Click Remove Location icon")
    public void clickRemoveLocationIcon() {
        getTabTable().callActionByLabel("Remove Location");
    }

    private ButtonContainer getButtons() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return ButtonContainer.create(driver, wait);
    }

    //pending the solution of OSSPHY-46774
    //TODO: change > OldTable.createByComponentDataAttributeName(driver, wait, data-attributename);
    private OldActionsContainer getTabTable() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return OldActionsContainer.createFromParent(driver, wait, driver.findElement(By.xpath(".//div[@class='OssWindow tabWindow']")));
    }

}
