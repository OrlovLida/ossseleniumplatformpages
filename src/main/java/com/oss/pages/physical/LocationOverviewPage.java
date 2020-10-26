package com.oss.pages.physical;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.contextactions.ButtonContainer;
import com.oss.framework.components.contextactions.OldActionsContainer;
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

    private ButtonContainer buttons = ButtonContainer.create(driver, wait);

    //pending the solution of OSSPHY-46774
    //TODO: change to OldTable tabTable = OldTable.createByComponentDataAttributeName(driver, wait, data-attributename);
    OldActionsContainer tabTable = OldActionsContainer.createFromParent(driver, wait, driver.findElement(By.xpath(".//div[@class='OssWindow tabWindow']")));

    @Step("Click Create Location")
    public LocationWizardPage clickCreateLocation() {
        DelayUtils.waitForPageToLoad(driver, wait);
        buttons.callActionById("buttonsAppAttributesId-2");
        return new LocationWizardPage(driver);
    }

    @Step("Select Location Tab")
    public LocationOverviewPage selectLocationTab() {
        DelayUtils.waitForPageToLoad(driver, wait);
        TabsInterface tabs = TabWindowWidget.create(driver, wait);
        tabs.selectTabById("tabLocationId");
        return this;
    }

    //TODO: remove createByComponentDataAttributeName after OSSPHY-46774
    @Step("Filter object and select object row")
    public void filterObject(String columnName, String objectName) {
        OldTable tabTable = OldTable.createByComponentDataAttributeName(driver, wait, "tableAppLocationsId");
        tabTable.searchByAttributeWithLabel(columnName, Input.ComponentType.TEXT_FIELD, objectName);
        tabTable.selectRowByAttributeValueWithLabel(columnName, objectName);
    }

    @Step("Click Edit Location icon")
    public LocationWizardPage clickEditLocationIcon() {
        tabTable.callActionByLabel("Edit Location");
        return new LocationWizardPage(driver);
    }

    @Step("Click Remove Location icon")
    public LocationOverviewPage clickRemoveLocationIcon() {
        tabTable.callActionByLabel("Remove Location");
        return this;
    }

}
