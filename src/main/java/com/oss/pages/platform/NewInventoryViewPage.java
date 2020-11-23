package com.oss.pages.platform;

import com.oss.framework.components.common.AttributesChooser;
import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.portals.SaveConfigurationWizard.Field;
import com.oss.framework.components.search.AdvancedSearch;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.filterpanel.FilterPanelPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.mainheader.ButtonPanel;
import com.oss.framework.widgets.Widget;
import com.oss.framework.widgets.Wizard;
import com.oss.framework.widgets.propertypanel.PropertiesFilter;
import com.oss.framework.widgets.propertypanel.PropertyPanel;
import com.oss.framework.widgets.tablewidget.TableWidget;
import com.oss.framework.widgets.tabswidget.TabsWidget;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;


public class NewInventoryViewPage extends BasePage {

    private final String loadBar = "//div[@class='load-bar']";

    public NewInventoryViewPage(WebDriver driver) {
        super(driver);
    }

    public TableWidget getMainTable() {
        Widget.waitForWidget(wait, TableWidget.TABLE_WIDGET_CLASS);
        return TableWidget.create(driver, TableWidget.TABLE_WIDGET_CLASS, wait);
    }

    public TabsWidget getTabsWidget() {
        Widget.waitForWidget(wait, TabsWidget.TABS_WIDGET_CLASS);
        return TabsWidget.create(driver, wait);
    }

    public Wizard getWizard() {
        return Wizard.createWizard(driver, wait);
    }

    public PropertyPanel getPropertyPanel() {
        Widget.waitForWidget(wait, PropertyPanel.PROPERTY_PANEL_CLASS);
        return PropertyPanel.create(driver);
    }

    public PropertiesFilter getPropertiesFilter() {
        Widget.waitForWidget(wait, PropertiesFilter.PROPERTIES_FILTER_CLASS);
        return PropertiesFilter.create(driver, wait);
    }

    //TODO: add getMethods for popup and property panel

    //TODO: wrap WebElement
    public WebElement getLoadBar() {
        return this.driver.findElement(By.xpath(loadBar));
    }

    public boolean isLoadBarDisplayed() {
        return getLoadBar().isDisplayed();
    }

    //TODO: create layoutWrapper component
    public int howManyRows() {
        return driver.findElements(By.xpath("//div[@class='view-v2-content']/div/div[@class='row']")).size();
    }

    @Step("Open Inventory View")
    public static NewInventoryViewPage goToInventoryViewPage(WebDriver driver, String basicURL, String type) {
        driver.get(String.format("%s/#/views/management/views/inventory-view/" + type +
                "?perspective=LIVE", basicURL));
        return new NewInventoryViewPage(driver);
    }

    @Step("Check if table has no data")
    public boolean checkIfTableIsEmpty() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return getMainTable().checkIfTableIsEmpty();
    }

    @Step("Change layout to Horizontal Orientation")
    public NewInventoryViewPage changeLayoutToHorizontal() {
        DelayUtils.waitForPageToLoad(driver, wait);
        if (howManyRows() == 1) {
            ButtonPanel.create(driver, wait).expandLayoutMenu();
            DropdownList.create(driver, wait).selectOptionWithId("TWO_ROWS");
        }
        DelayUtils.waitForPageToLoad(driver, wait);
        return this;
    }

    @Step("Change layout to Vertical Orientation")
    public NewInventoryViewPage changeLayoutToVertical() {
        DelayUtils.waitForPageToLoad(driver, wait);
        if (howManyRows() == 2) {
            ButtonPanel.create(driver, wait).expandLayoutMenu();
            DropdownList.create(driver, wait).selectOptionWithId("TWO_COLUMNS");
        }
        DelayUtils.waitForPageToLoad(driver, wait);
        return this;
    }

    @Step("Open Filter Panel")
    public FilterPanelPage openFilterPanel() {
        DelayUtils.waitForPageToLoad(driver, wait);
        AdvancedSearch advancedSearch = new AdvancedSearch(driver, wait);
        advancedSearch.openSearchPanel();
        return new FilterPanelPage(driver);
    }

    @Step("Clear all tags")
    public NewInventoryViewPage clearAllTags() {
        DelayUtils.waitForPageToLoad(driver, wait);
        AdvancedSearch advancedSearch = new AdvancedSearch(driver, wait);
        advancedSearch.clickOnTagByLabel("Clear");
        DelayUtils.waitForPageToLoad(driver, wait);
        return this;
    }

    @Step("Pick first row")
    public NewInventoryViewPage selectFirstRow() {
        DelayUtils.waitForPageToLoad(driver, wait);
        getMainTable().selectFirstRow();
        return this;
    }

    @Step("Open Edit")
    public NewInventoryViewPage editObject(String ActionId) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getMainTable().callAction("EDIT", ActionId);
        return this;
    }

    @Step("Edit Text Fields")
    public NewInventoryViewPage editTextFields(String componentId, Input.ComponentType componentType, String value) {
        DelayUtils.waitForPageToLoad(driver, wait);
        setValueOnTextType(componentId, componentType, value);
        return this;
    }

    @Step("Delete object")
    public NewInventoryViewPage deleteObject() {
        DelayUtils.sleep(10000);
        DelayUtils.waitForPageToLoad(driver, wait);
        Button.createBySelectorAndId(driver, "a", "DeleteVLANRangeContextAction").click();
        DelayUtils.sleep(10000);
        getWizard().clickButtonByLabel("OK");
        DelayUtils.waitForPageToLoad(driver, wait);
        return this;
    }

    @Step("Change columns order")
    public NewInventoryViewPage changeColumnsOrderInMainTable(String columnLabel, int position) {
        getMainTable().changeColumnsOrder(columnLabel, position);
        DelayUtils.waitForPageToLoad(driver, wait);
        return this;
    }

    @Step("Change Tabs order")
    public NewInventoryViewPage changeTabsOrder(String tabLabel, int position) {
        getTabsWidget().changeTabsOrder(tabLabel, position);
        DelayUtils.waitForPageToLoad(driver, wait);
        return this;
    }

    @Step("Save configuration for main table")
    public NewInventoryViewPage saveConfigurationForMainTable(String configurationName, Field... fields) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getMainTable().openSaveConfigurationWizard().saveAsNew(configurationName, fields);
        return this;
    }

    @Step("Save configuration for properties")
    public NewInventoryViewPage saveConfigurationForProperties(String configurationName, Field... fields) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getPropertiesFilter().openSaveAsNewConfigurationWizard().saveAsNew(configurationName, fields);
        return this;
    }

    @Step("Save configuration for page")
    public NewInventoryViewPage savePageConfiguration(Field... fields) {
        DelayUtils.waitForPageToLoad(driver, wait);
        ButtonPanel.create(driver, wait).openSaveConfigurationWizard().save(fields);
        return this;
    }

    @Step("Save new configuration for page")
    public NewInventoryViewPage saveNewPageConfiguration(String configurationName, Field... fields) {
        DelayUtils.waitForPageToLoad(driver, wait);
        ButtonPanel.create(driver, wait).openSaveConfigurationWizard().saveAsNew(configurationName, fields);
        return this;
    }

    @Step("Save configuration for tabs for user")
    public NewInventoryViewPage saveConfigurationForTabs(String configurationName, Field... fields) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getTabsWidget().openSaveConfigurationWizard().saveAsNew(configurationName, fields);
        return this;
    }

    @Step("Apply configuration for main table")
    public NewInventoryViewPage applyConfigurationForMainTable(String configurationName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getMainTable().openChooseConfigurationWizard().chooseConfiguration(configurationName).apply();
        return this;
    }

    @Step("Apply configuration for tabs")
    public NewInventoryViewPage applyConfigurationForTabs(String configurationName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getTabsWidget().openChooseConfigurationWizard().chooseConfiguration(configurationName).apply();
        return this;
    }

    @Step("Apply configuration for properties")
    public NewInventoryViewPage applyConfigurationForProperties(String configurationName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getPropertiesFilter().openChooseConfigurationWizard().chooseConfiguration(configurationName).apply();
        return this;
    }

    @Step("Apply configuration for page")
    public NewInventoryViewPage applyConfigurationForPage(String configurationName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        ButtonPanel.create(driver, wait).openChooseConfigurationWizard().chooseConfiguration(configurationName).apply();
        return this;
    }

    @Step("Download configuration for main table")
    public NewInventoryViewPage downloadConfigurationForMainTable(String configurationName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getMainTable().openDownloadConfigurationWizard().chooseConfiguration(configurationName).download();
        return this;
    }

    @Step("Download configuration for tabs")
    public NewInventoryViewPage downloadConfigurationForTabs(String configurationName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getTabsWidget().openDownloadConfigurationWizard().chooseConfiguration(configurationName).download();
        return this;
    }

    @Step("Download configuration for page")
    public NewInventoryViewPage downloadConfigurationForPage(String configurationName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        ButtonPanel.create(driver, wait).openDownloadConfigurationWizard().chooseConfiguration(configurationName).download();
        return this;
    }

    @Step("Download configuration for properties")
    public NewInventoryViewPage downloadConfigurationForProperties(String configurationName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getPropertiesFilter().openDownloadConfigurationWizard().chooseConfiguration(configurationName).download();
        return this;
    }

    @Step("Enable Column and apply")
    public NewInventoryViewPage enableColumnAndApply(String columnLabel) {
        enableColumn(columnLabel).clickApply();
        return new NewInventoryViewPage(driver);
    }

    @Step("Enable Column")
    public AttributesChooser enableColumn(String columnLabel) {
        getMainTable().getAttributesChooser().enableAttributeByLabel(columnLabel);
        return getMainTable().getAttributesChooser();
    }

    @Step("Disable Column and apply")
    public NewInventoryViewPage disableColumnAndApply(String columnLabel) {
        disableColumn(columnLabel).clickApply();
        return new NewInventoryViewPage(driver);
    }

    @Step("Disable Column")
    public AttributesChooser disableColumn(String columnLabel) {
        getMainTable().getAttributesChooser().disableAttributeByLabel(columnLabel);
        return getMainTable().getAttributesChooser();
    }

    @Step("Enable Widget Tab and apply")
    public NewInventoryViewPage enableWidgetAndApply(String widgetLabel) {
        getTabsWidget().getWidgetChooser().enableWidgetsByLabel(widgetLabel).clickAdd();
        return new NewInventoryViewPage(driver);
    }

    @Step("Disable Widget Tab and apply")
    public NewInventoryViewPage disableWidgetAndApply(String widgetLabel) {
        getTabsWidget().getWidgetChooser().disableWidgetsByLabel(widgetLabel).clickAdd();
        return new NewInventoryViewPage(driver);
    }

    @Step("Open Hierarchy View for selected object")
    public HierarchyViewPage goToHierarchyViewForSelectedObject() {
        DelayUtils.waitForPageToLoad(driver, wait);
        getMainTable().getContextActions().callActionById("NAVIGATION");
        DropdownList.create(driver, wait).selectOptionWithId("WebManagement_HierarchicalView");
        return new HierarchyViewPage(driver);
    }

    public String getTabLabel(int tabPosition) {
        return getTabsWidget().getTabLabel(tabPosition);
    }

    public boolean isTabVisible(String tabLabel) {
        return getTabsWidget().isTabVisible(tabLabel);
    }

    public NewInventoryViewPage changeTab(String tabLabel) {
        getTabsWidget().selectTabByLabel(tabLabel);
        return this;
    }

    public boolean isAllTagsInvisible() {
        DelayUtils.waitForPageToLoad(driver, wait);
        AdvancedSearch advancedSearch = new AdvancedSearch(driver, wait);
        return advancedSearch.howManyTagsIsVisible() == 0;
    }

    public String getIdOfMainTableObject(int rowIndex) {
        DelayUtils.waitForPageToLoad(driver, wait);
        return getMainTable().getAttribute(rowIndex, "id");
    }

    public boolean isOnlyOneObject(String id) {
        DelayUtils.waitForPageToLoad(driver, wait);
        return getMainTable().howManyRowsOnFirstPage() == 1 && getIdOfMainTableObject(0).equals(id);
    }

    public Input getComponent(String componentId, Input.ComponentType componentType) {
        return ComponentFactory.create(componentId, componentType, this.driver, this.wait);
    }

    private void setValueOnTextType(String componentId, Input.ComponentType componentType, String value) {
        DelayUtils.sleep();
        getWizard().getComponent(componentId, componentType).clearByAction();
        getWizard().getComponent(componentId, componentType).setSingleStringValue(value);
    }

}
