package com.oss.pages.platform;

import com.oss.framework.components.common.AttributesChooser;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.portals.ActionsDropdownList;
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

    private TableWidget mainTable;
    private TabsWidget tabsWidget;
    private PropertyPanel propertyPanel;
    private PropertiesFilter propertiesFilter;
    private Wizard wizard;

    @Step("Open Inventory View")
    public static NewInventoryViewPage goToInventoryViewPage(WebDriver driver, String basicURL, String type) {
        driver.get(String.format("%s/#/views/management/views/inventory-view/" + type +
                "?perspective=LIVE", basicURL));
        return new NewInventoryViewPage(driver);
    }

    public NewInventoryViewPage(WebDriver driver) {
        super(driver);
    }

    public TableWidget getTableWidget() {
        if (mainTable == null) {
            Widget.waitForWidget(wait, TableWidget.TABLE_WIDGET_CLASS);
            mainTable = TableWidget.create(driver, TableWidget.TABLE_WIDGET_CLASS, wait);
        }
        return mainTable;
    }

    public TabsWidget getTabsWidget() {
        if (tabsWidget == null) {
            Widget.waitForWidget(wait, TabsWidget.TABS_WIDGET_CLASS);
            tabsWidget = TabsWidget.create(driver, wait);
        }
        return tabsWidget;
    }

    public Wizard getWizard() {
        if (wizard == null) {
            wizard = Wizard.createWizard(driver, wait);
        }
        return wizard;
    }

    public PropertyPanel getPropertyPanel() {
        if (propertyPanel == null) {
            Widget.waitForWidget(wait, PropertyPanel.PROPERTY_PANEL_CLASS);
            propertyPanel = PropertyPanel.create(driver);
        }
        return propertyPanel;
    }

    public PropertiesFilter getPropertiesFilter() {
        if (propertiesFilter == null) {
            Widget.waitForWidget(wait, PropertiesFilter.PROPERTIES_FILTER_CLASS);
            propertiesFilter = PropertiesFilter.create(driver, wait);
        }
        return propertiesFilter;
    }

    //TODO: add getMethods for popup and property panel

    //TODO: wrap WebElement
    public WebElement getLoadBar() {
        return this.driver.findElement(By.xpath(loadBar));
    }

    public boolean isLoadBarDisplayed() {
        return getLoadBar().isDisplayed();
    }

    public int howManyRows() {
        return driver.findElements(By.xpath("//div[@class='view-v2-content']/div/div[@class='row']")).size();
    }

    public int howManyColumns() {
        return driver.findElements(By.xpath("//div[@class='view-v2-content']/div/div/div[contains(@class,'column')]")).size();
    }

    @Step("Check if table has no data")
    public boolean checkIfTableIsEmpty() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return getTableWidget().checkIfTableIsEmpty();
    }

    @Step("Change layout to Horizontal Orientation")
    public NewInventoryViewPage changeLayoutToHorizontal() {
        if (howManyRows() == 1) {
            if (ButtonPanel.create(driver, wait).isButtonDisplayed("fa fa-chevron-down")) {
                ButtonPanel.create(driver, wait).clickOnIcon("layout");
            }
            DropdownList.create(driver, wait).selectOptionWithId("TWO_ROWS");
            mainTable = null;
        }
        return this;
    }

    @Step("Change layout to Vertical Orientation")
    public NewInventoryViewPage changeLayoutToVertical() {
        DelayUtils.waitForPageToLoad(driver, wait);
        if (howManyRows() == 2) {
            if (ButtonPanel.create(driver, wait).isButtonDisplayed("fa fa-chevron-down")) {
                ButtonPanel.create(driver, wait).clickOnIcon("layout");
            }
            DropdownList.create(driver, wait).selectOptionWithId("TWO_COLUMNS");
            mainTable = null;
        }
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
    public NewInventoryViewPage SelectFirstRow() {
        DelayUtils.waitForPageToLoad(driver, wait);
        getTableWidget().selectFirstRow();
        return this;
    }

    @Step("Open Edit")
    public NewInventoryViewPage EditObject(String ActionId) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getTableWidget().callAction("EDIT", ActionId);
        return this;
    }

    @Step("Edit Text Fields")
    public NewInventoryViewPage EditTextFields(String componentId, Input.ComponentType componentType, String value) {
        DelayUtils.waitForPageToLoad(driver, wait);
        setValueOnTextType(componentId, componentType, value);
        return this;
    }

    @Step("Delete object")
    public NewInventoryViewPage DeleteObject() {
        DelayUtils.sleep(10000);
        DelayUtils.waitForPageToLoad(driver, wait);
        Button.createBySelectorAndId(driver, "a", "DeleteVLANRangeContextAction").click();
        DelayUtils.sleep(10000);
        getWizard().clickButtonByLabel("OK");
        DelayUtils.waitForPageToLoad(driver, wait);
        return this;
    }

    @Step("Change columns order")
    public NewInventoryViewPage changeColumnsOrder(String columnLabel, int position) {
        getTableWidget().changeColumnsOrder(columnLabel, position);
        DelayUtils.waitForPageToLoad(driver, wait);
        return this;
    }

    @Step("Change columns order")
    public NewInventoryViewPage changeTabsOrder(String tabLabel, int position) {
        getTabsWidget().changeTabsOrder(tabLabel, position);
        DelayUtils.waitForPageToLoad(driver, wait);
        return this;
    }

    @Step("Open save configuration wizard for table widget")
    public SaveConfigurationWizard openSaveConfigurationForTableWidgetWizard() {
        DelayUtils.waitForPageToLoad(driver, wait);
        getTableWidget().clickOnKebabMenu();
        DropdownList.create(driver, wait).selectOptionWithId("saveNewConfig");
        return new SaveConfigurationWizard(driver);
    }

    @Step("Open save configuration wizard for properties")
    public SaveConfigurationWizard openSaveConfigurationForPropertiesWizard() {
        DelayUtils.waitForPageToLoad(driver, wait);
        getPropertiesFilter().openSaveAsNewConfigurationWizard();
        return new SaveConfigurationWizard(driver);
    }

    @Step("Open save configuration wizard for page")
    public SaveConfigurationWizard openSavePageConfigurationWizard() {
        DelayUtils.waitForPageToLoad(driver, wait);
        ButtonPanel.create(driver, wait).clickOnIcon("fa fa-fw fa-floppy-o");
        return new SaveConfigurationWizard(driver);
    }

    @Step("Open save configuration wizard for tabs widget")
    public SaveConfigurationWizard openSaveTabsConfigurationWizard() {
        DelayUtils.waitForPageToLoad(driver, wait);
        getTabsWidget().openSaveConfigurationWizard();
        return new SaveConfigurationWizard(driver);
    }

    @Step("Open choose configuration for table widget wizard")
    public ChooseConfigurationWizard openChooseConfigurationForTableWidgetWizard() {
        DelayUtils.waitForPageToLoad(driver, wait);
        getTableWidget().clickOnKebabMenu();
        DropdownList.create(driver, wait).selectOptionWithId("chooseConfig");
        return new ChooseConfigurationWizard(driver);
    }

    @Step("Open choose configuration wizard for tabs")
    public ChooseConfigurationWizard openChooseConfigurationForTabsWizard() {
        DelayUtils.waitForPageToLoad(driver, wait);
        getTabsWidget().openChooseConfigurationWizard();
        return new ChooseConfigurationWizard(driver);
    }

    @Step("Open choose configuration wizard for properties")
    public ChooseConfigurationWizard openChooseConfigurationForPropertiesWizard() {
        DelayUtils.waitForPageToLoad(driver, wait);
        getPropertiesFilter().openChooseConfigurationWizard();
        return new ChooseConfigurationWizard(driver);
    }

    @Step("Open choose configuration wizard for page")
    public ChooseConfigurationWizard openChooseConfigurationForPageWizard() {
        DelayUtils.waitForPageToLoad(driver, wait);
        ButtonPanel.create(driver, wait).clickOnIcon("fa fa-fw fa-cog");
        return new ChooseConfigurationWizard(driver);
    }

    @Step("Open download configuration wizard for table widget")
    public ChooseConfigurationWizard openDownloadTableWidgetConfigurationWizard() {
        DelayUtils.waitForPageToLoad(driver, wait);
        getTableWidget().clickOnKebabMenu();
        DropdownList.create(driver, wait).selectOptionWithId("table_gql_Download");
        return new ChooseConfigurationWizard(driver);
    }

    @Step("Open download configuration wizard for tabs")
    public ChooseConfigurationWizard openDownloadConfigurationForTabsWizard() {
        DelayUtils.waitForPageToLoad(driver, wait);
        getTabsWidget().openDownloadConfigurationWizard();
        return new ChooseConfigurationWizard(driver);
    }

    @Step("Open download configuration wizard for page")
    public ChooseConfigurationWizard openDownloadConfigurationForPageWizard() {
        DelayUtils.waitForPageToLoad(driver, wait);
        ButtonPanel.create(driver, wait).clickOnIcon("fa fa-fw fa-download");
        return new ChooseConfigurationWizard(driver);
    }

    @Step("Open download configuration wizard for properties")
    public ChooseConfigurationWizard openDownloadConfigurationForPropertiesWizard() {
        DelayUtils.waitForPageToLoad(driver, wait);
        getPropertiesFilter().openDownloadConfigurationWizard();
        return new ChooseConfigurationWizard(driver);
    }

    @Step("Enable Column and apply")
    public NewInventoryViewPage enableColumnAndApply(String columnLabel) {
        enableColumn(columnLabel).clickApply();
        return new NewInventoryViewPage(driver);
    }

    @Step("Enable Column")
    public AttributesChooser enableColumn(String columnLabel) {
        getTableWidget().getAttributesChooser().enableColumnByLabel(columnLabel);
        return getTableWidget().getAttributesChooser();
    }

    @Step("Disable Column and apply")
    public NewInventoryViewPage disableColumnAndApply(String columnLabel) {
        disableColumn(columnLabel).clickApply();
        return new NewInventoryViewPage(driver);
    }

    @Step("Disable Column")
    public AttributesChooser disableColumn(String columnLabel) {
        getTableWidget().getAttributesChooser().disableColumnByLabel(columnLabel);
        return getTableWidget().getAttributesChooser();
    }

    @Step("Enable Widget Tab and apply")
    public NewInventoryViewPage enableWidgetAndApply(String widgetLabel) {
        getTabsWidget().getWidgetChooser().enableWidgetByLabel(widgetLabel).clickAdd();
        return new NewInventoryViewPage(driver);
    }

    @Step("Disable Widget Tab and apply")
    public NewInventoryViewPage disableWidgetAndApply(String widgetLabel) {
        getTabsWidget().getWidgetChooser().disableWidgetByLabel(widgetLabel).clickAdd();
        return new NewInventoryViewPage(driver);
    }

    @Step("Check first checkbox in table widget")
    public NewInventoryViewPage checkFirstCheckbox() {
        getTableWidget().selectRow(0);
        DelayUtils.waitForPageToLoad(driver, wait);
        return this;
    }

    @Step("Open Hierarchy View for selected object")
    public HierarchyViewPage goToHierarchyViewForSelectedObject() {
        DelayUtils.waitForPageToLoad(driver, wait);
        getTableWidget().getContextActions().callActionById("NAVIGATION");
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

    public String getIdOfFirstObject() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return driver.findElement(By.xpath("//div[@class='Row']")).getAttribute("id");
    }

    public boolean isOnlyOneObject(String id) {
        DelayUtils.waitForPageToLoad(driver, wait);
        return getTableWidget().howManyRowsOnFirstPage() == 1 && getIdOfFirstObject().equals(id);
    }

    private void setValueOnTextType(String componentId, Input.ComponentType componentType, String value) {
        DelayUtils.sleep();
        getWizard().getComponent(componentId, componentType).clearByAction();
        getWizard().getComponent(componentId, componentType).setSingleStringValue(value);
    }

    public Input getComponent(String componentId, Input.ComponentType componentType) {
        Input input = ComponentFactory.create(componentId, componentType, this.driver, this.wait);
        return input;
    }

}
