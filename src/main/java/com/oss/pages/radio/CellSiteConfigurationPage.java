package com.oss.pages.radio;

import com.oss.framework.components.contextactions.OldActionsContainer;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.framework.widgets.tablewidget.TableInterface;
import com.oss.framework.widgets.tabswidget.TabsInterface;
import com.oss.framework.widgets.tabswidget.TabsWidget;
import com.oss.pages.BasePage;
import com.oss.pages.physical.LocationWizardPage;
import com.oss.pages.platform.LocationOverviewPage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * @author Milena Miętkiewicz
 */

public class CellSiteConfigurationPage extends BasePage {

    public CellSiteConfigurationPage(WebDriver driver) {
        super(driver);
    }

    @Step("Click plus icon")
    public CellSiteConfigurationPage clickPlusIcon() {
        DelayUtils.waitForPageToLoad(driver, wait);
        OldActionsContainer icons = OldActionsContainer.createFromParent(driver, wait, driver.findElement(By.xpath("//div[@data-attributename='TableTabsApp']")));
        icons.callActionByLabel("ADD");
        return this;
    }

    @Step("Select Create eNodeB from the drop-down list")
    public ENodeBWizardPage selectCreateENodeB() {
        DelayUtils.waitForPageToLoad(driver, wait);
        OldActionsContainer list = OldActionsContainer.createFromXPath(driver, wait, "//div[@class='portal']");
        list.callActionByLabel("Create eNodeB");
        return new ENodeBWizardPage(driver);
    }

    @Step("Select Base Stations Tab")
    public CellSiteConfigurationPage selectBaseStationsTab() {
        DelayUtils.waitForPageToLoad(driver, wait);
        TabsInterface tabs = TabsWidget.createById(driver, wait, "TableTabsApp");
        tabs.selectTabByLabel("Base Stations");
        return this;
    }

    @Step("Filter object name and select object name row")
    public CellSiteConfigurationPage filterObjectName(String objectName) {
        TableInterface table = OldTable.createByComponentDataAttributeName(driver, wait, "BsTableApp");
        table.searchByAttributeWithLabel("Name", Input.ComponentType.TEXT_FIELD, objectName);
        table.selectRowByAttributeValueWithLabel("Name", objectName);
        return this;
    }

    @Step("Click Edit eNodeB icon")
    public ENodeBWizardPage clickEditENodeBIcon() {
        OldActionsContainer icons = OldActionsContainer.createFromParent(driver, wait, driver.findElement(By.xpath("//div[@data-attributename='TableTabsApp']")));
        icons.callActionByLabel("Edit");
        return new ENodeBWizardPage(driver);
    }

    @Step("Click Remove eNodeB icon")
    public CellSiteConfigurationPage clickRemoveENodeBIcon() {
        OldActionsContainer icons = OldActionsContainer.createFromParent(driver, wait, driver.findElement(By.xpath("//div[@data-attributename='TableTabsApp']")));
        icons.callActionByLabel("Delete");
        return this;
    }
}
