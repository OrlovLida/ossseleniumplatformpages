package com.oss.pages.platform;

import com.oss.framework.components.AdvancedSearch;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.filterpanel.FilterPanel;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.mainheader.ButtonPanel;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;
import com.oss.framework.widgets.Wizard;
import com.oss.framework.widgets.propertypanel.PropertiesFilter;
import com.oss.framework.widgets.propertypanel.PropertyPanel;
import com.oss.framework.widgets.tablewidget.TableWidget;
import com.oss.framework.widgets.tabswidget.TabsWidget;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

//import com.oss.framework.mainheader.ButtonPanel;

public class InventoryViewPage extends BasePage {

    private final String loadBar = "//div[@class='load-bar']";

    private TableWidget mainTable;
    private TabsWidget tabsWidget;
    private PropertyPanel propertyPanel;
    private PropertiesFilter propertiesFilter;
    private Wizard wizard;
    private InventoryViewPage inventoryViewPage;

    public InventoryViewPage(WebDriver driver) {
        super(driver);
    }

    @Step("Open Inventory View")
    public static InventoryViewPage goToInventoryViewPage(WebDriver driver, String basicURL, String context) {
        driver.get(String.format("%s/#/views/management/views/inventory-view/" + context +
                "?perspective=LIVE", basicURL));
        return new InventoryViewPage(driver);
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
            propertiesFilter = PropertiesFilter.create(driver);
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

    @Step("Change layout to Horizontal Orientation")
    public InventoryViewPage changeLayoutToHorizontal() {
        ButtonPanel.create(driver, wait).getButtonIcon("layout").click();
        DropdownList.create(driver, wait).selectOptionWithIconContains("Horizontal");
        return this;
    }

    @Step("Change layout to Vertical Orientation")
    public InventoryViewPage changeLayoutToVertical() {
        DelayUtils.waitForPageToLoad(driver, wait);
        ButtonPanel.create(driver, wait).getButtonIcon("layout").click();
        DropdownList.create(driver, wait).selectOptionWithIconContains("Vertical");
        return this;
    }

    @Step("Open Filter Panel")
    public FilterPanel openFilterPanel() {
        DelayUtils.waitForPageToLoad(driver,wait);
        AdvancedSearch advancedSearch = new AdvancedSearch(driver, wait);
        advancedSearch.openSearchPanel();
        return new FilterPanel(driver);
    }

    @Step("Clear all tags")
    public InventoryViewPage clearAllTags(){
        DelayUtils.waitForPageToLoad(driver,wait);
        AdvancedSearch advancedSearch = new AdvancedSearch(driver, wait);
        advancedSearch.clickOnTagByLabel("Clear");
        return this;
    }

    public boolean isAnyTagsVisible(){
        DelayUtils.waitForPageToLoad(driver,wait);
        AdvancedSearch advancedSearch = new AdvancedSearch(driver, wait);
        return advancedSearch.howManyTagsIsVisible()==0;
    }

    public String getIdOfFirstObject(){
        DelayUtils.waitForPageToLoad(driver,wait);
        return driver.findElement(By.xpath("//div[@class='Row']")).getAttribute("id");
    }

    public boolean isOnlyOneObject(String id){
        DelayUtils.waitForPageToLoad(driver,wait);
        return getTableWidget().howManyRowsOnFirstPage() == 1 && getIdOfFirstObject().equals(id);
    }

}
