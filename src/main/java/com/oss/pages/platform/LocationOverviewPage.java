package com.oss.pages.platform;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.contextactions.ButtonContainer;
import com.oss.framework.components.contextactions.OldActionsContainer;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.framework.widgets.tablewidget.TableInterface;
import com.oss.framework.widgets.tabswidget.TabWindowWidget;
import com.oss.framework.widgets.tabswidget.TabsInterface;
import com.oss.pages.BasePage;
import com.oss.pages.physical.LocationWizardPage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * @author Milena Miętkiewicz
 */

public class LocationOverviewPage extends BasePage {

    public LocationOverviewPage(WebDriver driver){
        super(driver);
    }

    @Step("Click Create Location")
    public LocationWizardPage clickCreateLocation() {
        DelayUtils.waitForPageToLoad(driver, wait);
        ButtonContainer buttons = ButtonContainer.create(driver, wait);
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

    @Step("Filter object name and select object name row")
    public LocationOverviewPage filterObjectName(String objectName) {
        TableInterface table = OldTable.createByComponentDataAttributeName(driver, wait, "tableAppLocationsId");
        table.searchByAttributeWithLabel("Name", Input.ComponentType.TEXT_FIELD, objectName);
        table.selectRowByAttributeValueWithLabel("Name", objectName);
        return this;
    }

    @Step("Click Edit Location icon")
    public LocationWizardPage clickEditLocationIcon() {
        OldActionsContainer icons = OldActionsContainer.createFromParent(driver, wait, driver.findElement(By.xpath(".//div[@class='OssWindow tabWindow']")));
        icons.callActionByLabel("Edit Location");
        return new LocationWizardPage(driver);
    }

    @Step("Click Remove Location icon")
    public LocationOverviewPage clickRemoveLocationIcon() {
        OldActionsContainer icons = OldActionsContainer.createFromParent(driver, wait, driver.findElement(By.xpath(".//div[@class='OssWindow tabWindow']")));
        icons.callActionByLabel("Remove Location");
        return this;
    }

}
