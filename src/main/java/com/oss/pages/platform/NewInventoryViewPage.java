package com.oss.pages.platform;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.collect.Multimap;
import com.oss.framework.components.attributechooser.AttributesChooser;
import com.oss.framework.components.pagination.PaginationComponent;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.inputs.Input.ComponentType;
import com.oss.framework.components.mainheader.ButtonPanel;
import com.oss.framework.components.prompts.Popup;
import com.oss.pages.platform.configuration.ChooseConfigurationWizard;
import com.oss.pages.platform.configuration.SaveConfigurationWizard;
import com.oss.pages.platform.configuration.SaveConfigurationWizard.Field;
import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.components.search.AdvancedSearch;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;
import com.oss.framework.widgets.Widget.WidgetType;
import com.oss.framework.widgets.propertypanel.PropertyPanel;
import com.oss.framework.widgets.table.TableRow;
import com.oss.framework.widgets.table.TableWidget;
import com.oss.framework.widgets.tabs.TabsWidget;
import com.oss.pages.BasePage;
import com.oss.pages.filterpanel.FilterPanelPage;

import io.qameta.allure.Step;

public class NewInventoryViewPage extends BasePage {

    private static final String TABLE_ID = "MainTableWidget";
    private static final String CONFIRM_ID = "ConfirmationBox_deleteBoxAppId_action_button";
    private static final String CHANGE_LAYOUT_BUTTON_ID = "ButtonChooseViewLayouts";
    private static final String HORIZONTAL_BUTTON_ID = "TWO_ROWS";
    private static final String VERTICAL_BUTTON_ID = "TWO_COLUMNS";
    private static final String SAVE_CONFIGURATION_BUTTON_ID = "ButtonSaveViewConfig";
    private static final String CHOOSE_CONFIGURATION_PAGE_ID = "ButtonChooseViewConfig";
    private static final String CHOOSE_CONFIG_ID = "chooseConfig";
    private static final String SAVE_CONFIG_TABS_ID = "saveTabs";
    private static final String DOWNLOAD_CONFIG_ID = "table_gql_Download";
    private static final String DOWNLOAD_CONFIG_PAGE_ID = "ButtonDownloadViewConfig";
    private static final String SETTINGS_ID = "frameworkCustomButtonsSecondaryGroup";
    private static final String SAVE_PROPERTY_CONFIG_ID = "propertyPanelSave";
    private static final String SAVE_NEW_CONFIG_ID = "saveNewConfig";

    public NewInventoryViewPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("Open Inventory View")
    public static NewInventoryViewPage goToInventoryViewPage(WebDriver driver, String basicURL, String type) {
        driver.get(String.format("%s/#/views/management/views/inventory-view/%s?perspective=LIVE", basicURL, type));
        WebDriverWait wait = new WebDriverWait(driver, 45);
        DelayUtils.waitForPageToLoad(driver, wait);
        return new NewInventoryViewPage(driver, wait);
    }

    public static NewInventoryViewPage getInventoryViewPage(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitForPageToLoad(driver, wait);
        return new NewInventoryViewPage(driver, wait);
    }

    public TableWidget getMainTable() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return TableWidget.createById(driver, TABLE_ID, wait);
    }

    public NewInventoryViewPage searchObject(String text) {
        TableWidget mainTable = getMainTable();
        mainTable.fullTextSearch(text);
        DelayUtils.waitForPageToLoad(driver, wait);
        return this;
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
        mainTable.unselectRow(rowId);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Enable Column")
    public void enableColumn(String columnLabel, String... path) {
        AttributesChooser attributesChooser = getMainTable().getAttributesChooser();
        attributesChooser.enableAttributeByLabel(columnLabel, path);
        attributesChooser.clickApply();
    }

    public List<String> getActiveColumnsHeaders() {
        TableWidget mainTable = getMainTable();
        return mainTable.getActiveColumnHeaders();
    }

    public List<TableRow> getSelectedRows() {
        return getMainTable().getSelectedRows();
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

    public List<String> getSavedFilters() {
        return getAdvancedSearch().getSavedFilters();
    }

    @Step("Clear all filters")
    public void clearFilters() {
        TableWidget mainTable = getMainTable();
        mainTable.clearAllFilters();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Clear all filters")
    public void clearFilter(String filterName) {
        TableWidget mainTable = getMainTable();
        mainTable.clearFilter(filterName);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void selectVisibilitySearchAttributes(List<String> attributeIds) {
        getMainTable().selectVisibilitySearchAttributes(attributeIds);
    }

    public void unselectVisibilitySearchAttributes(List<String> attributeIds) {
        getMainTable().unselectVisibilitySearchAttributes(attributeIds);
    }

    public List<String> getAllVisibleFilters() {
        return getMainTable().getAllVisibleFilters();
    }

    public AdvancedSearch getAdvancedSearch() {
        return getMainTable().getAdvancedSearch();
    }

    public String getAttributeValue(String columnId, int rowId) {
        TableWidget mainTable = getMainTable();
        return mainTable.getCellValue(rowId, columnId);
    }

    public void callActionByLabel(int rowId, String groupLabel, String actionLabel) {
        selectObjectByRowId(rowId);
        TableWidget tableWidget = getMainTable();
        tableWidget.callActionByLabel(groupLabel, actionLabel);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Check if table has no data")
    public boolean checkIfTableIsEmpty() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return getMainTable().hasNoData();
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

    @Step("Call {actionId} action from {groupId} group")
    public NewInventoryViewPage callAction(String groupId, String actionId) {
        getMainTable().callAction(groupId, actionId);
        return this;
    }

    @Step("Call context action by ID : {actionId}")
    public void callActionById(String actionId) {
        getMainTable().callAction(actionId);
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
        getMainTable().callAction(ActionsContainer.KEBAB_GROUP_ID, SAVE_NEW_CONFIG_ID);
        getSaveConfigurationWizard().saveAsNew(configurationName, fields);
        return this;
    }

    @Step("Refresh main table")
    public void refreshMainTable() {
        callAction(ActionsContainer.KEBAB_GROUP_ID, TableWidget.REFRESH_ACTION_ID);
    }

    @Step("Refresh main table unit there is no data")
    public void doRefreshWhileNoData() {
        getMainTable().doRefreshWhileNoData(10000, TableWidget.REFRESH_ACTION_ID);
    }

    public TabsWidget getTabsWidget() {
        if (getSelectedRows().isEmpty()) {
            throw new UnsupportedOperationException("Only single selection is supported");
        }
        Widget.waitForWidget(wait, TabsWidget.TABS_WIDGET_CLASS);
        return TabsWidget.create(driver, wait);
    }

    public PropertyPanel getPropertyPanel(int rowId, String propertyPanelId) {
        selectObjectByRowId(rowId);
        return getPropertyPanel(propertyPanelId);
    }

    public PropertyPanel getPropertyPanel(String propertyPanelId) {
        Widget.waitForWidget(wait, PropertyPanel.PROPERTY_PANEL_CLASS);
        return (PropertyPanel) getTabsWidget().getWidget(propertyPanelId, WidgetType.PROPERTY_PANEL);
    }

    public String getActiveTabLabel() {
        return getTabsWidget().getActiveTabLabel();
    }

    public String getTabLabel(int index) {
        return getTabsWidget().getTabLabel(index);
    }

    public void searchDetail(int rowId, String detailLabel, String widgetId, String text) {
        selectObjectByRowId(rowId);
        selectTabByLabel(detailLabel);
        TableWidget tableWidget = (TableWidget) getTabsWidget().getWidget(widgetId, WidgetType.TABLE_WIDGET);
        tableWidget.fullTextSearch(text);
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

    public void clickConfirmConfirmationBox() {
        getConfirmationBox().clickButtonById(CONFIRM_ID);
    }

    @Step("Change layout to Horizontal Orientation")
    public NewInventoryViewPage setHorizontalLayout() {
        DelayUtils.waitForPageToLoad(driver, wait);
        if (!isHorizontal()) {
            ButtonPanel.create(driver, wait).clickButton(CHANGE_LAYOUT_BUTTON_ID, HORIZONTAL_BUTTON_ID);
            Popup.create(driver,wait).clickButtonByLabel("Change");
        }
        DelayUtils.waitForPageToLoad(driver, wait);
        return this;
    }

    public boolean isHorizontal() {
        return ButtonPanel.create(driver, wait).isHorizontalLayout();
    }

    // TODO: add getMethods for popup and property panel

    @Step("Change layout to Vertical Orientation")
    public NewInventoryViewPage setVerticalLayout() {
        DelayUtils.waitForPageToLoad(driver, wait);
        if (isHorizontal()) {
            ButtonPanel.create(driver, wait).clickButton(CHANGE_LAYOUT_BUTTON_ID, VERTICAL_BUTTON_ID);
            Popup.create(driver,wait).clickButtonByLabel("Change");
        }
        DelayUtils.waitForPageToLoad(driver, wait);
        return this;
    }

    @Step("Change Properties order")
    public NewInventoryViewPage changePropertiesOrder(int rowId, String widgetId, String propertyLabel,
                                                      int position) {
        getPropertyPanel(rowId, widgetId).changePropertyOrder(propertyLabel, position);
        return this;
    }

    @Step("Save configuration for properties")
    public NewInventoryViewPage saveConfigurationForProperties(int rowId, String widgetId, String
            configurationName, Field... fields) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getPropertyPanel(rowId, widgetId).callAction(SETTINGS_ID, SAVE_PROPERTY_CONFIG_ID);
        getSaveConfigurationWizard().saveAsNew(configurationName, fields);
        return this;
    }

    @Step("Save configuration for page")
    public NewInventoryViewPage savePageConfiguration(Field... fields) {
        DelayUtils.waitForPageToLoad(driver, wait);
        ButtonPanel.create(driver, wait).clickButton(SAVE_CONFIGURATION_BUTTON_ID);
        getSaveConfigurationWizard().save(fields);
        return this;
    }

    @Step("Save new configuration for page")
    public NewInventoryViewPage saveNewPageConfiguration(String configurationName, Field... fields) {
        DelayUtils.waitForPageToLoad(driver, wait);
        ButtonPanel.create(driver, wait).clickButton(CHOOSE_CONFIGURATION_PAGE_ID);
        getSaveConfigurationWizard().saveAsNew(configurationName, fields);
        return this;
    }

    @Step("Save configuration for tabs for user")
    public NewInventoryViewPage saveConfigurationForTabs(String configurationName, Field... fields) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getTabsWidget().callActionById(ActionsContainer.KEBAB_GROUP_ID, SAVE_CONFIG_TABS_ID);
        getSaveConfigurationWizard().saveAsNew(configurationName, fields);
        return this;
    }

    @Step("Apply configuration for main table")
    public NewInventoryViewPage applyConfigurationForMainTable(String configurationName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getMainTable().callAction(ActionsContainer.KEBAB_GROUP_ID, CHOOSE_CONFIG_ID);
        getChooseConfigurationWizard().chooseConfiguration(configurationName).apply();
        return this;
    }

    @Step("Apply configuration for tabs")
    public NewInventoryViewPage applyConfigurationForTabs(String configurationName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getTabsWidget().callActionById(ActionsContainer.KEBAB_GROUP_ID, CHOOSE_CONFIG_ID);
        getChooseConfigurationWizard().chooseConfiguration(configurationName).apply();
        return this;
    }

    @Step("Apply configuration for page")
    public NewInventoryViewPage applyConfigurationForPage(String configurationName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        ButtonPanel.create(driver, wait).clickButton(CHOOSE_CONFIGURATION_PAGE_ID);
        getChooseConfigurationWizard().chooseConfiguration(configurationName).apply();
        return this;
    }

    @Step("Delete configuration for page")
    public NewInventoryViewPage deletePageConfiguration(String configurationName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        ButtonPanel.create(driver, wait).clickButton(CHOOSE_CONFIGURATION_PAGE_ID);
        getChooseConfigurationWizard().deleteConfiguration(configurationName).cancel();
        return this;
    }

    @Step("Delete configuration for main table")
    public NewInventoryViewPage deleteConfigurationForMainTable(String configurationName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getMainTable().callAction(ActionsContainer.KEBAB_GROUP_ID, CHOOSE_CONFIG_ID);
        getChooseConfigurationWizard().deleteConfiguration(configurationName).cancel();
        return this;
    }

    @Step("Download configuration for main table")
    public NewInventoryViewPage downloadConfigurationForMainTable(String configurationName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getMainTable().callAction(ActionsContainer.KEBAB_GROUP_ID, DOWNLOAD_CONFIG_ID);
        getChooseConfigurationWizard().chooseConfiguration(configurationName).download();
        return this;
    }

    @Step("Download configuration for page")
    public NewInventoryViewPage downloadConfigurationForPage(String configurationName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        ButtonPanel.create(driver, wait).clickButton(DOWNLOAD_CONFIG_PAGE_ID);
        getChooseConfigurationWizard().chooseConfiguration(configurationName).download();
        return this;
    }

    @Step("Disable Column and apply")
    public void disableColumnAndApply(String columnLabel) {
        TableWidget mainTable = getMainTable();
        mainTable.disableColumnByLabel(columnLabel);
        DelayUtils.waitForPageToLoad(driver, wait);
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
        callAction(ActionsContainer.SHOW_ON_GROUP_ID, "WebManagement_HierarchicalView");
        return new HierarchyViewPage(driver);
    }

    public boolean isTabVisible(String tabLabel) {
        return getTabsWidget().isTabDisplayed(tabLabel);
    }

    public NewInventoryViewPage changeTab(String tabLabel) {
        getTabsWidget().selectTabByLabel(tabLabel);
        return this;
    }

    public int countOfVisibleTags() {
        DelayUtils.waitForPageToLoad(driver, wait);
        AdvancedSearch advancedSearch = new AdvancedSearch(driver, wait);
        return advancedSearch.getTagsNumber();
    }

    public String getIdOfMainTableObject(int rowIndex) {
        DelayUtils.waitForPageToLoad(driver, wait);
        return getMainTable().getCellValue(rowIndex, "id");
    }

    public boolean isOnlyOneObject(String id) {
        DelayUtils.waitForPageToLoad(driver, wait);
        return getMainTable().getRowsNumber() == 1 && getIdOfMainTableObject(0).equals(id);
    }

    public int getColumnSize(String columnId) {
        int columnIndex = getMainTable().getActiveColumnIds().indexOf(columnId);
        return getMainTable().getColumnSize(columnIndex);
    }

    public void setPagination(int paginationValue) {
        PaginationComponent pagination = getMainTable().getPagination();
        pagination.changeRowsCount(paginationValue);
    }

    public void setDefaultSettings() {
        getMainTable().getAttributesChooser().clickDefaultSettings();
    }

    private ConfirmationBox getConfirmationBox() {
        return ConfirmationBox.create(driver, wait);
    }
    private SaveConfigurationWizard getSaveConfigurationWizard(){
        return SaveConfigurationWizard.create(driver, wait);
    }

    private ChooseConfigurationWizard getChooseConfigurationWizard(){
        return ChooseConfigurationWizard.create(driver, wait);
    }

}
