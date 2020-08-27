package com.oss.pages.platform;
import com.oss.framework.components.Input;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.contextactions.ButtonContainer;
import com.oss.framework.components.contextactions.OldActionsContainer;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.framework.widgets.tablewidget.TableInterface;
import com.oss.framework.widgets.tabswidget.TabWindowWidget;
import com.oss.framework.widgets.tabswidget.TabsInterface;
import com.oss.framework.widgets.tabswidget.TabsWidget;
import com.oss.pages.BasePage;
import com.oss.pages.physical.LocationWizardPage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
/**
 * @author Milena Miętkiewicz
 */

public class LocationOverviewPage extends BasePage {

    public LocationOverviewPage(WebDriver driver){
        super(driver);
    }

    @Step("Click Create Location")
    public LocationWizardPage clickCreateLocation() {
        ButtonContainer buttonContainer = ButtonContainer.create(driver, wait);
        buttonContainer.callActionById("buttonsAppAttributesId-2");
        return new LocationWizardPage(driver);
    }

    @Step("Select Location Tab")
    public LocationOverviewPage selectLocationTab() {
        DelayUtils.waitForPageToLoad(driver, wait);
        TabsInterface tabWindowWidget = TabWindowWidget.create(driver, wait);
        tabWindowWidget.selectTabByLabel("tabLocationId");
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
        OldActionsContainer oldActionsContainer = OldActionsContainer.createFromParent(driver, wait, driver.findElement(By.xpath(".//div[@class='OssWindow tabWindow']")));
        oldActionsContainer.callActionByLabel("Edit Location");
        return new LocationWizardPage(driver);
    }

    @Step("Click Remove Location icon")
    public LocationOverviewPage clickRemoveLocationIcon() {
        OldActionsContainer oldActionsContainer = OldActionsContainer.createFromParent(driver, wait, driver.findElement(By.xpath(".//div[@class='OssWindow tabWindow']")));
        oldActionsContainer.callActionByLabel("Remove Location");
        return this;
    }

}