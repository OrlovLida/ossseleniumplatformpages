package com.oss.pages.platform;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.collect.Multimap;
import com.oss.framework.components.common.AttributesChooser;
import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.Input.ComponentType;
import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.components.portals.SaveConfigurationWizard.Field;
import com.oss.framework.components.search.AdvancedSearch;
import com.oss.framework.mainheader.ButtonPanel;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;
import com.oss.framework.widgets.Widget.WidgetType;
import com.oss.framework.widgets.Wizard;
import com.oss.framework.widgets.propertypanel.PropertiesFilter;
import com.oss.framework.widgets.propertypanel.PropertyPanel;
import com.oss.framework.widgets.tablewidget.TableRow;
import com.oss.framework.widgets.tablewidget.TableWidget;
import com.oss.framework.widgets.tabswidget.TabsWidget;
import com.oss.pages.BasePage;
import com.oss.pages.filterpanel.FilterPanelPage;

import io.qameta.allure.Step;
import javafx.scene.control.Tab;

public class NewInventoryViewPage extends BasePage {

    private static final String LOAD_BAR = "//div[@class='load-bar']";

    @Step("Open Inventory View")
    public static NewInventoryViewPage goToInventoryViewPage(WebDriver driver, String basicURL, String type) {
        driver.get(String.format("%s/#/views/management/views/inventory-view/" + type +
                "?perspective=LIVE", basicURL));
        WebDriverWait wait = new WebDriverWait(driver, 45);
        DelayUtils.waitForPageToLoad(driver, wait);
        return new NewInventoryViewPage(driver, wait);
    }

    public static NewInventoryViewPage getInventoryViewPage(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitForPageToLoad(driver, wait);
        return new NewInventoryViewPage(driver, wait);
    }

    @Deprecated
    public NewInventoryViewPage(WebDriver driver) {
        super(driver);
    }

    private NewInventoryViewPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    //Main table operations

    public TableWidget getMainTable() {
        Widget.waitForWidget(wait, TableWidget.TABLE_WIDGET_CLASS);
        return TableWidget.create(driver, TableWidget.TABLE_WIDGET_CLASS, wait);
    }

    public void searchObject(String text) {
        TableWidget mainTable = getMainTable();
        mainTable.typeIntoSearch(text);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Pick first row")
    public NewInventoryViewPage selectFirstRow() {
        selectObjectByRowId(0);
        return this;
    }

    public void selectObjectByRowId(int rowId) {
        TableWidget mainTable = getMainTable();
        mainTable.selectRow(rowId);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void unselectObjectByRowId(int rowId) {
        TableWidget mainTable = getMainTable();
        mainTable.unselectTableRow(0);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void removeColumn(String columnLabel) {
        TableWidget mainTable = getMainTable();
        mainTable.disableColumnByLabel(columnLabel);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Enable Column and apply")
    public NewInventoryViewPage enableColumnAndApply(String columnLabel) {
        enableColumn(columnLabel).clickApply();
        return this;
    }

    @Step("Enable Column")
    public AttributesChooser enableColumn(String columnLabel) {
        getMainTable().getAttributesChooser().enableAttributeByLabel(columnLabel);
        return getMainTable().getAttributesChooser();
    }

    public List<String> getActiveColumnsHeaders() {
        TableWidget mainTable = getMainTable();
        return mainTable.getActiveColumnHeaders();
    }

    public List<TableRow> getSelectedRows() {
        List<TableRow> selectedRows = getMainTable().getSelectedRows();
        return selectedRows;
    }

    public int getRowsNumber() {
        return getMainTable().getRowsNumber();
    }

    public Multimap<String, String> searchByAttributeValue(String attributeId, String attributeValue, ComponentType componentType) {
        TableWidget mainTable = getMainTable();
        mainTable.searchByAttribute(attributeId, componentType, attributeValue);
        Multimap<String, String> filterValues = mainTable.getAppliedFilters();
        DelayUtils.waitForPageToLoad(driver, wait);
        return filterValues;
    }

    @Step("Clear all filters")
    public void clearFilters() {
        TableWidget mainTable = getMainTable();
        mainTable.clearAllFilters();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public String getAttributeValue(String attributeLabel, int rowId) {
        TableWidget mainTable = getMainTable();
        return mainTable.getCellValue(rowId, attributeLabel);
    }

    public void callActionByLabel(int rowId, String groupLabel, String actionLabel) {
        selectObjectByRowId(rowId);
        TableWidget tableWidget = getMainTable();
        tableWidget.callActionByLabel(groupLabel, actionLabel);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void callActionByLabel(String actionLabel, String... path) {

    }

    @Step("Check if table has no data")
    public boolean checkIfTableIsEmpty() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return getMainTable().checkIfTableIsEmpty();
    }

    @Step("Open Filter Panel")
    @Deprecated
    public FilterPanelPage openFilterPanel() {
        DelayUtils.waitForPageToLoad(driver, wait);
        AdvancedSearch advancedSearch = new AdvancedSearch(driver, wait);
        advancedSearch.openSearchPanel();
        DelayUtils.waitForPageToLoad(driver, wait);
        return new FilterPanelPage(driver);
    }

    @Step("Set value in Filter Panel")
    @Deprecated
    public FilterPanelPage setFilterPanel(String componentId, String value) {
        DelayUtils.waitForPageToLoad(driver, wait);
        openFilterPanel().setValue(Input.ComponentType.TEXT_FIELD, componentId, value).applyFilter();
        return new FilterPanelPage(driver);
    }

    @Step("Call {actionId} action from {groupId} group")
    public NewInventoryViewPage callAction(String groupId, String actionId) {
        getMainTable().callAction(groupId, actionId);
        return this;
    }

    @Step("Clear all tags")
    @Deprecated
    public NewInventoryViewPage clearAllTags() {
        DelayUtils.waitForPageToLoad(driver, wait);
        AdvancedSearch advancedSearch = new AdvancedSearch(driver, wait);
        advancedSearch.clickOnTagByLabel("Clear");
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

    //Details operations

    public TabsWidget getTabsWidget() {
        if (getSelectedRows().size() == 0) {
            throw new RuntimeException("Only single selection is supported");
        }

        Widget.waitForWidget(wait, TabsWidget.TABS_WIDGET_CLASS);
        return TabsWidget.create(driver, wait);
    }

    public PropertyPanel getPropertyPanel(int rowId, String propertyPanelId) {
        selectObjectByRowId(rowId);
        return (PropertyPanel) getTabsWidget().getWidget(propertyPanelId, WidgetType.PROPERTY_PANEL);
    }

    public String getActiveTabLabel() {
        return getTabsWidget().getActiveTabLabel();
    }

    public String getTabLabel(int index) {
        return getTabsWidget().getTabLabel(index);
    }

    public Wizard getWizard() {
        return Wizard.createWizard(driver, wait);
    }

    @Deprecated
    public PropertyPanel getPropertyPanel() {
        Widget.waitForWidget(wait, PropertyPanel.PROPERTY_PANEL_CLASS);
        return PropertyPanel.create(driver);
    }

    @Deprecated
    public PropertiesFilter getPropertiesFilter() {
        Widget.waitForWidget(wait, PropertiesFilter.PROPERTIES_FILTER_CLASS);
        return PropertiesFilter.create(driver, wait);
    }

    public void searchDetail(int rowId, String detailLabel, String widgetId, String text) {
        selectObjectByRowId(rowId);
        selectTabByLabel(detailLabel);
        TableWidget tableWidget = (TableWidget) getTabsWidget().getWidget(widgetId, WidgetType.TABLE_WIDGET);
        tableWidget.typeIntoSearch(text);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void selectTabByLabel(String detailLabel) {
        TabsWidget tabsWidget = getTabsWidget();
        tabsWidget.selectTabByLabel(detailLabel);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void selectDetail(int rowId, String detailTab, String widgetId, int detailRowId) {
        selectObjectByRowId(rowId);
        selectTabByLabel(detailTab);
        TableWidget tableWidget = (TableWidget) getTabsWidget().getWidget(widgetId, WidgetType.TABLE_WIDGET);
        tableWidget.selectRow(detailRowId);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public String getDetailAttributeValue() {
        return null;
    }

    //TODO: add getMethods for popup and property panel

    //TODO: wrap WebElement
    @Deprecated
    public WebElement getLoadBar() {
        return this.driver.findElement(By.xpath(LOAD_BAR));
    }

    public boolean isLoadBarDisplayed() {
        return getLoadBar().isDisplayed();
    }

    //View's operations

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

    //TODO: create layoutWrapper component
    public int howManyRows() {
        return driver.findElements(By.xpath("//div[@class='view-v2-content']/div/div[@class='row']")).size();
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

    @Step("Delete object")
    @Deprecated
    public NewInventoryViewPage deleteObject() {
        DelayUtils.waitForPageToLoad(driver, wait);
        Button.createBySelectorAndId(driver, "a", "DeleteVLANRangeContextAction").click();
        DelayUtils.waitForPageToLoad(driver, wait);
        getWizard().clickButtonByLabel("OK");
        DelayUtils.waitForPageToLoad(driver, wait);
        return this;
    }

    @Step("Save configuration for properties")
    public NewInventoryViewPage saveConfigurationForProperties(int rowId, String widgetId, String configurationName, Field... fields) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getPropertyPanel(rowId, widgetId).openSaveAsNewConfigurationWizard().saveAsNew(configurationName, fields);
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

    @Step("Delete configuration for page")
    public NewInventoryViewPage deletePageConfiguration(String configurationName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        ButtonPanel.create(driver, wait).openChooseConfigurationWizard().deleteConfiguration(configurationName).cancel();
        return this;
    }

    @Step("Delete configuration for main table")
    public NewInventoryViewPage deleteConfigurationForMainTable(String configurationName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getMainTable().openChooseConfigurationWizard().deleteConfiguration(configurationName).cancel();
        return this;
    }

    @Step("Delete configuration for tabs")
    public NewInventoryViewPage deleteConfigurationForTabs(String configurationName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getTabsWidget().openChooseConfigurationWizard().deleteConfiguration(configurationName).cancel();
        return this;
    }

    @Step("Delete configuration for properties")
    public NewInventoryViewPage deleteConfigurationForProperties(String configurationName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getPropertiesFilter().openChooseConfigurationWizard().deleteConfiguration(configurationName).cancel();
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

    @Step("Disable Column and apply")
    public NewInventoryViewPage disableColumnAndApply(String columnLabel) {
        disableColumn(columnLabel).clickApply();
        return new NewInventoryViewPage(driver, wait);
    }

    @Step("Disable Column")
    public AttributesChooser disableColumn(String columnLabel) {
        getMainTable().getAttributesChooser().disableAttributeByLabel(columnLabel);
        return getMainTable().getAttributesChooser();
    }

    @Step("Enable Widget Tab and apply")
    public NewInventoryViewPage enableWidgetAndApply(String widgetLabel) {
        getTabsWidget().getWidgetChooser().enableWidgetsByLabel(widgetLabel).clickAdd();
        return new NewInventoryViewPage(driver, wait);
    }

    @Step("Disable Widget Tab and apply")
    public NewInventoryViewPage disableWidgetAndApply(String widgetLabel) {
        getTabsWidget().getWidgetChooser().disableWidgetsByLabel(widgetLabel).clickAdd();
        return new NewInventoryViewPage(driver, wait);
    }

    @Step("Open Hierarchy View for selected object")
    public HierarchyViewPage goToHierarchyViewForSelectedObject() {
        DelayUtils.waitForPageToLoad(driver, wait);
        getMainTable().getContextActions().callActionById("NAVIGATION");
        DropdownList.create(driver, wait).selectOptionWithId("WebManagement_HierarchicalView");
        return new HierarchyViewPage(driver);
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

}
